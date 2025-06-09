package org.bugbusters.service;

import org.bugbusters.entity.Producto;
import org.bugbusters.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para gestionar operaciones relacionadas con los productos.
 */
@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    /**
     * Constructor con inyección del repositorio de productos.
     *
     * @param productoRepository el repositorio de productos
     */
    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Devuelve todos los productos que están marcados como disponibles.
     *
     * @return lista de productos disponibles
     */
    public List<Producto> obtenerProductosDisponibles() {
        return productoRepository.findByDisponibleTrue(); // Llama al repositorio
    }

    /**
     * Busca un producto por su ID.
     *
     * @param id el ID del producto
     * @return el producto si existe, o null si no se encuentra
     */
    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id).orElse(null); // Recupera producto por ID
    }
}