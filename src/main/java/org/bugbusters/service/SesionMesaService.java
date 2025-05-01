package org.bugbusters.service;

import org.bugbusters.entity.Mesa;
import org.bugbusters.entity.SesionMesa;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class SesionMesaService {

    private Map<String, SesionMesa> sesiones = new HashMap<>();

    // Crear una nueva sesión
    public SesionMesa crearSesionMesa(Mesa mesa) {
        // Crear una nueva sesión para la mesa
        String idSesion = UUID.randomUUID().toString();
        SesionMesa nuevaSesion = new SesionMesa();
        nuevaSesion.setId(UUID.fromString(idSesion));
        nuevaSesion.setMesa(mesa);
        sesiones.put(idSesion, nuevaSesion);
        return nuevaSesion;
    }

    // Obtener una sesión de mesa
    public SesionMesa obtenerSesionMesa(String idSesion) {
        return sesiones.get(idSesion);
    }

    // Cerrar una sesión de mesa
    public boolean cerrarSesionMesa(String idSesion) {
        SesionMesa sesion = sesiones.remove(idSesion);
        return sesion != null;
    }
}

