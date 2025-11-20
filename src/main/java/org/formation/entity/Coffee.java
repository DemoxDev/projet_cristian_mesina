package org.formation.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Coffee {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    public Coffee(String name) {this.name = name;}

}
