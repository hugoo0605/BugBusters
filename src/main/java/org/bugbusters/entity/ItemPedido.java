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
    private int cantidad;

    @Column(name = "precio_unitario")
    private double precioUnitario;

    private String estado;
    private String notas;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id")
    private Producto producto;
}
