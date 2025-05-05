package org.bugbusters.controller;

import org.bugbusters.entity.Producto;
import org.bugbusters.repository.ProductoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoRepository productoRepository;

    public ProductoController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Obtener todos los productos disponibles
    @GetMapping
    public List<Producto> listarProductos() {
        return productoRepository.findByDisponibleTrue();
    }

    // Obtener un producto por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear un nuevo producto
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        return ResponseEntity.ok(productoRepository.save(producto));
    }

    // Actualizar un producto existente
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        return productoRepository.findById(id).map(existente -> {
            existente.setNombre(producto.getNombre());
            existente.setDescripcion(producto.getDescripcion());
            existente.setPrecio(producto.getPrecio());
            existente.setCategoria(producto.getCategoria());
            existente.setDisponible(producto.getDisponible());
            existente.setTiempoPreparacion(producto.getTiempoPreparacion());
            existente.setImagenes(producto.getImagenes());
            return ResponseEntity.ok(productoRepository.save(existente));
        }).orElse(ResponseEntity.notFound().build());
    }

    // Deshabilitar un producto (no eliminar físicamente)
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deshabilitarProducto(@PathVariable Long id) {
        return productoRepository.findById(id).map(producto -> {
            producto.setDisponible(false);
            productoRepository.save(producto);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/categoria/{categoria}")
    public List<Producto> obtenerPorCategoria(@PathVariable String categoria) {
        return productoRepository.findByCategoriaAndDisponibleTrue(categoria.toUpperCase());
    }

    @GetMapping("/buscar")
    public List<Producto> buscarPorNombre(@RequestParam String nombre) {
        return productoRepository.findByNombreContainingIgnoreCaseAndDisponibleTrue(nombre);
    }

    @GetMapping("/especiales")
    public List<Producto> obtenerEspeciales() {
        return productoRepository.findByCategoriaAndDisponibleTrue("ESPECIAL");
    }

}
