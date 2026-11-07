package com.creditgo.decaissements.service;

import com.creditgo.decaissements.client.CreditFeignClient;
import com.creditgo.decaissements.dto.CreateDisbursementRequest;
import com.creditgo.decaissements.dto.CreateTreasuryAccountRequest;
import com.creditgo.decaissements.dto.DisbursementResponse;
import com.creditgo.decaissements.dto.TreasuryAccountResponse;
import com.creditgo.decaissements.entity.Disbursement;
import com.creditgo.decaissements.entity.TreasuryAccount;
import com.creditgo.decaissements.exception.InsufficientFundsException;
import com.creditgo.decaissements.exception.ResourceNotFoundException;
import com.creditgo.decaissements.exception.ValueNotValidException;
import com.creditgo.decaissements.repository.DisbursementRepository;
import com.creditgo.decaissements.repository.TreasuryAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class DecaissementsService {

    private final DisbursementRepository disbursementRepository;
    private final TreasuryAccountRepository treasuryAccountRepository;
    private final CreditFeignClient creditFeignClient;

    public DecaissementsService(DisbursementRepository disbursementRepository,
                                 TreasuryAccountRepository treasuryAccountRepository,
                                 CreditFeignClient creditFeignClient) {
        this.disbursementRepository = disbursementRepository;
        this.treasuryAccountRepository = treasuryAccountRepository;
        this.creditFeignClient = creditFeignClient;
    }

    public DisbursementResponse creerVersement(CreateDisbursementRequest request) {
        valider(request.getMontant().compareTo(BigDecimal.ZERO) > 0);
        TreasuryAccount compte = treasuryAccountRepository.findById(request.getTreasuryAccountId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Compte treasury introuvable avec l'id : " + request.getTreasuryAccountId()));

        if (compte.getSoldeDisponible().compareTo(request.getMontant()) < 0) {
            throw new InsufficientFundsException(
                    "Solde insuffisant : " + compte.getSoldeDisponible() + " < " + request.getMontant());
        }

        disvalider(request.getLoanApplicationId(), request.getTreasuryAccountId(), request.getMontant());

        Disbursement virement = new Disbursement(
                request.getLoanApplicationId(),
                request.getTreasuryAccountId(),
                request.getMontant(),
                LocalDate.now()
        );
        virement = disbursementRepository.save(virement);

        compte.setSoldeDisponible(compte.getSoldeDisponible().subtract(request.getMontant()));
        treasuryAccountRepository.save(compte);
        creditFeignClient.activateSchedule(request.getLoanApplicationId());

        return new DisbursementResponse(virement);
    }

    private void disvalider(Long loanApplicationId, Long treasuryAccountId, BigDecimal montant) {
        creditFeignClient.validateDisbursement(loanApplicationId, montant);
        if (treasuryAccountId == null || treasuryAccountId <= 0) {
            throw new ResourceNotFoundException("ID du compte treasury invalide : " + treasuryAccountId);
        }
    }

    private void valider(boolean valid) {
        if (!valid) {
            throw new ValueNotValidException("La valeur fournie n'est pas valide");
        }
    }

    @Transactional(readOnly = true)
    public List<DisbursementResponse> getAllDisbursements() {
        return disbursementRepository.findAll().stream()
                .map(DisbursementResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public DisbursementResponse getDisbursementById(Long id) {
        Disbursement entity = disbursementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Versement introuvable avec l'id : " + id));
        return new DisbursementResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<DisbursementResponse> getDisbursementsByLoanApplicationId(Long loanApplicationId) {
        return disbursementRepository.findByLoanApplicationId(loanApplicationId).stream()
                .map(DisbursementResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DisbursementResponse> getDisbursementsByTreasuryAccountId(Long treasuryAccountId) {
        return disbursementRepository.findByTreasuryAccountId(treasuryAccountId).stream()
                .map(DisbursementResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DisbursementResponse> getDisbursementsByStatut(String statut) {
        return disbursementRepository.findByStatut(statut).stream()
                .map(DisbursementResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public TreasuryAccountResponse getCompteById(Long id) {
        TreasuryAccount entity = treasuryAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compte treasury introuvable avec l'id : " + id));
        return new TreasuryAccountResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<TreasuryAccountResponse> getAllComptes() {
        return treasuryAccountRepository.findAll().stream()
                .map(TreasuryAccountResponse::new)
                .toList();
    }

    public TreasuryAccountResponse creerCompte(CreateTreasuryAccountRequest request) {
        valider(request.getSoldeDisponible().compareTo(BigDecimal.ZERO) >= 0);
        TreasuryAccount entity = new TreasuryAccount(
                request.getLibelle(),
                request.getSoldeDisponible()
        );
        entity = treasuryAccountRepository.save(entity);
        return new TreasuryAccountResponse(entity);
    }

    public DisbursementResponse effectiveDisbursement(Long id) {
        Disbursement virement = disbursementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Versement introuvable avec l'id : " + id));

        if (virement.estEffectue()) {
            throw new ValueNotValidException("Le versement est déjà effectif");
        }

        virement.setStatut(Disbursement.Statut.EFFECTUE.name());
        virement = disbursementRepository.save(virement);

        return new DisbursementResponse(virement);
    }

    public DisbursementResponse testerVersement(CreateDisbursementRequest request) {
        TreasuryAccount compte = treasuryAccountRepository.findById(request.getTreasuryAccountId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Compte treasury introuvable avec l'id : " + request.getTreasuryAccountId()));

        if (compte.getSoldeDisponible().compareTo(request.getMontant()) < 0) {
            throw new InsufficientFundsException(
                    "Solde insuffisant : " + compte.getSoldeDisponible() + " < " + request.getMontant());
        }

        creditFeignClient.validateDisbursement(request.getLoanApplicationId(), request.getMontant());
        creditFeignClient.activateSchedule(request.getLoanApplicationId());
        return new DisbursementResponse();
    }

    public DisbursementResponse effectuerVersement(Long id) {
        Disbursement virement = disbursementRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Versement introuvable avec l'id : " + id));

        if (virement.estEffectue()) {
            throw new ValueNotValidException("Le versement est déjà effectif");
        }

        virement.setStatut(Disbursement.Statut.EFFECTUE.name());
        virement = disbursementRepository.save(virement);

        return new DisbursementResponse(virement);
    }

}
