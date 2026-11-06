package com.creditgo.decaissements.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "disbursements")
public class Disbursement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La demande de credit est obligatoire")
    @Column(nullable = false)
    private Long loanApplicationId;

    @NotNull(message = "Le compte treasury est obligatoire")
    @Column(nullable = false)
    private Long treasuryAccountId;

    @NotNull(message = "Le montant est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant doit être strictement positif")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal montant;

    @NotNull(message = "La date est obligatoire")
    @Column(nullable = false)
    private LocalDate date;

    @NotNull(message = "Le statut est obligatoire")
    @Column(nullable = false)
    private String statut;

    public Disbursement() {
    }

    public Disbursement(Long loanApplicationId, Long treasuryAccountId, BigDecimal montant, LocalDate date) {
        this.loanApplicationId = loanApplicationId;
        this.treasuryAccountId = treasuryAccountId;
        this.montant = montant;
        this.date = date;
        this.statut = Statut.EN_ATTENTE.name();
    }

    public Long getId() {
        return id;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public enum Statut {
        EN_ATTENTE, EFFECTUE, REJETE
    }

    public boolean estEffectue() {
        return Statut.EFFECTUE.name().equals(statut);
    }

    public boolean estRejete() {
        return Statut.REJETE.name().equals(statut);
    }
}
