package org.formation.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.formation.entity.Client;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_compte")
@Data
public abstract class Compte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numeroCompte;
    private double solde;
    private LocalDate dateOuverture = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "client_id") // Foreign key in the Account table
    private Client client;
}

