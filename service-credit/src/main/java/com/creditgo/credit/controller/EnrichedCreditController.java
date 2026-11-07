package com.creditgo.credit.controller;

import com.creditgo.credit.dto.EnrichedLoanApplicationResponse;
import com.creditgo.credit.service.CreditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Enriched loan applications", description = "Consultation d'un dossier enrichi avec client, garanties, échéancier et décaissement")
@RestController
@RequestMapping("/api/enriched/loan-applications")
public class EnrichedCreditController {

    private final CreditService creditService;

    public EnrichedCreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    @Operation(summary = "Récupérer un dossier de crédit enrichi")
    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = EnrichedLoanApplicationResponse.class)))
    @ApiResponse(responseCode = "404", description = "Non trouvé")
    @GetMapping("/{id}")
    public EnrichedLoanApplicationResponse getEnrichedLoanApplicationById(
            @Parameter(description = "Dossier de crédit id") @PathVariable Long id) {
        return creditService.getEnrichedLoanApplicationById(id);
    }
}
