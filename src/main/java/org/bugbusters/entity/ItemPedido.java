package org.bugbusters.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "item_pedido")
@Data
public class ItemPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    private Integer cantidad;

    @Column(name = "precio_unitario")
    private Double precioUnitario;

    private String estado; // PENDIENTE, PREPARACION, LISTO, etc.

    private String notas;
}

