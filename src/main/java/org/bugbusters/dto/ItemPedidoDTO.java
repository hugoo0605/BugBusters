package org.bugbusters.dto;

public class ItemPedidoDTO {

    private int cantidad;
    private double precioUnitario;
    private String estado;
    private String notas;
    private Long pedidoId;
    private Long productoId;

    // Constructor
    public ItemPedidoDTO(int cantidad, double precioUnitario, String estado, String notas, Long pedidoId, Long productoId) {
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.estado = estado;
        this.notas = notas;
        this.pedidoId = pedidoId;
        this.productoId = productoId;
    }

    // Getters y setters
    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }
}
