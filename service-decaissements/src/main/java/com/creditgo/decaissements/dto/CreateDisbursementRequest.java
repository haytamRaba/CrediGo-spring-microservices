package com.creditgo.decaissements.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class CreateDisbursementRequest {

    @NotNull(message = "La demande de credit est obligatoire")
    @Positive(message = "L'id de la demande de crédit doit être positif")
    private Long loanApplicationId;

    @NotNull(message = "Le compte treasury est obligatoire")
    @Positive(message = "L'id du compte treasury doit être positif")
    private Long treasuryAccountId;

    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant doit être strictement positif")
    private BigDecimal montant;

    public CreateDisbursementRequest() {
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
}
