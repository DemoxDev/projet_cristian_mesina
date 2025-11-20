package org.formation.service;

import org.formation.entity.Client;

public interface PatrimoineService {
    boolean isClientEligiblePatrimoine(Client client);
    String analyzePatrimoine(Client client);
}
