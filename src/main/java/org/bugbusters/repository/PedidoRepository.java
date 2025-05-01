package org.bugbusters.repository;

import org.bugbusters.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findBySesionMesaId(UUID sesionId);
    List<Pedido> findByEstado(String estado);
}

