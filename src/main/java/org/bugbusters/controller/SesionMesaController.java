package org.bugbusters.controller;

import org.bugbusters.entity.Mesa;
import org.bugbusters.entity.SesionMesa;
import org.bugbusters.repository.MesaRepository;
import org.bugbusters.repository.SesionMesaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Controlador REST para gestionar sesiones de mesas.
 * Permite abrir, cerrar y consultar sesiones activas o pasadas.
 */
@RestController
@RequestMapping("/api/sesiones")
public class SesionMesaController {

    private final SesionMesaRepository sesionMesaRepository;
    private final MesaRepository mesaRepository;

    /**
     * Constructor que inyecta los repositorios necesarios.
     *
     * @param sesionMesaRepository repositorio de sesiones
     * @param mesaRepository       repositorio de mesas
     */
    public SesionMesaController(SesionMesaRepository sesionMesaRepository, MesaRepository mesaRepository) {
        this.sesionMesaRepository = sesionMesaRepository;
        this.mesaRepository = mesaRepository;
    }

    /**
     * Crea una nueva sesión para una mesa.
     *
     * @param mesaId ID de la mesa
     * @return la nueva sesión creada
     */
    @PostMapping
    public SesionMesa crearSesion(@RequestParam Long mesaId) {
        Optional<Mesa> mesaOpt = mesaRepository.findById(mesaId);
        if (mesaOpt.isEmpty()) {
            throw new RuntimeException("Mesa no encontrada");
        }

        SesionMesa sesion = new SesionMesa();
        sesion.setMesa(mesaOpt.get());
        sesion.setFechaApertura(LocalDateTime.now());
        sesion.setTokenAcceso(UUID.randomUUID().toString());

        return sesionMesaRepository.save(sesion);
    }

    /**
     * Cierra una sesión existente.
     *
     * @param id ID (UUID) de la sesión
     * @return la sesión cerrada
     */
    @PutMapping("/{id}/cerrar")
    public SesionMesa cerrarSesion(@PathVariable UUID id) {
        SesionMesa sesion = sesionMesaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
        sesion.setFechaCierre(LocalDateTime.now());
        return sesionMesaRepository.save(sesion);
    }

    /**
     * Obtiene todas las sesiones que están activas (sin fecha de cierre).
     *
     * @return lista de sesiones activas
     */
    @GetMapping("/activas")
    public List<SesionMesa> obtenerSesionesActivas() {
        return sesionMesaRepository.findByFechaCierreIsNull();
    }

    /**
     * Busca una sesión por su token de acceso.
     *
     * @param token token generado al abrir la sesión
     * @return la sesión encontrada
     */
    @GetMapping("/token/{token}")
    public SesionMesa obtenerSesionPorToken(@PathVariable String token) {
        return sesionMesaRepository.findByTokenAcceso(token)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada por token"));
    }

    /**
     * Devuelve todas las sesiones (activas o cerradas) de una mesa concreta.
     *
     * @param mesaId ID de la mesa
     * @return lista de sesiones asociadas a esa mesa
     */
    @GetMapping("/mesa/{mesaId}")
    public List<SesionMesa> obtenerSesionesPorMesa(@PathVariable Long mesaId) {
        return sesionMesaRepository.findByMesaId(mesaId);
    }

    /**
     * Devuelve la sesión activa de una mesa (la que aún no se ha cerrado).
     *
     * @param mesaId ID de la mesa
     * @return la sesión activa si existe
     */
    @GetMapping("/mesa/{mesaId}/activa")
    public SesionMesa obtenerSesionActiva(@PathVariable Long mesaId) {
        return sesionMesaRepository
                .findByMesaIdAndFechaCierreIsNull(mesaId)
                .orElseThrow(() -> new RuntimeException("No hay sesión activa para la mesa " + mesaId));
    }

    /**
     * Devuelve el ID de la mesa asociada a una sesión concreta.
     *
     * @param id UUID de la sesión
     * @return el ID de la mesa asociada
     */
    @GetMapping("/{id}/mesa-id")
    public ResponseEntity<Long> obtenerIdMesaPorSesionId(@PathVariable UUID id) {
        return sesionMesaRepository.findById(id)
                .map(sesion -> ResponseEntity.ok(sesion.getMesa().getId()))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Devuelve el número de mesa a partir del UUID de la sesión.
     *
     * @param uuid UUID de la sesión
     * @return número de mesa, si se encuentra
     */
    @GetMapping("{uuid}/numero-mesa")
    public ResponseEntity<Integer> obtenerNumeroMesa(@PathVariable UUID uuid) {
        Optional<SesionMesa> sesionOpt = sesionMesaRepository.findById(uuid);
        if (sesionOpt.isPresent()) {
            Mesa mesa = sesionOpt.get().getMesa();
            if (mesa != null) {
                return ResponseEntity.ok(mesa.getNumero());
            }
        }
        return ResponseEntity.notFound().build();
    }
}
