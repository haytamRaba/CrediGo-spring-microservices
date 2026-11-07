package com.creditgo.credit.service;

import com.creditgo.credit.client.ClientsFeignClient;
import com.creditgo.credit.dto.ClientResponse;


import com.creditgo.credit.dto.CollateralResponse;
import com.creditgo.credit.dto.CreateLoanApplicationRequest;
import com.creditgo.credit.dto.EnrichedLoanApplicationResponse;
import com.creditgo.credit.dto.LoanApplicationResponse;
import com.creditgo.credit.dto.RepaymentScheduleResponse;
import com.creditgo.credit.dto.TransitionRequest;
import com.creditgo.credit.entity.LoanApplication;
import com.creditgo.credit.entity.RepaymentSchedule;
import com.creditgo.credit.exception.InvalidTransitionException;
import com.creditgo.credit.exception.LoanApplicationNotFoundException;
import com.creditgo.credit.exception.OpenFeignException;
import com.creditgo.credit.exception.ResourceNotFoundException;

import com.creditgo.credit.repository.LoanApplicationRepository;
import com.creditgo.credit.repository.RepaymentScheduleRepository;
import com.creditgo.credit.client.DecaissementsFeignClient;
import com.creditgo.credit.dto.DisbursementResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import static com.creditgo.credit.entity.LoanApplication.Statut;

