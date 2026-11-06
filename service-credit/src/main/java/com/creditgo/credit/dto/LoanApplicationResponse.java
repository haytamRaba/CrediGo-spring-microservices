package com.creditgo.credit.dto;

import com.creditgo.credit.entity.LoanApplication;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LoanApplicationResponse {

    private Long id;
    private Long clientId;
    private BigDecimal montantDemande;
    private BigDecimal montantAccorde;
    private Integer scoreEtude;
    private Integer dureeMois;
    private BigDecimal tauxPropose;
    private String motif;
    private String statut;
    private LocalDate dateDepot;

    public LoanApplicationResponse() {
    }

    public LoanApplicationResponse(LoanApplication entity) {
        this.id = entity.getId();
        this.clientId = entity.getClientId();
        this.montantDemande = entity.getMontantDemande();
        this.montantAccorde = entity.getMontantAccorde();
        this.scoreEtude = entity.getScoreEtude();
        this.dureeMois = entity.getDureeMois();
        this.tauxPropose = entity.getTauxPropose();
        this.motif = entity.getMotif();
        this.statut = entity.getStatut();
        this.dateDepot = entity.getDateDepot();
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

    public BigDecimal getMontantDemande() {
        return montantDemande;
    }

    public void setMontantDemande(BigDecimal montantDemande) {
        this.montantDemande = montantDemande;
    }

    public BigDecimal getMontantAccorde() {
        return montantAccorde;
    }

    public void setMontantAccorde(BigDecimal montantAccorde) {
        this.montantAccorde = montantAccorde;
    }

    public Integer getScoreEtude() {
        return scoreEtude;
    }

    public void setScoreEtude(Integer scoreEtude) {
        this.scoreEtude = scoreEtude;
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

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public LocalDate getDateDepot() {
        return dateDepot;
    }

    public void setDateDepot(LocalDate dateDepot) {
        this.dateDepot = dateDepot;
    }
}
