package org.formation.service;

import org.formation.entity.Conseiller;

public interface ConseillerService {
    Conseiller createConseiller(Conseiller conseiller);
    Conseiller assignClientToConseiller(Long conseillerId, Long clientId);
}
