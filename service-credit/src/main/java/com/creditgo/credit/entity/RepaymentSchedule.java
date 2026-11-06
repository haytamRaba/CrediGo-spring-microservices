package com.creditgo.credit.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "repayment_schedules")
public class RepaymentSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La demande de credit est obligatoire")
    @Column(nullable = false)
    private Long loanApplicationId;

    @NotNull(message = "Le numéro d'échéance est obligatoire")
    @Column(nullable = false)
    private Integer numeroEcheance;

    @NotNull(message = "La date d'échéance est obligatoire")
    @Column(nullable = false)
    private LocalDate dateEcheance;

    @NotNull(message = "Le montant de capital est obligatoire")
    @DecimalMin(value = "0.0", message = "Le montant de capital doit être positif ou nul")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal montantCapital;

    @NotNull(message = "Le montant d'intérêts est obligatoire")
    @DecimalMin(value = "0.0", message = "Le montant d'intérêts doit être positif ou nul")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal montantInterets;

    @NotNull(message = "Le statut est obligatoire")
    @Column(nullable = false)
    private String statut;

    public RepaymentSchedule() {
    }

    public RepaymentSchedule(Long loanApplicationId, Integer numeroEcheance,
                             LocalDate dateEcheance, BigDecimal montantCapital,
                             BigDecimal montantInterets, String statut) {
        this.loanApplicationId = loanApplicationId;
        this.numeroEcheance = numeroEcheance;
        this.dateEcheance = dateEcheance;
        this.montantCapital = montantCapital;
        this.montantInterets = montantInterets;
        this.statut = statut;
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

    public Integer getNumeroEcheance() {
        return numeroEcheance;
    }

    public void setNumeroEcheance(Integer numeroEcheance) {
        this.numeroEcheance = numeroEcheance;
    }

    public LocalDate getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(LocalDate dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public BigDecimal getMontantCapital() {
        return montantCapital;
    }

    public void setMontantCapital(BigDecimal montantCapital) {
        this.montantCapital = montantCapital;
    }

    public BigDecimal getMontantInterets() {
        return montantInterets;
    }

    public void setMontantInterets(BigDecimal montantInterets) {
        this.montantInterets = montantInterets;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public enum Statut {
        A_PAYER, PAYE, EN_RETARD
    }
}
