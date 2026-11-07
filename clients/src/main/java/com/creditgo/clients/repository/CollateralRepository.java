package com.creditgo.clients.repository;

import com.creditgo.clients.entity.Collateral;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollateralRepository extends JpaRepository<Collateral, Long> {

    List<Collateral> findByClientId(Long clientId);
}
