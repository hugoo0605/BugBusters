package org.bugbusters.repository;

import org.bugbusters.entity.SesionMesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SesionMesaRepository extends JpaRepository<SesionMesa, UUID> {
    List<SesionMesa> findByFechaCierreIsNull();
    Optional<SesionMesa> findByTokenAcceso(String token);
    List<SesionMesa> findByMesaId(Long mesaId);
}
