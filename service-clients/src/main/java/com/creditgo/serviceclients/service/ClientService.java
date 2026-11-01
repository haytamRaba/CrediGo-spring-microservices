package com.creditgo.serviceclients.service;

import com.creditgo.serviceclients.entity.Client;
import com.creditgo.serviceclients.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Service
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client createClient(Client client) {
        if (clientRepository.existsByEmail(client.getEmail())) {
            throw new ResponseStatusException(
                    CONFLICT,
                    "Un client avec cet email existe déjà"
            );
        }

        return clientRepository.save(client);
    }

    @Transactional(readOnly = true)
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND,
                        "Client introuvable avec l'id : " + id
                ));
    }

    public Client updateClient(Long id, Client updatedClient) {
        Client existingClient = getClientById(id);

        clientRepository.findByEmail(updatedClient.getEmail())
                .filter(client -> !client.getId().equals(id))
                .ifPresent(client -> {
                    throw new ResponseStatusException(
                            CONFLICT,
                            "Cet email est déjà utilisé par un autre client"
                    );
                });

        existingClient.setNom(updatedClient.getNom());
        existingClient.setEmail(updatedClient.getEmail());
        existingClient.setRevenusMensuels(updatedClient.getRevenusMensuels());
        existingClient.setScoreInterne(updatedClient.getScoreInterne());
        existingClient.setStatut(updatedClient.getStatut());

        return clientRepository.save(existingClient);
    }

    public void deleteClient(Long id) {
        Client client = getClientById(id);
        clientRepository.delete(client);
    }
}