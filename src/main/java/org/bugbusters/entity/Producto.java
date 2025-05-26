package org.bugbusters.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
    private String categoria;
    private Boolean disponible = true;
    @Column(name = "tiempo_preparacion")
    private Integer tiempoPreparacion;
    @Column(name = "imagen_url")
    private String imagenes;
}
