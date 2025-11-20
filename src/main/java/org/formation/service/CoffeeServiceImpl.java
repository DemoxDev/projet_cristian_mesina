package org.formation.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.formation.entity.Coffee;
import org.formation.repository.CoffeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoffeeServiceImpl implements CoffeeService {
    private final CoffeeRepository repository;

    @PostConstruct
    private void initDB() {
        repository.saveAll(List.of(new Coffee("espresso"), new Coffee("latte"), new Coffee("cappuccino"), new Coffee("americano")));
    }


    @Override
    public List<Coffee> getCoffees() {
        return repository.findAll();
    }
}
