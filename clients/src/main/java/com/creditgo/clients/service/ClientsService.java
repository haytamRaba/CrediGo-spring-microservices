package com.creditgo.clients.service;

import com.creditgo.clients.entity.Client;
import com.creditgo.clients.entity.Collateral;
import com.creditgo.clients.exception.ClientNotFoundException;
import com.creditgo.clients.exception.CollateralNotFoundException;
import com.creditgo.clients.repository.ClientRepository;
import com.creditgo.clients.repository.CollateralRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ClientsService {

    private final ClientRepository clientRepository;
    private final CollateralRepository collateralRepository;

    public ClientsService(ClientRepository clientRepository, CollateralRepository collateralRepository) {
        this.clientRepository = clientRepository;
        this.collateralRepository = collateralRepository;
    }

    private void valider(boolean valid) {
        if (!valid) {
            throw new IllegalArgumentException("La valeur fournie n'est pas valide");
        }
    }

    public Client creerClient(Client client) {
        valider(client.getNom() != null && !client.getNom().isBlank());
        valider(client.getEmail() != null && !client.getEmail().isBlank());
        valider(client.getRevenusMensuels() != null && client.getRevenusMensuels().compareTo(BigDecimal.ZERO) >= 0);
        valider(client.getScoreInterne() != null && client.getScoreInterne() >= 0);
        valider(client.getStatut() != null);

        client = clientRepository.save(client);
        return client;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Client introuvable avec l'id : " + id));
    }

    public Client getClientByEmail(String email) {
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> new ClientNotFoundException("Client introuvable avec l'email : " + email));
    }

    public Client updateClient(Long id, Client update) {
        Client existing = getClientById(id);

        if (update.getNom() != null) {
            existing.setNom(update.getNom());
        }
        if (update.getEmail() != null) {
            existing.setEmail(update.getEmail());
        }
        if (update.getRevenusMensuels() != null) {
            existing.setRevenusMensuels(update.getRevenusMensuels());
        }
        if (update.getScoreInterne() != null) {
            existing.setScoreInterne(update.getScoreInterne());
        }
        if (update.getStatut() != null) {
            existing.setStatut(update.getStatut());
        }

        return clientRepository.save(existing);
    }

    public void supprimerClient(Long id) {
        Client existing = getClientById(id);
        clientRepository.delete(existing);
    }

    public List<Collateral> getAllCollaterals() {
        return collateralRepository.findAll();
    }

    public Collateral getCollateralById(Long id) {
        return collateralRepository.findById(id)
                .orElseThrow(() -> new CollateralNotFoundException("Garantie introuvable avec l'id : " + id));
    }

    public List<Collateral> getCollateralsByClientId(Long clientId) {
        return collateralRepository.findByClientId(clientId);
    }

    public Collateral creerCollateral(Collateral collateral) {
        valider(collateral.getClientId() != null);
        getClientById(collateral.getClientId());
        valider(collateral.getType() != null && !collateral.getType().isBlank());
        valider(collateral.getValeur() != null && collateral.getValeur().compareTo(BigDecimal.ZERO) >= 0);
        valider(collateral.getDescription() != null && !collateral.getDescription().isBlank());

        collateral = collateralRepository.save(collateral);
        return collateral;
    }

    public Collateral updateCollateral(Long id, Collateral update) {
        Collateral existing = getCollateralById(id);

        if (update.getClientId() != null) {
            existing.setClientId(update.getClientId());
        }
        if (update.getType() != null) {
            existing.setType(update.getType());
        }
        if (update.getValeur() != null) {
            existing.setValeur(update.getValeur());
        }
        if (update.getDescription() != null) {
            existing.setDescription(update.getDescription());
        }

        return collateralRepository.save(existing);
    }

    public void supprimerCollateral(Long id) {
        Collateral existing = getCollateralById(id);
        collateralRepository.delete(existing);
    }
}
