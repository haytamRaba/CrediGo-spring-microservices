package com.creditgo.credit.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class CreateLoanApplicationRequest {

    @NotNull(message = "Le client est obligatoire")
    @Positive(message = "L'id du client doit être positif")
    private Long clientId;

    @NotNull(message = "Le montant demandé est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant demandé doit être strictement positif")
    private BigDecimal montantDemande;

    @NotNull(message = "La durée est obligatoire")
    @Min(value = 1, message = "La durée doit être positive")
    private Integer dureeMois;

    @NotNull(message = "Le taux est obligatoire")
    @DecimalMin(value = "0.0", message = "Le taux doit être positif ou nul")
    private BigDecimal tauxPropose;

    @NotNull(message = "Le motif est obligatoire")
    private String motif;

    public CreateLoanApplicationRequest() {
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public BigDecimal getMontantDemande() {
        return montantDemande;
    }

    public void setMontantDemande(BigDecimal montantDemande) {
        this.montantDemande = montantDemande;
    }

    public Integer getDureeMois() {
        return dureeMois;
    }

    public void setDureeMois(Integer dureeMois) {
        this.dureeMois = dureeMois;
    }

    public BigDecimal getTauxPropose() {
        return tauxPropose;
    }

    public void setTauxPropose(BigDecimal tauxPropose) {
        this.tauxPropose = tauxPropose;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }
}
