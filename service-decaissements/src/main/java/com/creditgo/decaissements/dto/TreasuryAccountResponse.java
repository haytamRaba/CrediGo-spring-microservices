package com.creditgo.decaissements.dto;

import com.creditgo.decaissements.entity.TreasuryAccount;

import java.math.BigDecimal;

public class TreasuryAccountResponse {

    private Long id;
    private String libelle;
    private BigDecimal soldeDisponible;

    public TreasuryAccountResponse() {
    }

    public TreasuryAccountResponse(TreasuryAccount entity) {
        this.id = entity.getId();
        this.libelle = entity.getLibelle();
        this.soldeDisponible = entity.getSoldeDisponible();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public BigDecimal getSoldeDisponible() {
        return soldeDisponible;
    }

    public void setSoldeDisponible(BigDecimal soldeDisponible) {
        this.soldeDisponible = soldeDisponible;
    }
}
