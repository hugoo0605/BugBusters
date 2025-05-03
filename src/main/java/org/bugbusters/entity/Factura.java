package org.bugbusters.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "facturas")
@Data
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @Column(name = "monto_total", nullable = false)
    private Double total;

    private LocalDateTime fecha;

    private String estado; // PAGADA, PENDIENTE, ANULADA
}
