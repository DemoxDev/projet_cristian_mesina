package org.formation.service;

import lombok.RequiredArgsConstructor;
import org.formation.entity.Client;
import org.formation.entity.Conseiller;
import org.formation.repository.ClientRepository;
import org.formation.repository.ConseillerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ConseillerRepository conseillerRepository;

    @Override
    public Client createClient(Client client, Long conseillerId) {
        Conseiller conseiller = conseillerRepository.findById(conseillerId)
                .orElseThrow(() -> new IllegalArgumentException("Conseiller introuvable"));

        if (conseiller.getClients().size() >= 10) {
            throw new IllegalStateException("Quota atteint : Ce conseiller a déjà 10 clients.");
        }

        client.setConseiller(conseiller);
        conseiller.getClients().add(client);

        return clientRepository.save(client);
    }
}

