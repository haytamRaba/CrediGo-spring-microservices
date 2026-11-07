package com.creditgo.clients.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "collaterals")
public class Collateral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le client est obligatoire")
    @Column(nullable = false)
    private Long clientId;

    @NotBlank(message = "Le type est obligatoire")
    @Column(nullable = false, length = 30)
    private String type;

    @NotNull(message = "La valeur est obligatoire")
    @DecimalMin(value = "0.0", message = "La valeur doit être positive ou nulle")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valeur;

    @NotBlank(message = "La description est obligatoire")
    @Column(nullable = false, length = 255)
    private String description;

    public Collateral() {
    }

    public Collateral(Long clientId, String type, BigDecimal valeur, String description) {
        this.clientId = clientId;
        this.type = type;
        this.valeur = valeur;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getValeur() {
        return valeur;
    }

    public void setValeur(BigDecimal valeur) {
        this.valeur = valeur;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public enum CollateralType {
        IMMOBILIER, VASSE_ENTREPRISE, ORDINATEUR, VOITURE, AUTRES
    }
}
