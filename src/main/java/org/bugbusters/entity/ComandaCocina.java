package org.bugbusters.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "comandas_cocina")
@Data
public class ComandaCocina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_pedido_id")
    private ItemPedido itemPedido;

    private String prioridad; // NORMAL o URGENTE

    @Column(name = "hora_entrega_estimada")
    private LocalDateTime horaEntregaEstimada;
}

