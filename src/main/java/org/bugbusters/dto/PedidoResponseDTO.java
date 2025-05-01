package org.bugbusters.dto;

import org.bugbusters.entity.Pedido;

import java.time.LocalDateTime;
import java.util.UUID;

public class PedidoResponseDTO {
    private Long id;
    private String estado;
    private LocalDateTime fechaCreacion;
    private String notas;
    private double total;
    private Long trabajadorId;
    private UUID sesionId;

    public PedidoResponseDTO(Pedido pedido) {
        this.id = pedido.getId();
        this.estado = pedido.getEstado();
        this.fechaCreacion = pedido.getFechaCreacion();
        this.notas = pedido.getNotas();
        this.total = pedido.getTotal();
        this.trabajadorId = pedido.getTrabajador() != null ? pedido.getTrabajador().getId() : null;
        this.sesionId = pedido.getSesionMesa() != null ? pedido.getSesionMesa().getId() : null;
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Long getTrabajadorId() {
        return trabajadorId;
    }

    public void setTrabajadorId(Long trabajadorId) {
        this.trabajadorId = trabajadorId;
    }

    public UUID getSesionId() {
        return sesionId;
    }

    public void setSesionId(UUID sesionId) {
        this.sesionId = sesionId;
    }
}
