package org.bugbusters.controller;

import org.bugbusters.dto.EstadoUpdateDTO;
import org.bugbusters.dto.ItemPedidoDTO;
import org.bugbusters.entity.ItemPedido;
import org.bugbusters.entity.Pedido;
import org.bugbusters.entity.Producto;
import org.bugbusters.repository.ItemPedidoRepository;
import org.bugbusters.repository.PedidoRepository;
import org.bugbusters.repository.ProductoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador que gestiona los items dentro de un pedido.
 * Permite crear, listar, actualizar el estado y eliminar items de pedido.
 */
@RestController
@RequestMapping("/api/item_pedido")
public class ItemPedidoController {

    private final ItemPedidoRepository itemPedidoRepository;
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    /**
     * Constructor con los repositorios necesarios para gestionar los items del pedido.
     */
    public ItemPedidoController(ItemPedidoRepository itemPedidoRepository, PedidoRepository pedidoRepository, ProductoRepository productoRepository) {
        this.itemPedidoRepository = itemPedidoRepository;
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    /**
     * Crea un nuevo item para un pedido.
     *
     * @param itemPedidoDTO datos del item a crear (incluye ID de pedido y producto).
     * @return el item creado y guardado en la base de datos.
     */
    @PostMapping
    public ItemPedido crearItemPedido(@RequestBody ItemPedidoDTO itemPedidoDTO) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(itemPedidoDTO.getPedidoId());
        Optional<Producto> productoOpt = productoRepository.findById(itemPedidoDTO.getProductoId());

        if (pedidoOpt.isEmpty()) {
            throw new RuntimeException("Pedido no encontrado");
        }
        if (productoOpt.isEmpty()) {
            throw new RuntimeException("Producto no encontrado");
        }

        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setCantidad(itemPedidoDTO.getCantidad());
        itemPedido.setPrecioUnitario(itemPedidoDTO.getPrecioUnitario());
        itemPedido.setEstado(itemPedidoDTO.getEstado());
        itemPedido.setNotas(itemPedidoDTO.getNotas());
        itemPedido.setPedido(pedidoOpt.get());
        itemPedido.setProducto(productoOpt.get());

        return itemPedidoRepository.save(itemPedido);
    }

    /**
     * Devuelve todos los items de un pedido concreto.
     *
     * @param pedidoId ID del pedido.
     * @return lista de items asociados a ese pedido.
     */
    @GetMapping("/pedido/{pedidoId}")
    public List<ItemPedidoDTO> listarItemsPorPedido(@PathVariable Long pedidoId) {
        return itemPedidoRepository.findByPedidoId(pedidoId)
                .stream()
                .map(ItemPedidoDTO::new)
                .toList();
    }

    /**
     * Actualiza solo el estado de un item de pedido.
     *
     * @param id  ID del item de pedido.
     * @param dto objeto con el nuevo estado.
     * @return el item actualizado.
     */
    @PatchMapping("/{id}/estado")
    public ItemPedido actualizarEstado(@PathVariable Long id, @RequestBody EstadoUpdateDTO dto) {
        ItemPedido item = itemPedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ItemPedido no encontrado"));
        item.setEstado(dto.getEstado());
        return itemPedidoRepository.save(item);
    }

    /**
     * Elimina un item de pedido por su ID.
     *
     * @param id ID del item a eliminar.
     */
    @DeleteMapping("/{id}")
    public void eliminarItemPedido(@PathVariable Long id) {
        itemPedidoRepository.deleteById(id);
    }
}