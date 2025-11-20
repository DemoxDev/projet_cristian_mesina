package org.formation.service;

import lombok.RequiredArgsConstructor;
import org.formation.entity.Compte;
import org.formation.entity.CompteCourant;
import org.formation.repository.CompteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
@Transactional
public class VirementServiceImpl implements VirementService {

    private final CompteRepository compteRepository;

    @Override
    public void effectuerVirement(Long sourceId, Long destId, double montant) {
        Assert.isTrue(montant > 0, "Le montant du virement doit être positif");
        Compte source = compteRepository.findById(sourceId)
                .orElseThrow(() -> new IllegalArgumentException("Compte source introuvable"));
        Compte dest = compteRepository.findById(destId)
                .orElseThrow(() -> new IllegalArgumentException("Compte destinataire introuvable"));

        validateSolvency(source, montant);

        source.setSolde(source.getSolde() - montant);
        dest.setSolde(dest.getSolde() + montant);

        compteRepository.save(source);
        compteRepository.save(dest);
    }

    private void validateSolvency(Compte source, double montant) {
        if (source instanceof CompteCourant courant) {
            double decouvert = courant.getDecouvertAutorise();
            if (source.getSolde() - montant < -decouvert) {
                throw new IllegalStateException("Virement impossible : découvert dépassé.");
            }
            return;
        }

        if (source.getSolde() - montant < 0) {
            throw new IllegalStateException("Virement impossible : solde insuffisant.");
        }
    }
}
