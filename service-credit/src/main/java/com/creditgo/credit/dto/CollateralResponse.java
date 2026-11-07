package com.creditgo.credit.dto;

import java.math.BigDecimal;

public class CollateralResponse {

    private Long id;
    private Long clientId;
    private String type;
    private BigDecimal valeur;
    private String description;

    public CollateralResponse() {
    }

    public CollateralResponse(Long id, Long clientId, String type, BigDecimal valeur, String description) {
        this.id = id;
        this.clientId = clientId;
        this.type = type;
        this.valeur = valeur;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
