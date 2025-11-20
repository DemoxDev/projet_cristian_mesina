package org.formation.service;

import lombok.RequiredArgsConstructor;
import org.formation.entity.Client;
import org.formation.entity.Compte;
import org.formation.entity.CompteCourant;
import org.formation.entity.CompteEpargne;
import org.formation.repository.ClientRepository;
import org.formation.repository.CompteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CompteServiceImpl implements CompteService {

    private final CompteRepository compteRepository;
    private final ClientRepository clientRepository;

    @Override
    public Compte createAccount(Long clientId, String type) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client introuvable"));

        Compte compte;
        if ("COURANT".equalsIgnoreCase(type)) {
            compte = new CompteCourant();
        } else if ("EPARGNE".equalsIgnoreCase(type)) {
            compte = new CompteEpargne();
        } else {
            throw new IllegalArgumentException("Type de compte invalide");
        }

        compte.setClient(client);
        compte.setSolde(0.0);
        client.getComptes().add(compte);

        return compteRepository.save(compte);
    }
}

