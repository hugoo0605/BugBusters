package org.bugbusters.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pedidos")
public class Pedido {

    // Getters y setters
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    private String estado;
    @Setter
    @Getter
    private LocalDateTime fechaCreacion;
    @Setter
    @Getter
    private String notas;
    @Setter
    @Getter
    private double total;

    @Setter
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trabajador_id", nullable = false)
    private Trabajador trabajador;

    @Setter
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sesion_id", nullable = false)
    private SesionMesa sesionMesa;

    @Setter
    @Getter
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> items;

    @Setter
    @Getter
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<Factura> facturas;


    public Pedido() {
    }
}