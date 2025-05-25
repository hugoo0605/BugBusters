package org.bugbusters.controller;

import org.bugbusters.entity.Mesa;
import org.bugbusters.entity.SesionMesa;
import org.bugbusters.repository.MesaRepository;
import org.bugbusters.repository.SesionMesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/mesas")
@CrossOrigin(origins = "*")
public class MesaController {

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private SesionMesaRepository sesionMesaRepository;

    @GetMapping
    public List<Mesa> getMesas() {
        return mesaRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Mesa> crearMesaYSesion(@RequestBody Mesa mesa) {
        // 1) Guardamos la Mesa
        Mesa mesaGuardada = mesaRepository.save(mesa);

        // 2) Creamos la SesionMesa asociada
        SesionMesa sesion = new SesionMesa();
        sesion.setMesa(mesaGuardada);
        sesion.setFechaApertura(LocalDateTime.now());
        sesion.setTokenAcceso(UUID.randomUUID().toString());
        sesionMesaRepository.save(sesion);

        // 3) Devolvemos la Mesa guardada
        return ResponseEntity.ok(mesaGuardada);
    }
}
