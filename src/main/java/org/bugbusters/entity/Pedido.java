package org.bugbusters.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
@Data
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sesion_id")
    private SesionMesa sesionMesa;

    @ManyToOne
    @JoinColumn(name = "trabajador_id")
    private Trabajador trabajador; // puede ser null (pedido autogestionado)

    private String estado; // PENDIENTE, PREPARACION, LISTO, etc.

    private Double total;

    private String notas;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
}

