package org.bugbusters.service;

import org.bugbusters.entity.Mesa;
import org.bugbusters.entity.SesionMesa;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Servicio temporal para gestionar sesiones de mesa en memoria (sin base de datos).
 */
@Service
public class SesionMesaService {

    private final Map<String, SesionMesa> sesiones = new HashMap<>();

    /**
     * Crea una nueva sesión para una mesa dada.
     *
     * @param mesa la mesa para la que se crea la sesión
     * @return la nueva sesión creada
     */
    public SesionMesa crearSesionMesa(Mesa mesa) {
        String idSesion = UUID.randomUUID().toString();
        SesionMesa nuevaSesion = new SesionMesa();
        nuevaSesion.setId(UUID.fromString(idSesion));
        nuevaSesion.setMesa(mesa);
        sesiones.put(idSesion, nuevaSesion);
        return nuevaSesion;
    }

    /**
     * Obtiene una sesión de mesa según su ID.
     *
     * @param idSesion el ID de la sesión
     * @return la sesión si existe, o null si no
     */
    public SesionMesa obtenerSesionMesa(String idSesion) {
        return sesiones.get(idSesion);
    }

    /**
     * Cierra (elimina) una sesión de mesa.
     *
     * @param idSesion el ID de la sesión a cerrar
     * @return true si se eliminó correctamente, false si no existía
     */
    public boolean cerrarSesionMesa(String idSesion) {
        SesionMesa sesion = sesiones.remove(idSesion);
        return sesion != null;
    }
}