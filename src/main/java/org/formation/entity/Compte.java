package org.formation.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonIdentityReference;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_compte")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "numeroCompte")
public abstract class Compte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long numeroCompte;
    private double solde;
    private LocalDate dateOuverture = LocalDate.now();

    @ManyToOne
    @JoinColumn(name = "client_id") // Foreign key in the Account table
    @JsonIdentityReference(alwaysAsId = true)
    private Client client;
}
