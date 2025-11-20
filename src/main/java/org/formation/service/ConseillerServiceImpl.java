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
public class ConseillerServiceImpl implements ConseillerService {

    private final ConseillerRepository conseillerRepository;
    private final ClientRepository clientRepository;

    @Override
    public Conseiller createConseiller(Conseiller conseiller) {
        if (conseiller.getNom() == null || conseiller.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom du conseiller est obligatoire");
        }
        return conseillerRepository.save(conseiller);
    }

    @Override
    public Conseiller assignClientToConseiller(Long conseillerId, Long clientId) {
        Conseiller conseiller = conseillerRepository.findById(conseillerId)
                .orElseThrow(() -> new IllegalArgumentException("Conseiller introuvable"));

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client introuvable"));

        // Check the 10-client quota
        if (conseiller.getClients().size() >= 10) {
            throw new IllegalStateException("Quota atteint : Ce conseiller a déjà 10 clients.");
        }

        // Remove client from old conseiller if exists
        if (client.getConseiller() != null) {
            Conseiller oldConseiller = client.getConseiller();
            oldConseiller.getClients().remove(client);
            conseillerRepository.save(oldConseiller);
        }

        // Assign to new conseiller
        client.setConseiller(conseiller);
        conseiller.getClients().add(client);
        clientRepository.save(client);

        return conseillerRepository.save(conseiller);
    }
}
