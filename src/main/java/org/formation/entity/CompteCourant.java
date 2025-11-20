package org.formation.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@DiscriminatorValue("COURANT")
@Data
public class CompteCourant extends Compte {
    private double decouvertAutorise = 1000.0;
}