package org.bugbusters.controller;

import org.bugbusters.entity.Trabajador;
import org.bugbusters.repository.TrabajadorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar trabajadores.
 * Permite listar y registrar nuevos trabajadores.
 */
@RestController
@RequestMapping("/api/trabajadores")
public class TrabajadorController {

    private final TrabajadorRepository trabajadorRepository;

    /**
     * Constructor que inyecta el repositorio de trabajadores.
     *
     * @param trabajadorRepository repositorio de trabajadores
     */
    public TrabajadorController(TrabajadorRepository trabajadorRepository) {
        this.trabajadorRepository = trabajadorRepository;
    }

    /**
     * Devuelve la lista de todos los trabajadores registrados.
     *
     * @return lista de trabajadores
     */
    @GetMapping
    public List<Trabajador> listarTrabajadores() {
        return trabajadorRepository.findAll();
    }

    /**
     * Registra un nuevo trabajador, si el email no está ya en uso.
     *
     * @param trabajador datos del trabajador a registrar
     * @return mensaje de éxito o error si el email ya existe
     */
    @PostMapping("/registro")
    public ResponseEntity<String> registrarTrabajador(@RequestBody Trabajador trabajador) {
        if (trabajadorRepository.existsByEmail(trabajador.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email ya registrado.");
        }
        trabajadorRepository.save(trabajador);
        return ResponseEntity.ok("Usuario registrado con éxito");
    }
}