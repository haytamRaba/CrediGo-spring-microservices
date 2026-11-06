package com.creditgo.credit.dto;

import com.creditgo.credit.entity.RepaymentSchedule;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RepaymentScheduleResponse {

    private Long id;
    private Long loanApplicationId;
    private Integer numeroEcheance;
    private LocalDate dateEcheance;
    private BigDecimal montantCapital;
    private BigDecimal montantInterets;
    private String statut;

    public RepaymentScheduleResponse() {
    }

    public RepaymentScheduleResponse(RepaymentSchedule entity) {
        this.id = entity.getId();
        this.loanApplicationId = entity.getLoanApplicationId();
        this.numeroEcheance = entity.getNumeroEcheance();
        this.dateEcheance = entity.getDateEcheance();
        this.montantCapital = entity.getMontantCapital();
        this.montantInterets = entity.getMontantInterets();
        this.statut = entity.getStatut();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
