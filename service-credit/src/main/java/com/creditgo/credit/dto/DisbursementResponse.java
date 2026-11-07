package com.creditgo.credit.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DisbursementResponse {

    private Long id;
    private Long loanApplicationId;
    private Long treasuryAccountId;
    private BigDecimal montant;
    private LocalDate date;
    private String statut;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLoanApplicationId() {
        return loanApplicationId;
    }

    public void setLoanApplicationId(Long loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public Long getTreasuryAccountId() {
        return treasuryAccountId;
    }

    public void setTreasuryAccountId(Long treasuryAccountId) {
        this.treasuryAccountId = treasuryAccountId;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
