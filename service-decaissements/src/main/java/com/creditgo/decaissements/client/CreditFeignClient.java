package com.creditgo.decaissements.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(name = "CREDIT")
public interface CreditFeignClient {

    @PostMapping("/api/loan-applications/{id}/disbursement-validation")
    void validateDisbursement(@PathVariable("id") Long id, @RequestParam("montant") BigDecimal montant);

    @PostMapping("/api/loan-applications/{id}/activate-schedule")
    void activateSchedule(@PathVariable("id") Long id);
}
