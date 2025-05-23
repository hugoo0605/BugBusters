package org.bugbusters.service;

import org.bugbusters.entity.Pedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PedidoNotificacionService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notificarCambioEnMesa(UUID mesaUUID, Pedido pedido) {
        messagingTemplate.convertAndSend("/topic/mesa/" + mesaUUID, pedido);
    }
}
