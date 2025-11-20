package org.formation.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@DiscriminatorValue("EPARGNE")
@Data
public class CompteEpargne extends Compte {
    private double tauxRemuneration = 0.03;
}