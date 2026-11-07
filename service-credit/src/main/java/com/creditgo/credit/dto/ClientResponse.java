package com.creditgo.credit.dto;

import java.math.BigDecimal;

public class ClientResponse {

    private Long id;
    private String nom;
    private String email;
    private BigDecimal revenusMensuels;
    private Integer scoreInterne;
    private String statut;

    public ClientResponse() {
    }

    public ClientResponse(Long id, String nom, String email, BigDecimal revenusMensuels,
                          Integer scoreInterne, String statut) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.revenusMensuels = revenusMensuels;
        this.scoreInterne = scoreInterne;
        this.statut = statut;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getRevenusMensuels() {
        return revenusMensuels;
    }

    public void setRevenusMensuels(BigDecimal revenusMensuels) {
        this.revenusMensuels = revenusMensuels;
    }

    public Integer getScoreInterne() {
        return scoreInterne;
    }

    public void setScoreInterne(Integer scoreInterne) {
        this.scoreInterne = scoreInterne;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}
