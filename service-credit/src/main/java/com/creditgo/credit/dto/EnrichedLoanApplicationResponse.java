package com.creditgo.credit.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class EnrichedLoanApplicationResponse {

    private Long id;
    private Long clientId;
    private ClientResponse client;
    private BigDecimal montantDemande;
    private BigDecimal montantAccorde;
    private Integer scoreEtude;
    private Integer dureeMois;
    private BigDecimal tauxPropose;
    private String motif;
    private String statut;
    private LocalDate dateDepot;
    private List<CollateralResponse> garanties;
    private RepaymentScheduleResponse echeancier;
    private DisbursementResponse deconcilement;

    public EnrichedLoanApplicationResponse() {
    }

    public EnrichedLoanApplicationResponse(Long id, Long clientId, ClientResponse client,
                                           BigDecimal montantDemande, BigDecimal montantAccorde,
                                           Integer scoreEtude, Integer dureeMois,
                                           BigDecimal tauxPropose, String motif, String statut,
                                           LocalDate dateDepot, List<CollateralResponse> garanties,
                                           RepaymentScheduleResponse echeancier,
                                           DisbursementResponse deconcilement) {
        this.id = id;
        this.clientId = clientId;
        this.client = client;
        this.montantDemande = montantDemande;
        this.montantAccorde = montantAccorde;
        this.scoreEtude = scoreEtude;
        this.dureeMois = dureeMois;
        this.tauxPropose = tauxPropose;
        this.motif = motif;
        this.statut = statut;
        this.dateDepot = dateDepot;
        this.garanties = garanties;
        this.echeancier = echeancier;
        this.deconcilement = deconcilement;
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

    public ClientResponse getClient() {
        return client;
    }

    public void setClient(ClientResponse client) {
        this.client = client;
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

    public List<CollateralResponse> getGaranties() {
        return garanties;
    }

    public void setGaranties(List<CollateralResponse> garanties) {
        this.garanties = garanties;
    }

    public RepaymentScheduleResponse getEcheancier() {
        return echeancier;
    }

    public void setEcheancier(RepaymentScheduleResponse echeancier) {
        this.echeancier = echeancier;
    }

    public DisbursementResponse getDeconcilement() {
        return deconcilement;
    }

    public void setDeconcilement(DisbursementResponse deconcilement) {
        this.deconcilement = deconcilement;
    }
}
