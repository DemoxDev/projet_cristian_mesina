package org.formation.service;

import lombok.RequiredArgsConstructor;
import org.formation.entity.Compte;
import org.formation.entity.CompteCourant;
import org.formation.repository.CompteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuditServiceImpl implements AuditService {
    private static final double MAX_ALLOWED_OVERDRAFT = -5000d;

    private final CompteRepository compteRepository;

    @Override
    public List<Compte> findRiskyAccounts() {
        return compteRepository.findAll().stream()
                .filter(CompteCourant.class::isInstance)
                .filter(c -> c.getSolde() < MAX_ALLOWED_OVERDRAFT)
                .toList();
    }
}
