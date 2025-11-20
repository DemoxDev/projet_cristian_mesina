package org.formation.controller;

import lombok.RequiredArgsConstructor;
import org.formation.entity.*;
import org.formation.repository.ClientRepository;
import org.formation.repository.CompteRepository;
import org.formation.repository.ConseillerRepository;
import org.formation.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BankController {
    private final VirementService virementService;
    private final PatrimoineService patrimoineService;
    private final AuditService auditService;
    private final ClientRepository clientRepository;
    private final CompteRepository compteRepository;
    private final ConseillerRepository conseillerRepository;
    private final ClientService clientService;
    private final CompteService compteService;

    // 1. Transfer
    @PostMapping("/virement")
    public ResponseEntity<String> transfer(@RequestParam Long from, @RequestParam Long to, @RequestParam double amount) {
        try {
            virementService.effectuerVirement(from, to, amount);
            return ResponseEntity.ok("Transfer successful");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. Wealth Check
    @GetMapping("/patrimoine/{clientId}")
    public ResponseEntity<String> checkWealth(@PathVariable Long clientId) {
        Client c = clientRepository.findById(clientId).orElseThrow();
        return ResponseEntity.ok(patrimoineService.analyzePatrimoine(c));
    }

    // 3. Audit
    @GetMapping("/audit")
    public List<Compte> runAudit() {
        return auditService.findRiskyAccounts();
    }

    @GetMapping("test/initData")
    public String initData() {
        // Clear existing data
        compteRepository.deleteAll();
        clientRepository.deleteAll();
        conseillerRepository.deleteAll();

        // Create Conseillers
        Conseiller conseiller1 = new Conseiller();
        conseiller1.setNom("Smith");
        conseillerRepository.save(conseiller1);

        Conseiller conseiller2 = new Conseiller();
        conseiller2.setNom("Johnson");
        conseillerRepository.save(conseiller2);

        // 1. Rich Client (Patrimoine > 500k) - For wealth management testing
        Client richClient = new Client();
        richClient.setNom("Musk");
        richClient.setPrenom("Elon");
        richClient.setAdresse("123 Silicon Valley");
        richClient.setConseiller(conseiller1);
        clientRepository.save(richClient);

        CompteCourant richCC = new CompteCourant();
        richCC.setClient(richClient);
        richCC.setSolde(400000);
        richCC.setDecouvertAutorise(5000);
        compteRepository.save(richCC);

        CompteEpargne richCE = new CompteEpargne();
        richCE.setClient(richClient);
        richCE.setSolde(250000);
        richCE.setTauxRemuneration(0.03);
        compteRepository.save(richCE);

        // 2. Risky Client (Overdraft > 5000) - For audit testing
        Client riskyClient = new Client();
        riskyClient.setNom("Broke");
        riskyClient.setPrenom("Johnny");
        riskyClient.setAdresse("456 Debt Street");
        riskyClient.setConseiller(conseiller1);
        clientRepository.save(riskyClient);

        CompteCourant riskyCC = new CompteCourant();
        riskyCC.setClient(riskyClient);
        riskyCC.setSolde(-6000); // Dangerous overdraft!
        riskyCC.setDecouvertAutorise(10000);
        compteRepository.save(riskyCC);

        // 3. Normal Client with both account types - For virement testing
        Client normalClient1 = new Client();
        normalClient1.setNom("Dupont");
        normalClient1.setPrenom("Alice");
        normalClient1.setAdresse("789 Paris Avenue");
        normalClient1.setConseiller(conseiller2);
        clientRepository.save(normalClient1);

        CompteCourant normalCC1 = new CompteCourant();
        normalCC1.setClient(normalClient1);
        normalCC1.setSolde(5000);
        normalCC1.setDecouvertAutorise(1000);
        compteRepository.save(normalCC1);

        CompteEpargne normalCE1 = new CompteEpargne();
        normalCE1.setClient(normalClient1);
        normalCE1.setSolde(15000);
        normalCE1.setTauxRemuneration(0.025);
        compteRepository.save(normalCE1);

        // 4. Another Normal Client - Transfer destination
        Client normalClient2 = new Client();
        normalClient2.setNom("Martin");
        normalClient2.setPrenom("Bob");
        normalClient2.setAdresse("321 Lyon Street");
        normalClient2.setConseiller(conseiller2);
        clientRepository.save(normalClient2);

        CompteCourant normalCC2 = new CompteCourant();
        normalCC2.setClient(normalClient2);
        normalCC2.setSolde(2000);
        normalCC2.setDecouvertAutorise(500);
        compteRepository.save(normalCC2);

        CompteEpargne normalCE2 = new CompteEpargne();
        normalCE2.setClient(normalClient2);
        normalCE2.setSolde(8000);
        normalCE2.setTauxRemuneration(0.02);
        compteRepository.save(normalCE2);

        // 5. Client with minimal balance - Edge case for transfers
        Client poorClient = new Client();
        poorClient.setNom("Pauvre");
        poorClient.setPrenom("Marie");
        poorClient.setAdresse("999 Empty Wallet Rd");
        poorClient.setConseiller(conseiller1);
        clientRepository.save(poorClient);

        CompteCourant poorCC = new CompteCourant();
        poorCC.setClient(poorClient);
        poorCC.setSolde(100);
        poorCC.setDecouvertAutorise(500);
        compteRepository.save(poorCC);

        return String.format(
            "Sample data initialized successfully!%n" +
            "- %d Conseillers%n" +
            "- %d Clients%n" +
            "- %d Comptes%n%n" +
            "Test scenarios:%n" +
            "1. Wealth Check: Client '%s %s' (ID=%d) has patrimoine > 500k%n" +
            "2. Audit: Client '%s %s' has overdraft > 5000 (Account ID=%d)%n" +
            "3. Transfers: Use accounts from clients '%s' and '%s'%n" +
            "4. Edge cases: Client '%s' has minimal balance",
            conseillerRepository.count(),
            clientRepository.count(),
            compteRepository.count(),
            richClient.getPrenom(), richClient.getNom(), richClient.getId(),
            riskyClient.getPrenom(), riskyClient.getNom(), riskyCC.getNumeroCompte(),
            normalClient1.getNom(), normalClient2.getNom(),
            poorClient.getNom()
        );
    }

    @GetMapping("test/getAll")
    public List<Client> getAll() {
        // Returns all clients with their accounts for testing purposes
        return clientRepository.findAll();
    }

    @PostMapping("/conseillers/{conseillerId}/clients")
    public ResponseEntity<Client> createClient(@PathVariable Long conseillerId, @RequestBody Client client) {
        try {
            Client newClient = clientService.createClient(client, conseillerId);
            return ResponseEntity.ok(newClient);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/clients/{clientId}/comptes")
    public ResponseEntity<Compte> createAccount(@PathVariable Long clientId, @RequestParam String type) {
        try {
            Compte newAccount = compteService.createAccount(clientId, type);
            return ResponseEntity.ok(newAccount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

// give me a list of requests to make with postman to cover all endpoints and test cases, such as creating a client, creating accounts, performing transfers, checking wealth, running audits, creating conseillers, viewing account info, and so on... be extensive