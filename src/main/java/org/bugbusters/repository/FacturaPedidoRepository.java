package org.bugbusters.repository;

import org.bugbusters.entity.FacturaPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacturaPedidoRepository extends JpaRepository<FacturaPedido, Long> {
    List<FacturaPedido> findByFacturaId(Long facturaId);
}