package org.bugbusters.controller;

import org.bugbusters.dto.ItemPedidoDTO;
import org.bugbusters.entity.ItemPedido;
import org.bugbusters.entity.Pedido;
import org.bugbusters.entity.Producto;
import org.bugbusters.repository.ItemPedidoRepository;
import org.bugbusters.repository.PedidoRepository;
import org.bugbusters.repository.ProductoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/item_pedido")
public class ItemPedidoController {

    private final ItemPedidoRepository itemPedidoRepository;
    private final PedidoRepository pedidoRepository;
    private final ProductoRepository productoRepository;

    public ItemPedidoController(ItemPedidoRepository itemPedidoRepository, PedidoRepository pedidoRepository, ProductoRepository productoRepository) {
        this.itemPedidoRepository = itemPedidoRepository;
        this.pedidoRepository = pedidoRepository;
        this.productoRepository = productoRepository;
    }

    // Crear un nuevo ItemPedido
    @PostMapping
    public ItemPedido crearItemPedido(@RequestBody ItemPedidoDTO itemPedidoDTO) {
        // Buscar el Pedido y Producto correspondiente
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(itemPedidoDTO.getPedidoId());
        Optional<Producto> productoOpt = productoRepository.findById(itemPedidoDTO.getProductoId());

        if (pedidoOpt.isEmpty()) {
            throw new RuntimeException("Pedido no encontrado");
        }
        if (productoOpt.isEmpty()) {
            throw new RuntimeException("Producto no encontrado");
        }

        // Crear el objeto ItemPedido
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setCantidad(itemPedidoDTO.getCantidad());
        itemPedido.setPrecioUnitario(itemPedidoDTO.getPrecioUnitario());
        itemPedido.setEstado(itemPedidoDTO.getEstado());
        itemPedido.setNotas(itemPedidoDTO.getNotas());
        itemPedido.setPedido(pedidoOpt.get());
        itemPedido.setProducto(productoOpt.get());

        // Guardar y devolver el itemPedido
        return itemPedidoRepository.save(itemPedido);
    }
}
