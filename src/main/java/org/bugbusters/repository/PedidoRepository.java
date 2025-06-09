package org.bugbusters.repository;

import org.bugbusters.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Repositorio para gestionar entidades de tipo Pedido.
 * Permite operaciones CRUD y consultas por sesión y estado.
 */
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    /**
     * Obtiene los pedidos de una sesión de mesa con un estado específico.
     *
     * @param sesionId ID de la sesión de mesa
     * @param estado estado del pedido
     * @return lista de pedidos filtrados por estado
     */
    List<Pedido> findBySesionMesaIdAndEstado(UUID sesionId, String estado);

    /**
     * Obtiene todos los pedidos con un estado específico.
     *
     * @param estado estado del pedido
     * @return lista de pedidos con ese estado
     */
    List<Pedido> findByEstado(String estado);

    /**
     * Obtiene todos los pedidos cuyo estado esté en una lista de estados.
     *
     * @param estados lista de estados
     * @return lista de pedidos con alguno de esos estados
     */
    List<Pedido> findByEstadoIn(List<String> estados);
}
