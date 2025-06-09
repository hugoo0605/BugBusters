package org.bugbusters.repository;

import org.bugbusters.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repositorio para la entidad Producto.
 * Proporciona métodos para consultar productos según disponibilidad, categoría y nombre.
 */
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    /**
     * Devuelve todos los productos disponibles ordenados por ID.
     *
     * @return lista de productos disponibles
     */
    @Query("FROM Producto p ORDER BY p.id ASC")
    List<Producto> findByDisponibleTrue();

    /**
     * Devuelve los productos disponibles de una categoría específica.
     *
     * @param categoria la categoría del producto
     * @return lista de productos disponibles de esa categoría
     */
    List<Producto> findByCategoriaAndDisponibleTrue(String categoria);

    /**
     * Busca productos disponibles que contengan el nombre indicado (ignorando mayúsculas y minúsculas).
     *
     * @param nombre el nombre a buscar
     * @return lista de productos que coincidan con el nombre
     */
    List<Producto> findByNombreContainingIgnoreCaseAndDisponibleTrue(String nombre);

    /**
     * Devuelve una lista de categorías distintas entre todos los productos.
     *
     * @return lista de nombres de categorías únicas
     */
    @Query("SELECT DISTINCT p.categoria FROM Producto p")
    List<String> findDistinctCategorias();
}