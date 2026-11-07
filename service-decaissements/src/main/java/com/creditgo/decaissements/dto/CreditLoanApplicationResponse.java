package com.creditgo.decaissements.dto;

import java.math.BigDecimal;

public class CreditLoanApplicationResponse {

    private Long id;
    private BigDecimal montantAccorde;
    private String statut;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMontantAccorde() {
        return montantAccorde;
    }

    public void setMontantAccorde(BigDecimal montantAccorde) {
        this.montantAccorde = montantAccorde;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
