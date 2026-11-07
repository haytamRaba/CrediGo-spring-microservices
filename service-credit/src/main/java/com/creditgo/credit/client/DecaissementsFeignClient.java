package com.creditgo.credit.client;

import com.creditgo.credit.dto.DisbursementResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "DECAISSEMENTS")
public interface DecaissementsFeignClient {

    @GetMapping("/api/disbursements/loan-application/{loanApplicationId}")
    List<DisbursementResponse> getDisbursementsByLoanApplicationId(@PathVariable("loanApplicationId") Long loanApplicationId);
}
