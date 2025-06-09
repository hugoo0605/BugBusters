package org.bugbusters.repository;

import org.bugbusters.entity.SesionMesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio para acceder a las sesiones de mesa.
 * Permite consultar sesiones activas, por token, por mesa, etc.
 */
public interface SesionMesaRepository extends JpaRepository<SesionMesa, UUID> {

    /**
     * Devuelve todas las sesiones que aún están activas (sin fecha de cierre).
     *
     * @return lista de sesiones activas
     */
    List<SesionMesa> findByFechaCierreIsNull();

    /**
     * Busca una sesión por su token de acceso.
     *
     * @param token el token de la sesión
     * @return sesión encontrada (si existe)
     */
    Optional<SesionMesa> findByTokenAcceso(String token);

    /**
     * Devuelve todas las sesiones asociadas a una mesa concreta.
     *
     * @param mesaId ID de la mesa
     * @return lista de sesiones para esa mesa
     */
    List<SesionMesa> findByMesaId(Long mesaId);

    /**
     * Busca la sesión activa (sin fecha de cierre) de una mesa concreta.
     *
     * @param mesaId ID de la mesa
     * @return sesión activa encontrada (si existe)
     */
    Optional<SesionMesa> findByMesaIdAndFechaCierreIsNull(Long mesaId);
}