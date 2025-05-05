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

    @Getter
    @Setter
    private String nombre;
    @Getter
    @Setter
    private String descripcion;
    @Getter
    @Setter
    private Double precio;
    @Getter
    @Setter
    private String categoria; // COMIDA, BEBIDA, POSTRE, ESPECIAL
    @Getter
    @Setter
    private Boolean disponible = true;
    @Getter
    @Setter
    @Column(name = "tiempo_preparacion")
    private Integer tiempoPreparacion;
    @Getter
    @Setter
    @Column(name = "imagen_url")
    private String imagenes;
    // Getters y setters generados por Lombok (@Data)
}
