package com.creditgo.credit.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "loan_applications")
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le client est obligatoire")
    private Long clientId;

    @NotNull(message = "Le montant demandé est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant demandé doit être strictement positif")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal montantDemande;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal montantAccorde;

    @NotNull(message = "Le score d'étude est obligatoire")
    @Min(value = 0, message = "Le score d'étude ne peut pas être négatif")
    @Column(nullable = false)
    private Integer scoreEtude;

    @NotNull(message = "La durée est obligatoire")
    @Min(value = 1, message = "La durée doit être positive")
    @Column(nullable = false)
    private Integer dureeMois;

    @NotNull(message = "Le taux est obligatoire")
    @DecimalMin(value = "0.0", message = "Le taux doit être positif ou nul")
    @Column(nullable = false, precision = 5, scale = 3)
    private BigDecimal tauxPropose;

    @NotNull(message = "Le motif est obligatoire")
    @Column(nullable = false)
    private String motif;

    @NotNull(message = "Le statut est obligatoire")
    @Column(nullable = false)
    private String statut;

    @Column(nullable = false)
    private LocalDate dateDepot;

    public LoanApplication() {
    }

    public LoanApplication(Long clientId, BigDecimal montantDemande, Integer dureeMois,
                           BigDecimal tauxPropose, String motif) {
        this.clientId = clientId;
        this.montantDemande = montantDemande;
        this.dureeMois = dureeMois;
        this.tauxPropose = tauxPropose;
        this.motif = motif;
        this.scoreEtude = null;
        this.montantAccorde = null;
        this.statut = Statut.DEPOSE.name();
        this.dateDepot = LocalDate.now();
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

    public enum Statut {
        DEPOSE, EN_ETUDE, ACCORDE, DEBLOQUE, REFUSE
    }

    public boolean estDebloque() {
        return Statut.DEBLOQUE.name().equals(statut);
    }

    public boolean estAccorde() {
        return Statut.ACCORDE.name().equals(statut);
    }

    public boolean estRefuse() {
        return Statut.REFUSE.name().equals(statut);
    }

    public boolean estEnEtude() {
        return Statut.EN_ETUDE.name().equals(statut);
    }

    public boolean estDepose() {
        return Statut.DEPOSE.name().equals(statut);
    }
}
