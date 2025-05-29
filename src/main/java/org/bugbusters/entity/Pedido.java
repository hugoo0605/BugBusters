package org.bugbusters.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String estado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    private String notas;
    private double total;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trabajador_id")
    @JsonIgnore
    private Trabajador trabajador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sesion_id", nullable = false)
    @JsonIgnore
    @JsonBackReference
    private SesionMesa sesionMesa;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> items;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FacturaPedido> facturasPedidos;
}