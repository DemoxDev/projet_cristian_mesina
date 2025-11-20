package org.formation.service;

public interface VirementService {
    void effectuerVirement(Long sourceId, Long destId, double montant);
}