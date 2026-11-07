package com.creditgo.clients.controller;

import com.creditgo.clients.entity.Client;
import com.creditgo.clients.entity.Collateral;
import com.creditgo.clients.service.ClientsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Clients", description = "Clients et garanties")
@RestController
@RequestMapping("/api/clients")
public class ClientsController {

    private final ClientsService clientsService;

    public ClientsController(ClientsService clientsService) {
        this.clientsService = clientsService;
    }

    @Operation(summary = "Créer un client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Créé", content = @Content(schema = @Schema(implementation = Client.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Client creerClient(@Valid @RequestBody Client client) {
        return clientsService.creerClient(client);
    }

    @Operation(summary = "Liste tous les clients")
    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = Client.class)))
    @GetMapping
    public List<Client> getAllClients() {
        return clientsService.getAllClients();
    }

    @Operation(summary = "Récupérer un client par id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = Client.class))),
            @ApiResponse(responseCode = "404", description = "Non trouvé")
    })
    @GetMapping("/{id}")
    public Client getClientById(@Parameter(description = "Client id") @PathVariable Long id) {
        return clientsService.getClientById(id);
    }

    @Operation(summary = "Récupérer un client par email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = Client.class))),
            @ApiResponse(responseCode = "404", description = "Non trouvé")
    })
    @GetMapping("/email/{email}")
    public Client getClientByEmail(@Parameter(description = "Client email") @PathVariable String email) {
        return clientsService.getClientByEmail(email);
    }

    @Operation(summary = "Mettre à jour un client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mis à jour", content = @Content(schema = @Schema(implementation = Client.class))),
            @ApiResponse(responseCode = "404", description = "Non trouvé")
    })
    @PutMapping("/{id}")
    public Client updateClient(@Parameter(description = "Client id") @PathVariable Long id, @Valid @RequestBody Client update) {
        return clientsService.updateClient(id, update);
    }

    @Operation(summary = "Supprimer un client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Supprimé"),
            @ApiResponse(responseCode = "404", description = "Non trouvé")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimerClient(@Parameter(description = "Client id") @PathVariable Long id) {
        clientsService.supprimerClient(id);
    }

    @Operation(summary = "Créer une garantie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Créé", content = @Content(schema = @Schema(implementation = Collateral.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{clientId}/collaterals")
    public Collateral creerCollateral(@Parameter(description = "Client id") @PathVariable Long clientId, @Valid @RequestBody Collateral collateral) {
        collateral.setClientId(clientId);
        return clientsService.creerCollateral(collateral);
    }

    @Operation(summary = "Liste toutes les garanties")
    @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = Collateral.class)))
    @GetMapping("/collaterals")
    public List<Collateral> getAllCollaterals() {
        return clientsService.getAllCollaterals();
    }

    @Operation(summary = "Récupérer une garantie par id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = Collateral.class))),
            @ApiResponse(responseCode = "404", description = "Non trouvé")
    })
    @GetMapping("/collaterals/{id}")
    public Collateral getCollateralById(@Parameter(description = "Garantie id") @PathVariable Long id) {
        return clientsService.getCollateralById(id);
    }

    @Operation(summary = "Récupérer les garanties d'un client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(implementation = Collateral.class)))
    })
    @GetMapping("/collaterals/client/{clientId}")
    public List<Collateral> getCollateralsByClientId(@Parameter(description = "Client id") @PathVariable Long clientId) {
        return clientsService.getCollateralsByClientId(clientId);
    }

    @Operation(summary = "Mettre à jour une garantie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Mis à jour", content = @Content(schema = @Schema(implementation = Collateral.class))),
            @ApiResponse(responseCode = "404", description = "Non trouvé")
    })
    @PutMapping("/collaterals/{id}")
    public Collateral updateCollateral(@Parameter(description = "Garantie id") @PathVariable Long id, @Valid @RequestBody Collateral update) {
        return clientsService.updateCollateral(id, update);
    }

    @Operation(summary = "Supprimer une garantie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Supprimé"),
            @ApiResponse(responseCode = "404", description = "Non trouvé")
    })
    @DeleteMapping("/collaterals/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimerCollateral(@Parameter(description = "Garantie id") @PathVariable Long id) {
        clientsService.supprimerCollateral(id);
    }
}
