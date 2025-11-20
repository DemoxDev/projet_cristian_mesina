package org.formation.repository;

import org.formation.entity.Client;
import org.formation.entity.Coffee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
