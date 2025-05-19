package org.bugbusters.dto;

import lombok.Getter;
import lombok.Setter;
import org.bugbusters.entity.ItemPedido;

@Setter
@Getter
public class ItemPedidoDTO {
    private int cantidad;
    private double precioUnitario;
    private String estado;
    private String notas;
    private Long pedidoId;
    private Long productoId;
    private String nombreProducto;

    public ItemPedidoDTO(int cantidad, double precioUnitario, String estado, String notas, Long pedidoId, Long productoId) {
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.estado = estado;
        this.notas = notas;
        this.pedidoId = pedidoId;
        this.productoId = productoId;
    }

    public ItemPedidoDTO(ItemPedido itemPedido) {
        this.cantidad = itemPedido.getCantidad();
        this.precioUnitario = itemPedido.getPrecioUnitario();
        this.estado = itemPedido.getEstado();
        this.notas = itemPedido.getNotas();
        this.pedidoId = itemPedido.getPedido().getId();
        this.productoId = itemPedido.getProducto().getId();
        this.nombreProducto = itemPedido.getProducto() != null ? itemPedido.getProducto().getNombre() : "Producto desconocido";
    }
}
