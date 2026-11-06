package com.creditgo.decaissements.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class CreateTreasuryAccountRequest {

    @NotBlank(message = "Le libellé est obligatoire")
    private String libelle;

    @NotNull(message = "Le solde disponible est obligatoire")
    @DecimalMin(value = "0.0", message = "Le solde ne peut pas être négatif")
    private BigDecimal soldeDisponible;

    public CreateTreasuryAccountRequest() {
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
