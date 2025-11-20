package org.formation.service;

import lombok.RequiredArgsConstructor;
import org.formation.entity.Client;
import org.formation.entity.Compte;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class PatrimoineServiceImpl implements PatrimoineService {

    private static final double PATRIMOINE_THRESHOLD = 500_000d;

    @Override
    public boolean isClientEligiblePatrimoine(Client client) {
        if (client == null || CollectionUtils.isEmpty(client.getComptes())) {
            return false;
        }
        double total = client.getComptes().stream()
                .mapToDouble(Compte::getSolde)
                .sum();
        return total > PATRIMOINE_THRESHOLD;
    }

    @Override
    public String analyzePatrimoine(Client client) {
        return isClientEligiblePatrimoine(client)
                ? "Client éligible: Suggérer des investissements dans les marchés de Paris, de NY, ou de Tokyo."
                : "Client normal: Pas de gestion de patrimoine nécessaire.";
    }
}
