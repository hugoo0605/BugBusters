package org.bugbusters.controller;

import org.bugbusters.entity.Producto;
import org.bugbusters.repository.ProductoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar los productos del sistema.
 * Permite consultar, crear, actualizar, deshabilitar y buscar productos.
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoRepository productoRepository;

    /**
     * Constructor que inyecta el repositorio de productos.
     *
     * @param productoRepository el repositorio de productos
     */
    public ProductoController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Devuelve todos los productos que están disponibles (activos).
     *
     * @return lista de productos disponibles
     */
    @GetMapping
    public List<Producto> listarProductos() {
        return productoRepository.findByDisponibleTrue();
    }

    /**
     * Busca un producto por su ID.
     *
     * @param id el ID del producto
     * @return el producto si existe, o 404 si no se encuentra
     */
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo producto.
     *
     * @param producto el producto a crear
     * @return el producto creado
     */
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        return ResponseEntity.ok(productoRepository.save(producto));
    }

    /**
     * Actualiza un producto existente por su ID.
     *
     * @param id       el ID del producto a actualizar
     * @param producto los nuevos datos del producto
     * @return el producto actualizado, o 404 si no se encuentra
     */
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

    /**
     * Marca un producto como no disponible (no lo elimina de la base de datos).
     *
     * @param id el ID del producto a deshabilitar
     * @return 204 si se deshabilitó, o 404 si no se encuentra
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deshabilitarProducto(@PathVariable Long id) {
        return productoRepository.findById(id).map(producto -> {
            producto.setDisponible(false);
            productoRepository.save(producto);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    /**
     * Devuelve los productos disponibles de una categoría concreta.
     *
     * @param categoria el nombre de la categoría
     * @return lista de productos de esa categoría
     */
    @GetMapping("/categoria/{categoria}")
    public List<Producto> obtenerPorCategoria(@PathVariable String categoria) {
        return productoRepository.findByCategoriaAndDisponibleTrue(categoria.toUpperCase());
    }

    /**
     * Busca productos disponibles por nombre (ignora mayúsculas/minúsculas).
     *
     * @param nombre el texto a buscar dentro del nombre del producto
     * @return lista de productos que coinciden
     */
    @GetMapping("/buscar")
    public List<Producto> buscarPorNombre(@RequestParam String nombre) {
        return productoRepository.findByNombreContainingIgnoreCaseAndDisponibleTrue(nombre);
    }

    /**
     * Devuelve los productos de la categoría "ESPECIAL".
     *
     * @return lista de productos especiales
     */
    @GetMapping("/especiales")
    public List<Producto> obtenerEspeciales() {
        return productoRepository.findByCategoriaAndDisponibleTrue("ESPECIAL");
    }

    /**
     * Devuelve la lista de categorías distintas de productos.
     *
     * @return lista de nombres de categorías
     */
    @GetMapping("/categorias")
    public List<String> obtenerCategorias() {
        return productoRepository.findDistinctCategorias();
    }
}