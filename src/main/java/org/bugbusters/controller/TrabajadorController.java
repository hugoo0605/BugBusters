package org.bugbusters.controller;

import org.bugbusters.entity.Trabajador;
import org.bugbusters.repository.TrabajadorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/registro")
    public ResponseEntity<String> registrarTrabajador(@RequestBody Trabajador trabajador) {
        if (trabajadorRepository.existsByEmail(trabajador.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email ya registrado.");
        }
        trabajadorRepository.save(trabajador);
        return ResponseEntity.ok("Usuario registrado con éxito");
    }
}
