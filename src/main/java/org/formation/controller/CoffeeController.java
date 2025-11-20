package org.formation.controller;

import lombok.RequiredArgsConstructor;
import org.formation.entity.Coffee;
import org.formation.service.CoffeeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CoffeeController {
    private final CoffeeService service;

    @GetMapping("/coffees")
    List<Coffee> getCoffees() {
        return service.getCoffees();
    }
}
