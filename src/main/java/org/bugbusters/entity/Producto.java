package org.bugbusters.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "productos")
@Data
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String descripcion;

    private Double precio;

    private String categoria; // COMIDA, BEBIDA, POSTRE, ESPECIAL

    private Boolean disponible = true;

    @Column(name = "tiempo_preparacion")
    private Integer tiempoPreparacion;
}

