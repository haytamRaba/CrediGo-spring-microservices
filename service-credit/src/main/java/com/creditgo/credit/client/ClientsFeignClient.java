package com.creditgo.credit.client;

import com.creditgo.credit.dto.ClientResponse;
import com.creditgo.credit.dto.CollateralResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "CLIENTS", fallback = ClientsFeignClient.Fallback.class)
public interface ClientsFeignClient {

    @GetMapping("/api/clients/{id}")
    ClientResponse findClientById(@PathVariable("id") Long id);

    @GetMapping("/api/clients/email/{email}")
    ClientResponse findClientByEmail(@PathVariable("email") String email);

    @GetMapping("/api/clients/collaterals/client/{clientId}")
    List<CollateralResponse> findCollateralsByClientId(@PathVariable("clientId") Long clientId);

    class Fallback implements ClientsFeignClient {
        @Override
        public ClientResponse findClientById(Long id) {
            return null;
        }

        @Override
        public ClientResponse findClientByEmail(String email) {
            return null;
        }

        @Override
        public List<CollateralResponse> findCollateralsByClientId(Long clientId) {
            return List.of();
        }
    }
}
