package com.creditgo.credit.controller;

import com.creditgo.credit.dto.CreateLoanApplicationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.creditgo.credit.dto.LoanApplicationResponse;
import com.creditgo.credit.dto.RepaymentScheduleResponse;
import com.creditgo.credit.dto.TransitionRequest;
import com.creditgo.credit.service.CreditService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Loan applications", description = "Loan application lifecycle: create, study, block, withdraw")
@RestController
@RequestMapping("/api/loan-applications")
public class CreditController {

    private final CreditService creditService;

    public CreditController(CreditService creditService) {
        this.creditService = creditService;
    }

    @Operation(summary = "Create a loan application", description = "Transitions DEPOSE -> EN_ETUDE (validation of request body)")
    @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(implementation = LoanApplicationResponse.class)))
    @ResponseStatus(HttpStatus.CREATED)
    public LoanApplicationResponse createLoanApplication(@Valid @RequestBody CreateLoanApplicationRequest request) {
        return creditService.createLoanApplication(request);
    }

    @Operation(summary = "List all loan applications")
    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = LoanApplicationResponse.class)))
    @GetMapping
    public List<LoanApplicationResponse> getAllLoanApplications() {
        return creditService.getAllLoanApplications();
    }

    @Operation(summary = "Get a loan application by id")
    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = LoanApplicationResponse.class)))
    @ApiResponse(responseCode = "404", description = "Not found")
    @GetMapping("/{id}")
    public LoanApplicationResponse getLoanApplicationById(@Parameter(description = "Loan application id") @PathVariable Long id) {
        return creditService.getLoanApplicationById(id);
    }

    @Operation(summary = "Study a loan application", description = "Transitions EN_ETUDE -> ACCORDE or REFUSE (rejects DEPOSE / DEBLOQUE)")
    @ApiResponse(responseCode = "200", description = "Updated", content = @Content(schema = @Schema(implementation = LoanApplicationResponse.class)))
    @ApiResponse(responseCode = "409", description = "Invalid transition")
    @PutMapping("/{id}/etudier")
    public LoanApplicationResponse etudier(@Parameter(description = "Loan application id") @PathVariable Long id, @RequestBody @Valid TransitionRequest request) {
        return creditService.etudier(id, request);
    }

    @Operation(summary = "Block a loan application", description = "Transitions EN_ETUDE -> DEBLOQUE (rejects other statuses)")
    @ApiResponse(responseCode = "200", description = "Updated", content = @Content(schema = @Schema(implementation = LoanApplicationResponse.class)))
    @ApiResponse(responseCode = "409", description = "Invalid transition")
    @PutMapping("/{id}/bloquer")
    public LoanApplicationResponse bloquer(@Parameter(description = "Loan application id") @PathVariable Long id) {
        return creditService.bloquer(id);
    }

    @Operation(summary = "Unblock a loan application", description = "Transitions DEBLOQUE -> EN_ETUDE (unblocks the schedule)")
    @ApiResponse(responseCode = "200", description = "Updated", content = @Content(schema = @Schema(implementation = LoanApplicationResponse.class)))
    @ApiResponse(responseCode = "409", description = "Invalid transition")
    @PutMapping("/{id}/retirer")
    public LoanApplicationResponse retirer(@Parameter(description = "Loan application id") @PathVariable Long id) {
        return creditService.retirer(id);
    }

    @Operation(summary = "List loan applications by status")
    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = LoanApplicationResponse.class)))
    @GetMapping("/statut/{statut}")
    public List<LoanApplicationResponse> getByStatut(@Parameter(description = "Status filter (DEPOSE | EN_ETUDE | ACCORDE | DEBLOQUE | REFUSE)") @PathVariable String statut) {
        return creditService.getByStatut(statut);
    }

    @Operation(summary = "Generate the repayment schedule", description = "Generates a constant-annuity schedule, only when DEBLOQUE; refuses if already generated")
    @ApiResponse(responseCode = "200", description = "Schedule generated", content = @Content(schema = @Schema(implementation = RepaymentScheduleResponse.class)))
    @ApiResponse(responseCode = "409", description = "Already generated")
    @PostMapping("/{id}/remarque")
    public RepaymentScheduleResponse genererRemarque(@Parameter(description = "Loan application id") @PathVariable Long id) {
        return creditService.genererRemarque(id);
    }
}
