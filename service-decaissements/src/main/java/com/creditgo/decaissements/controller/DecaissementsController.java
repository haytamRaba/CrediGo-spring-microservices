package com.creditgo.decaissements.controller;

import com.creditgo.decaissements.dto.CreateDisbursementRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.creditgo.decaissements.dto.CreateTreasuryAccountRequest;
import com.creditgo.decaissements.dto.DisbursementResponse;
import com.creditgo.decaissements.dto.TreasuryAccountResponse;
import com.creditgo.decaissements.service.DecaissementsService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Disbursements", description = "Treasury disbursements and treasury accounts")
@RestController
@RequestMapping("/api/disbursements")
public class DecaissementsController {

    private final DecaissementsService decaissementsService;

    public DecaissementsController(DecaissementsService decaissementsService) {
        this.decaissementsService = decaissementsService;
    }

    @Operation(summary = "Create a disbursement", description = "Deducts montant from the treasury account; rejects if soldeDisponible < montant")
    @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = DisbursementResponse.class)))
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public DisbursementResponse creerVersement(@Valid @RequestBody CreateDisbursementRequest request) {
        return decaissementsService.creerVersement(request);
    }

    @Operation(summary = "List all disbursements")
    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = DisbursementResponse.class)))
    @GetMapping
    public List<DisbursementResponse> getAllDisbursements() {
        return decaissementsService.getAllDisbursements();
    }

    @Operation(summary = "Get a disbursement by id")
    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = DisbursementResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("/{id}")
    public DisbursementResponse getDisbursementById(@Parameter(description = "Disbursement id") @PathVariable Long id) {
        return decaissementsService.getDisbursementById(id);
    }

    @Operation(summary = "List disbursements by loan application")
    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = DisbursementResponse.class)))
    @GetMapping("/loan-application/{loanApplicationId}")
    public List<DisbursementResponse> getDisbursementsByLoanApplication(@Parameter(description = "Loan application id") @PathVariable Long loanApplicationId) {
        return decaissementsService.getDisbursementsByLoanApplicationId(loanApplicationId);
    }

    @Operation(summary = "List disbursements by treasury account")
    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = DisbursementResponse.class)))
    @GetMapping("/treasury-account/{treasuryAccountId}")
    public List<DisbursementResponse> getDisbursementsByTreasuryAccount(@Parameter(description = "Treasury account id") @PathVariable Long treasuryAccountId) {
        return decaissementsService.getDisbursementsByTreasuryAccountId(treasuryAccountId);
    }

    @Operation(summary = "List disbursements by status")
    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = DisbursementResponse.class)))
    @GetMapping("/statut/{statut}")
    public List<DisbursementResponse> getByStatut(@Parameter(description = "Status filter (EN_ATTENTE | EFFECTUE | REJETE)") @PathVariable String statut) {
        return decaissementsService.getDisbursementsByStatut(statut);
    }

    @Operation(summary = "List all treasury accounts")
    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = TreasuryAccountResponse.class)))
    @GetMapping("/comptes")
    public List<TreasuryAccountResponse> getAllComptes() {
        return decaissementsService.getAllComptes();
    }

    @Operation(summary = "Get a treasury account by id")
    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = TreasuryAccountResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("/comptes/{id}")
    public TreasuryAccountResponse getCompteById(@Parameter(description = "Treasury account id") @PathVariable Long id) {
        return decaissementsService.getCompteById(id);
    }

    @Operation(summary = "Create a treasury account")
    @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = TreasuryAccountResponse.class)))
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/comptes")
    public TreasuryAccountResponse creerCompte(@Valid @RequestBody CreateTreasuryAccountRequest request) {
        return decaissementsService.creerCompte(request);
    }
}
