package org.bugbusters.service;

import org.bugbusters.entity.Producto;
import org.bugbusters.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Obtener todos los productos disponibles
    public List<Producto> obtenerProductosDisponibles() {
        return productoRepository.findByDisponibleTrue(); // Llama al repositorio
    }

    // Obtener un producto por su ID
    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id).orElse(null); // Recupera producto por ID
    }
}
