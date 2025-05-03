package org.bugbusters.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ItemPedidoDTO {

    // Getters y setters
    private int cantidad;
    private double precioUnitario;
    private String estado;
    private String notas;
    private Long pedidoId;
    private Long productoId;

    // Constructor
    public ItemPedidoDTO(int cantidad, double precioUnitario, String estado, String notas, Long pedidoId, Long productoId) {
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.estado = estado;
        this.notas = notas;
        this.pedidoId = pedidoId;
        this.productoId = productoId;
    }

}