@Service
@Transactional
public class CreditService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final RepaymentScheduleRepository repaymentScheduleRepository;
    private final DecaissementsFeignClient decaissementsFeignClient;
    private final ClientsFeignClient clientsFeignClient;

    public CreditService(LoanApplicationRepository loanApplicationRepository,
                         RepaymentScheduleRepository repaymentScheduleRepository,
                         DecaissementsFeignClient decaissementsFeignClient,
                         ClientsFeignClient clientsFeignClient) {
        this.loanApplicationRepository = loanApplicationRepository;
        this.repaymentScheduleRepository = repaymentScheduleRepository;
        this.decaissementsFeignClient = decaissementsFeignClient;
        this.clientsFeignClient = clientsFeignClient;
    }

    public LoanApplicationResponse createLoanApplication(CreateLoanApplicationRequest request) {
        valider(createLoanApplicationRequestValidation(request));
        valider(clientsFeignClient.findClientById(request.getClientId()) != null);

        LoanApplication entity = new LoanApplication(
                request.getClientId(),
                request.getMontantDemande(),
                request.getDureeMois(),
                request.getTauxPropose(),
                request.getMotif()
        );
        entity = loanApplicationRepository.save(entity);
        return new LoanApplicationResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<LoanApplicationResponse> getAllLoanApplications() {
        return loanApplicationRepository.findAll().stream()
                .map(LoanApplicationResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public LoanApplicationResponse getLoanApplicationById(Long id) {
        LoanApplication entity = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new LoanApplicationNotFoundException("Demande de credit introuvable avec l'id : " + id));
        return new LoanApplicationResponse(entity);
    }

    public EnrichedLoanApplicationResponse getEnrichedLoanApplicationById(Long id) {
        LoanApplication entity = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new LoanApplicationNotFoundException("Demande de credit introuvable avec l'id : " + id));

        ClientResponse client = clientsFeignClient.findClientById(entity.getClientId());
        List<CollateralResponse> garanties = clientsFeignClient.findCollateralsByClientId(entity.getClientId());

        RepaymentScheduleResponse echeancier = getRepaymentScheduleSummary(entity.getId());
        DisbursementResponse deconcilement = getDisbursementSummary(entity.getId());

        return new EnrichedLoanApplicationResponse(
                entity.getId(), entity.getClientId(), client,
                entity.getMontantDemande(), entity.getMontantAccorde(),
                entity.getScoreEtude(), entity.getDureeMois(),
                entity.getTauxPropose(), entity.getMotif(), entity.getStatut(),
                entity.getDateDepot(), garanties, echeancier, deconcilement
        );
    }

    private RepaymentScheduleResponse getRepaymentScheduleSummary(Long loanApplicationId) {
        List<RepaymentSchedule> echeances = repaymentScheduleRepository.findByLoanApplicationId(loanApplicationId);
        if (echeances.isEmpty()) {
            return null;
        }
        return new RepaymentScheduleResponse(echeances.get(0));
    }

    public RepaymentScheduleResponse genererRemarque(Long id) {
        LoanApplication entity = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new LoanApplicationNotFoundException("Demande de credit introuvable avec l'id : " + id));
        if (repaymentScheduleRepository.existsByLoanApplicationId(id)) {
            throw new com.creditgo.credit.exception.AlreadyGeneratedException("L'échéancier est déjà généré");
        }

        List<RepaymentSchedule> echeances = new ArrayList<>();
        for (int i = 1; i <= entity.getDureeMois(); i++) {
            echeances.add(new RepaymentSchedule(
                    entity.getId(),
                    i,
                    entity.getDateDepot().plusMonths(i),
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    RepaymentSchedule.Statut.A_PAYER.name()
            ));
        }
        return new RepaymentScheduleResponse(repaymentScheduleRepository.saveAll(echeances).get(0));
    }

    private DisbursementResponse getDisbursementSummary(Long loanApplicationId) {
        List<DisbursementResponse> paiements = decaissementsFeignClient.getDisbursementsByLoanApplicationId(loanApplicationId);
        if (paiements.isEmpty()) {
            return null;
        }
        return paiements.get(0);
    }

    public LoanApplicationResponse etudier(Long id, TransitionRequest request) {
        LoanApplication entity = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new LoanApplicationNotFoundException("Demande de credit introuvable avec l'id : " + id));

        String nouvelleStatut = request.getStatut();
        if (entity.estDepose()) {
            if (!"EN_ETUDE".equals(nouvelleStatut)) {
                throw new InvalidTransitionException("Une demande déposée ne peut pas passer directement en " + nouvelleStatut);
            }
        } else if (entity.estEnEtude()) {
            if (!"ACCORDE".equals(nouvelleStatut) && !"REFUSE".equals(nouvelleStatut)) {
                throw new InvalidTransitionException("Une demande en étude ne peut passer que en ACCORDE ou REFUSE");
            }
        } else {
            throw new InvalidTransitionException("Une demande ne peut pas passer de " + entity.getStatut() + " à " + nouvelleStatut);
        }

        entity.setStatut(nouvelleStatut);
        entity = loanApplicationRepository.save(entity);
        return new LoanApplicationResponse(entity);
    }

    public LoanApplicationResponse bloquer(Long id) {
        LoanApplication entity = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new LoanApplicationNotFoundException("Demande de credit introuvable avec l'id : " + id));

        if (!Statut.ACCORDE.name().equals(entity.getStatut())) {
            throw new InvalidTransitionException("Seules les demandes accordées peuvent être bloquées");
        }

        entity.setStatut(Statut.DEBLOQUE.name());
        entity = loanApplicationRepository.save(entity);
        return new LoanApplicationResponse(entity);
    }

    public LoanApplicationResponse retirer(Long id) {
        LoanApplication entity = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new LoanApplicationNotFoundException("Demande de credit introuvable avec l'id : " + id));

        if (!Statut.ACCORDE.name().equals(entity.getStatut())) {
            throw new InvalidTransitionException("Seules les demandes accordées peuvent être retirées");
        }

        entity.setStatut(Statut.REFUSE.name());
        entity = loanApplicationRepository.save(entity);
        return new LoanApplicationResponse(entity);
    }

    public List<LoanApplicationResponse> getByStatut(String statut) {
        return loanApplicationRepository.findByStatut(statut).stream()
                .map(LoanApplicationResponse::new)
                .toList();
    }

    public LoanApplicationResponse accorder(Long id, TransitionRequest request) {
        LoanApplication entity = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new LoanApplicationNotFoundException("Demande de credit introuvable avec l'id : " + id));

        if (!Statut.EN_ETUDE.name().equals(entity.getStatut())) {
            throw new InvalidTransitionException("Seul un dossier en étude peut être accordé");
        }

        Integer scoreExterne = consulterScoreExterne(entity.getClientId());
        if (scoreExterne == null || scoreExterne < 50) {
            entity.setStatut(Statut.REFUSE.name());
            entity = loanApplicationRepository.save(entity);
            return new LoanApplicationResponse(entity);
        }

        entity.setScoreEtude(scoreExterne);
        entity.setMontantAccorde(entity.getMontantDemande());
        entity.setStatut(Statut.ACCORDE.name());
        entity = loanApplicationRepository.save(entity);
        return new LoanApplicationResponse(entity);
    }

    public LoanApplicationResponse refuser(Long id, TransitionRequest request) {
        LoanApplication entity = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new LoanApplicationNotFoundException("Demande de credit introuvable avec l'id : " + id));

        if (!Statut.EN_ETUDE.name().equals(entity.getStatut())) {
            throw new InvalidTransitionException("Seul un dossier en étude peut être refusé");
        }

        entity.setStatut(Statut.REFUSE.name());
        entity = loanApplicationRepository.save(entity);
        return new LoanApplicationResponse(entity);
    }

    private Integer consulterScoreExterne(Long clientId) {
        ClientResponse client = clientsFeignClient.findClientByEmail(clientId.toString());
        if (client == null) {
            throw new OpenFeignException("Impossible de récupérer le client auprès de service-clients");
        }
        return client.getScoreInterne();
    }

    private void consulterClientExterne(Long clientId, Integer scoreEtude) {
        ClientResponse client = clientsFeignClient.findClientByEmail(clientId.toString());
        if (client == null) {
            throw new OpenFeignException("Client introuvable auprès de service-clients");
        }
        if (client.getScoreInterne() == null) {
            throw new OpenFeignException("Score client introuvable auprès de service-clients");
        }
        if (client.getScoreInterne() < 50) {
            throw new OpenFeignException("Score client trop bas : " + client.getScoreInterne());
        }
        if (client.getStatut() == null || !client.getStatut().equalsIgnoreCase("ACTIVE")) {
            throw new OpenFeignException("Client non actif auprès de service-clients");
        }
        List<CollateralResponse> garanties = clientsFeignClient.findCollateralsByClientId(clientId);
        if (garanties == null || garanties.isEmpty()) {
            throw new OpenFeignException("Aucune garantie trouvée pour le client");
        }
        for (CollateralResponse garantie : garanties) {
            if (garantie.getValeur() == null || garantie.getValeur().compareTo(BigDecimal.ZERO) <= 0) {
                throw new OpenFeignException("Une garantie a une valeur invalide");
            }
        }
    }

    public LoanApplicationResponse consulterAccord(Long id) {
        LoanApplication entity = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new LoanApplicationNotFoundException("Demande de credit introuvable avec l'id : " + id));

        if (!Statut.ACCORDE.name().equals(entity.getStatut())) {
            throw new InvalidTransitionException("Le dossier n'est pas accordé");
        }

        return new LoanApplicationResponse(entity);
    }

    public void consulterMontantAccorde(Long id, BigDecimal montant) {
        LoanApplication entity = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new LoanApplicationNotFoundException("Demande de credit introuvable avec l'id : " + id));

        if (!Statut.ACCORDE.name().equals(entity.getStatut())) {
            throw new InvalidTransitionException("Le dossier n'est pas accordé");
        }

        if (entity.getMontantAccorde() == null || entity.getMontantAccorde().compareTo(montant) < 0) {
            throw new ResourceNotFoundException("Montant accordé insuffisant pour le dossier : " + id);
        }
    }

    public void activerEcheancier(Long id) {
        LoanApplication entity = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new LoanApplicationNotFoundException("Demande de credit introuvable avec l'id : " + id));

        if (!Statut.ACCORDE.name().equals(entity.getStatut())) {
            throw new InvalidTransitionException("Le dossier n'est pas accordé, impossible d'activer l'échéancier");
        }

        if (repaymentScheduleRepository.existsByLoanApplicationId(id)) {
            List<RepaymentSchedule> echeances = repaymentScheduleRepository.findByLoanApplicationId(id);
            List<RepaymentSchedule> aActiver = new ArrayList<>();
            for (RepaymentSchedule ec : echeances) {
                if (ec.getStatut() == null || ec.getStatut().isEmpty()
                        || ec.getStatut().equalsIgnoreCase("EN_ATTENTE")) {
                    ec.setStatut(RepaymentSchedule.Statut.A_PAYER.name());
                    aActiver.add(ec);
                }
            }
            if (!aActiver.isEmpty()) {
                repaymentScheduleRepository.saveAll(aActiver);
            }
        } else {
            List<RepaymentSchedule> nouvellesEcheances = new ArrayList<>();
            for (int i = 1; i <= entity.getDureeMois(); i++) {
                RepaymentSchedule ec = new RepaymentSchedule(
                        entity.getId(),
                        i,
                        java.time.LocalDate.now().plusMonths(i),
                        new BigDecimal("0.0"),
                        new BigDecimal("0.0"),
                        RepaymentSchedule.Statut.A_PAYER.name()
                );
                nouvellesEcheances.add(ec);
            }
            repaymentScheduleRepository.saveAll(nouvellesEcheances);
        }
    }

    public void desactiverEcheancier(Long id) {
        LoanApplication entity = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new LoanApplicationNotFoundException("Demande de credit introuvable avec l'id : " + id));

        if (!Statut.ACCORDE.name().equals(entity.getStatut())) {
            throw new InvalidTransitionException("Le dossier n'est pas accordé, impossible de désactiver l'échéancier");
        }

        List<RepaymentSchedule> echeances = repaymentScheduleRepository.findByLoanApplicationId(id);
        for (RepaymentSchedule ec : echeances) {
            if (ec.getStatut() == null || ec.getStatut().isEmpty()
                    || ec.getStatut().equalsIgnoreCase(RepaymentSchedule.Statut.A_PAYER.name())) {
                ec.setStatut("EN_ATTENTE");
            }
        }
        repaymentScheduleRepository.saveAll(echeances);
    }

    public void consulterCompteTreasury(Long id) {
        if (id == null || id <= 0) {
            throw new ResourceNotFoundException("ID du compte treasury invalide : " + id);
        }
    }

    private void valider(boolean valid) {
        if (!valid) {
            throw new IllegalArgumentException("La valeur fournie n'est pas valide");
        }
    }

    private boolean createLoanApplicationRequestValidation(CreateLoanApplicationRequest request) {
        return request.getMontantDemande().compareTo(BigDecimal.ZERO) > 0
                && request.getDureeMois() > 0
                && request.getTauxPropose().compareTo(BigDecimal.ZERO) >= 0;
    }
}
