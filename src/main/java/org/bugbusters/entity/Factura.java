package org.bugbusters.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "facturas")
@Data
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "monto_total", nullable = false)
    private Double total;

    private LocalDateTime fecha;

    private String estado; // POR EJEMPLO: PENDIENTE, PAGADA, ANULADA, etc.

    // Relación 1:N con FacturaPedido:
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FacturaPedido> facturasPedidos;
}
