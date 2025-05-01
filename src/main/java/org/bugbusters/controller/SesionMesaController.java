package org.bugbusters.controller;

import org.bugbusters.entity.Mesa;
import org.bugbusters.entity.SesionMesa;
import org.bugbusters.service.SesionMesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sesiones")
public class SesionMesaController {

    @Autowired
    private SesionMesaService sesionMesaService; // Servicio que maneja la lógica de negocio

    // Endpoint para crear una nueva sesión de mesa
    @PostMapping("/crear")
    public ResponseEntity<SesionMesa> crearSesionMesa(@RequestBody Mesa mesa) {
        SesionMesa sesionMesa = sesionMesaService.crearSesionMesa(mesa);
        if (sesionMesa != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(sesionMesa);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Endpoint para obtener una sesión de mesa existente
    @GetMapping("/{idSesion}")
    public ResponseEntity<SesionMesa> obtenerSesionMesa(@PathVariable String idSesion) {
        SesionMesa sesionMesa = sesionMesaService.obtenerSesionMesa(idSesion);
        if (sesionMesa != null) {
            return ResponseEntity.ok(sesionMesa);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Endpoint para cerrar la sesión de mesa
    @PostMapping("/cerrar/{idSesion}")
    public ResponseEntity<Void> cerrarSesionMesa(@PathVariable String idSesion) {
        boolean cerrado = sesionMesaService.cerrarSesionMesa(idSesion);
        if (cerrado) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}

