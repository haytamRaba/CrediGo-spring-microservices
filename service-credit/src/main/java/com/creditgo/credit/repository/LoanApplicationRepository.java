package com.creditgo.credit.repository;

import com.creditgo.credit.entity.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {

    Optional<LoanApplication> findByClientId(Long clientId);

    List<LoanApplication> findByStatut(String statut);

    boolean existsByClientIdAndStatut(Long clientId, String statut);

    @Modifying
    @Transactional
    @Query("UPDATE LoanApplication l SET l.statut = :statut WHERE l.id = :id")
    int updateStatusById(Long id, String statut);
}
