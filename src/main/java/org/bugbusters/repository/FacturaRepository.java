package org.bugbusters.repository;

import org.bugbusters.entity.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para acceder y gestionar las facturas.
 * Proporciona operaciones CRUD y consultas personalizadas.
 */
public interface FacturaRepository extends JpaRepository<Factura, Long> {

    /**
     * Busca todas las facturas que coincidan con el estado dado.
     *
     * @param estado el estado de la factura (por ejemplo, "PAGADA", "PENDIENTE", etc.)
     * @return una lista de facturas con ese estado
     */
    List<Factura> findByEstado(String estado);
}
