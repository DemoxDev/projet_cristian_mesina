package org.formation.service;

import org.formation.entity.Compte;

import java.util.List;

public interface AuditService {
    List<Compte> findRiskyAccounts();
}
