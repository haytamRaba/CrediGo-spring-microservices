package com.creditgo.credit.client;

import com.creditgo.credit.dto.ClientResponse;
import com.creditgo.credit.dto.CollateralResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "CLIENTS")
public interface ClientsFeignClient {

    @GetMapping("/api/clients/{id}")
    ClientResponse findClientById(@PathVariable("id") Long id);

    @GetMapping("/api/clients/collaterals/client/{clientId}")
    List<CollateralResponse> findCollateralsByClientId(@PathVariable("clientId") Long clientId);

}
