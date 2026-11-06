package com.creditgo.decaissements.repository;

import com.creditgo.decaissements.entity.Disbursement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DisbursementRepository extends JpaRepository<Disbursement, Long> {

    List<Disbursement> findByLoanApplicationId(Long loanApplicationId);

    List<Disbursement> findByTreasuryAccountId(Long treasuryAccountId);

    List<Disbursement> findByStatut(String statut);

    Optional<Disbursement> findByIdAndStatut(Long id, String statut);
}
