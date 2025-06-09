package org.bugbusters.repository;

import org.bugbusters.entity.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio para gestionar entidades de tipo Mesa.
 * Permite operaciones CRUD y búsqueda por número de mesa.
 */
public interface MesaRepository extends JpaRepository<Mesa, Long> {

    /**
     * Busca una mesa por su número.
     *
     * @param numero número identificador de la mesa
     * @return una mesa opcional que coincida con el número
     */
    Optional<Mesa> findByNumero(int numero);
}
