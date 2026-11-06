package com.creditgo.decaissements.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "treasury_accounts")
public class TreasuryAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le libellé est obligatoire")
    @Column(nullable = false)
    private String libelle;

    @NotNull(message = "Le solde disponible est obligatoire")
    @DecimalMin(value = "0.0", message = "Le solde ne peut pas être négatif")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal soldeDisponible;

    public TreasuryAccount() {
    }

    public TreasuryAccount(String libelle, BigDecimal soldeDisponible) {
        this.libelle = libelle;
        this.soldeDisponible = soldeDisponible;
    }

    public Long getId() {
        return id;
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
