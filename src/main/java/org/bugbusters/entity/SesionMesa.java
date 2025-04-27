package org.bugbusters.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sesiones_mesa")
@Data
public class SesionMesa {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "mesa_id")
    private Mesa mesa;

    @Column(name = "token_acceso")
    private String tokenAcceso;

    @Column(name = "fecha_apertura")
    private LocalDateTime fechaApertura;

    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;
}
