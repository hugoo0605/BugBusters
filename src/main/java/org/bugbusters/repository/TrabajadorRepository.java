package org.bugbusters.repository;

import org.bugbusters.entity.Trabajador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio para manejar datos de trabajadores.
 * Permite buscar por email y comprobar si un email ya existe.
 */
public interface TrabajadorRepository extends JpaRepository<Trabajador, Long> {

    /**
     * Busca un trabajador por su email.
     * Usado para login o verificación.
     *
     * @param email el email del trabajador
     * @return trabajador si existe
     */
    Optional<Trabajador> findByEmail(String email);

    /**
     * Comprueba si ya existe un trabajador con ese email.
     *
     * @param email el email a comprobar
     * @return true si existe, false si no
     */
    boolean existsByEmail(String email);
}