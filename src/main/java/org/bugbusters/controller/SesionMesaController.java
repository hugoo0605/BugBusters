package org.bugbusters.controller;

import org.bugbusters.entity.Mesa;
import org.bugbusters.entity.SesionMesa;
import org.bugbusters.repository.MesaRepository;
import org.bugbusters.repository.SesionMesaRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/sesiones")
public class SesionMesaController {

    private final SesionMesaRepository sesionMesaRepository;
    private final MesaRepository mesaRepository;

    public SesionMesaController(SesionMesaRepository sesionMesaRepository, MesaRepository mesaRepository) {
        this.sesionMesaRepository = sesionMesaRepository;
        this.mesaRepository = mesaRepository;
    }

    // Crear una nueva sesión para una mesa
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

    // Cerrar una sesión
    @PutMapping("/{id}/cerrar")
    public SesionMesa cerrarSesion(@PathVariable UUID id) {
        SesionMesa sesion = sesionMesaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada"));
        sesion.setFechaCierre(LocalDateTime.now());
        return sesionMesaRepository.save(sesion);
    }

    // Obtener sesiones activas (sin fecha de cierre)
    @GetMapping("/activas")
    public List<SesionMesa> obtenerSesionesActivas() {
        return sesionMesaRepository.findByFechaCierreIsNull();
    }

    // Obtener sesión por token de acceso
    @GetMapping("/token/{token}")
    public SesionMesa obtenerSesionPorToken(@PathVariable String token) {
        return sesionMesaRepository.findByTokenAcceso(token)
                .orElseThrow(() -> new RuntimeException("Sesión no encontrada por token"));
    }

    // (Opcional) Obtener sesiones por mesa
    @GetMapping("/mesa/{mesaId}")
    public List<SesionMesa> obtenerSesionesPorMesa(@PathVariable Long mesaId) {
        return sesionMesaRepository.findByMesaId(mesaId);
    }
}
