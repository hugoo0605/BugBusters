package org.bugbusters.repository;

import org.bugbusters.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByDisponibleTrue();
    List<Producto> findByCategoriaAndDisponibleTrue(String categoria);
    List<Producto> findByNombreContainingIgnoreCaseAndDisponibleTrue(String nombre);

}