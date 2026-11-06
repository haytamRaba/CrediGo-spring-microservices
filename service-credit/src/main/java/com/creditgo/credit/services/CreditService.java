package com.creditgo.credit.service;

import com.creditgo.credit.dto.CreateLoanApplicationRequest;
import com.creditgo.credit.dto.LoanApplicationResponse;
import com.creditgo.credit.dto.TransitionRequest;
import com.creditgo.credit.dto.RepaymentScheduleResponse;
import com.creditgo.credit.entity.LoanApplication;
import com.creditgo.credit.entity.RepaymentSchedule;
import com.creditgo.credit.exception.AlreadyGeneratedException;
import com.creditgo.credit.exception.InvalidTransitionException;
import com.creditgo.credit.exception.ResourceNotFoundException;
import com.creditgo.credit.exception.ValueNotValidException;
import com.creditgo.credit.repository.LoanApplicationRepository;
import com.creditgo.credit.repository.RepaymentScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.creditgo.credit.entity.LoanApplication.Statut;

@Service
@Transactional
public class CreditService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final RepaymentScheduleRepository repaymentScheduleRepository;

    public CreditService(LoanApplicationRepository loanApplicationRepository,
                         RepaymentScheduleRepository repaymentScheduleRepository) {
        this.loanApplicationRepository = loanApplicationRepository;
        this.repaymentScheduleRepository = repaymentScheduleRepository;
    }

    public LoanApplicationResponse createLoanApplication(CreateLoanApplicationRequest request) {
        valider(createLoanApplicationRequestValidation(request));
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

    private void valider(boolean valid) {
        if (!valid) {
            throw new ValueNotValidException("La valeur fournie n'est pas valide");
        }
    }

    private boolean createLoanApplicationRequestValidation(CreateLoanApplicationRequest request) {
        return request.getMontantDemande().compareTo(BigDecimal.ZERO) > 0
                && request.getDureeMois() > 0
                && request.getTauxPropose().compareTo(BigDecimal.ZERO) >= 0;
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
                .orElseThrow(() -> new ResourceNotFoundException("Demande de credit introuvable avec l'id : " + id));
        return new LoanApplicationResponse(entity);
    }

    public LoanApplicationResponse etudier(Long id, TransitionRequest request) {
        LoanApplication entity = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande de credit introuvable avec l'id : " + id));

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
                .orElseThrow(() -> new ResourceNotFoundException("Demande de credit introuvable avec l'id : " + id));

        if (!Statut.ACCORDE.name().equals(entity.getStatut())) {
            throw new InvalidTransitionException("Seules les demandes accordées peuvent être bloquées");
        }

        entity.setStatut(Statut.DEBLOQUE.name());
        entity = loanApplicationRepository.save(entity);
        return new LoanApplicationResponse(entity);
    }

    public LoanApplicationResponse retirer(Long id) {
        LoanApplication entity = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande de credit introuvable avec l'id : " + id));

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

    public RepaymentScheduleResponse genererRemarque(Long id) {
        LoanApplication entity = loanApplicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande de credit introuvable avec l'id : " + id));

        if (!Statut.DEBLOQUE.name().equals(entity.getStatut())) {
            throw new InvalidTransitionException("Le calendrier ne peut être généré que pour une demande débloquée");
        }

        if (repaymentScheduleRepository.existsByLoanApplicationId(id)) {
            throw new AlreadyGeneratedException("Le calendrier d'amortissement existe déjà pour cette demande");
        }

        BigDecimal tauxMensuel = entity.getTauxPropose().divide(new BigDecimal("12"), 10, RoundingMode.HALF_UP);
        Integer duree = entity.getDureeMois();
        BigDecimal capital = entity.getMontantAccorde();
        BigDecimal periodique = capital.multiply(tauxMensuel)
                .divide(BigDecimal.ONE.subtract(BigDecimal.ONE
                        .divide(BigDecimal.ONE.add(tauxMensuel).pow(duree), 10, RoundingMode.HALF_UP)), 2, RoundingMode.HALF_UP);

        List<RepaymentSchedule> echeances = new ArrayList<>();
        BigDecimal resteCapital = capital;
        LocalDate date = entity.getDateDepot().plusMonths(1);

        for (int i = 1; i <= duree; i++) {
            BigDecimal interets = resteCapital.multiply(tauxMensuel).setScale(2, RoundingMode.HALF_UP);
            BigDecimal capitalRestant = resteCapital.subtract(interets);
            BigDecimal montant = interets.add(capitalRestant).setScale(2, RoundingMode.HALF_UP);

            if (i == duree) {
                montant = resteCapital.setScale(2, RoundingMode.HALF_UP);
            }

            RepaymentSchedule echeance = new RepaymentSchedule(
                    id, i, date, montant.subtract(interets), interets, RepaymentSchedule.Statut.A_PAYER.name()
            );
            echeances.add(echeance);
            resteCapital = resteCapital.subtract(montant.subtract(interets));
            date = date.plusMonths(1);
        }

        repaymentScheduleRepository.saveAll(echeances);
        return new RepaymentScheduleResponse(echeances.get(0));
    }
}
