package org.formation.service;

import org.formation.entity.Compte;

public interface CompteService {
    Compte createAccount(Long clientId, String type);
}

