package org.bugbusters.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Tabla intermedia que une Factura <–> Pedido.
 */
@Entity
@Table(name = "factura_pedido")
@Data
public class FacturaPedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Muchas filas FacturaPedido pueden apuntar a la misma Factura
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;

    // Cada fila FacturaPedido apunta a un Pedido en concreto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;
}
