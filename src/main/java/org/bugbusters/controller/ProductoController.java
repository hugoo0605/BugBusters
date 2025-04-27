package org.bugbusters.controller;

import org.bugbusters.entity.Producto;
import org.bugbusters.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductoController {

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // Ruta para obtener todos los productos disponibles
    @GetMapping("/api/productos")
    public List<Producto> obtenerProductosDisponibles() {
        return productoService.obtenerProductosDisponibles();
    }

    // Ruta para obtener un producto por su ID
    @GetMapping("/api/productos/{id}")
    public Producto obtenerProductoPorId(@PathVariable Long id) {
        return productoService.obtenerProductoPorId(id);
    }
}

