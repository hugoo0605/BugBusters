package org.bugbusters.service;

import org.bugbusters.entity.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Servicio para enviar notificaciones en tiempo real
 * cuando hay cambios en pedidos de una mesa específica.
 */
@Service
public class PedidoNotificacionService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Envía una notificación a todos los suscriptores de una mesa
     * sobre un cambio en un pedido.
     *
     * @param mesaUUID id único de la mesa
     * @param pedido   pedido que ha cambiado
     */
    public void notificarCambioEnMesa(UUID mesaUUID, Pedido pedido) {
        messagingTemplate.convertAndSend("/topic/mesa/" + mesaUUID, pedido);
    }
}
