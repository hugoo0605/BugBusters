package org.bugbusters.dto;

import lombok.Getter;
import lombok.Setter;
import org.bugbusters.entity.Pedido;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class PedidoResponseDTO {
    private Long id;
    private String estado;
    private LocalDateTime fechaCreacion;
    private String notas;
    private double total;
    private Long trabajadorId;
    private UUID sesionId;

    public PedidoResponseDTO(Pedido pedido) {
        this.id = pedido.getId();
        this.estado = pedido.getEstado();
        this.fechaCreacion = pedido.getFechaCreacion();
        this.notas = pedido.getNotas();
        this.total = pedido.getTotal();
        this.trabajadorId = pedido.getTrabajador() != null ? pedido.getTrabajador().getId() : null;
        this.sesionId = pedido.getSesionMesa() != null ? pedido.getSesionMesa().getId() : null;
    }

    // Getters y setters

}