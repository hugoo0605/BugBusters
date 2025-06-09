package org.bugbusters.service;

import org.bugbusters.dto.ItemPedidoDTO;
import org.bugbusters.entity.ItemPedido;
import org.bugbusters.entity.Pedido;
import org.bugbusters.entity.Producto;
import org.bugbusters.repository.ItemPedidoRepository;
import org.bugbusters.repository.MesaRepository;
import org.bugbusters.repository.PedidoRepository;
import org.bugbusters.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para gestionar operaciones relacionadas con pedidos.
 */
@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private MesaRepository mesaRepository;

    /**
     * Añade un nuevo item a un pedido existente.
     *
     * @param pedidoId id del pedido al que se añadirá el item
     * @param itemDTO datos del item a añadir (producto y cantidad)
     * @return el pedido actualizado con el nuevo item
     * @throws RuntimeException si el pedido o el producto no existen
     */
    @Transactional
    public Pedido anadirItem(Long pedidoId, ItemPedidoDTO itemDTO) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        Producto producto = productoRepository.findById(itemDTO.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        ItemPedido item = new ItemPedido();
        item.setProducto(producto);
        item.setCantidad(itemDTO.getCantidad());
        item.setPrecioUnitario(producto.getPrecio());
        item.setPedido(pedido);

        itemPedidoRepository.save(item);

        pedido.getItems().add(item);
        return pedidoRepository.save(pedido);
    }
}