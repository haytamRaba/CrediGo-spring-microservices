package com.creditgo.decaissements.repository;

import com.creditgo.decaissements.entity.TreasuryAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TreasuryAccountRepository extends JpaRepository<TreasuryAccount, Long> {

    Optional<TreasuryAccount> findByLibelle(String libelle);
}
