package org.bugbusters.repository;

import org.bugbusters.entity.FacturaPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para gestionar entidades FacturaPedido.
 * Proporciona métodos para acceder a los datos de la relación entre facturas y pedidos.
 */
public interface FacturaPedidoRepository extends JpaRepository<FacturaPedido, Long> {

    /**
     * Busca todos los registros de FacturaPedido que estén asociados a una factura concreta.
     *
     * @param facturaId ID de la factura
     * @return lista de FacturaPedido relacionados con esa factura
     */
    List<FacturaPedido> findByFacturaId(Long facturaId);
}