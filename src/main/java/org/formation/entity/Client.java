package org.formation.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String adresse;

    // One client has a list of accounts
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<Compte> comptes = new ArrayList<>();

    @ManyToOne
    private Conseiller conseiller;
}

