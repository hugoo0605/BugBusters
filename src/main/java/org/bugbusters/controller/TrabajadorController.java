package org.bugbusters.controller;

import org.bugbusters.entity.Trabajador;
import org.bugbusters.repository.TrabajadorRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/trabajadores")
public class TrabajadorController {

    private final TrabajadorRepository trabajadorRepository;

    public TrabajadorController(TrabajadorRepository trabajadorRepository) {
        this.trabajadorRepository = trabajadorRepository;
    }

    @GetMapping
    public List<Trabajador> listarTrabajadores() {
        return trabajadorRepository.findAll();
    }
}
