package org.bugbusters.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "mesas")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Mesa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer numero;
    private String estado;
    private Integer capacidad;
    private String ubicacion;

    @OneToMany(mappedBy = "mesa", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SesionMesa> sesiones;
}