package org.bugbusters.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "mesas")
@Data
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numero;

    private String estado; // LIBRE, OCUPADA, etc.

    private Integer capacidad;

    private String ubicacion;
}

