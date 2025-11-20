package org.formation.service;

import org.formation.entity.Client;

public interface ClientService {
    Client createClient(Client client, Long conseillerId);
}

