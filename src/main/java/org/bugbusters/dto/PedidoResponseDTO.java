package org.bugbusters.dto;

import lombok.Getter;
import lombok.Setter;
import org.bugbusters.entity.Pedido;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class PedidoResponseDTO {
    private Long id;
    private String estado;
    private LocalDateTime fechaCreacion;
    private String notas;
    private double total;

    private Long trabajadorId;
    private String camarero;        // nombre del trabajador
    private UUID sesionId;
    private int mesa;               // número de la mesa

    public PedidoResponseDTO(Pedido pedido) {
        this.id = pedido.getId();
        this.estado = pedido.getEstado();
        this.fechaCreacion = pedido.getFechaCreacion();
        this.notas = pedido.getNotas();
        this.total = pedido.getTotal();

        this.trabajadorId = pedido.getTrabajador() != null ? pedido.getTrabajador().getId() : null;
        this.camarero = pedido.getTrabajador() != null ? pedido.getTrabajador().getNombre() : "Autogestionado";

        this.sesionId = pedido.getSesionMesa() != null ? pedido.getSesionMesa().getId() : null;
        this.mesa = pedido.getSesionMesa() != null && pedido.getSesionMesa().getMesa() != null
                ? pedido.getSesionMesa().getMesa().getNumero()
                : -1;
    }
}
