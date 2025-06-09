package org.bugbusters.repository;

import org.bugbusters.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para gestionar los items de un pedido.
 * Permite realizar operaciones CRUD y consultas por pedido.
 */
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    /**
     * Obtiene la lista de items asociados a un pedido específico.
     *
     * @param pedidoId ID del pedido
     * @return lista de items que pertenecen a ese pedido
     */
    List<ItemPedido> findByPedidoId(Long pedidoId);
}
