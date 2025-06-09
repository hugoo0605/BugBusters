package org.bugbusters.repository;

import org.bugbusters.entity.ComandaCocina;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para gestionar las entidades ComandaCocina.
 * Hereda operaciones CRUD básicas de JpaRepository.
 */
public interface ComandaCocinaRepository extends JpaRepository<ComandaCocina, Long> {
}
