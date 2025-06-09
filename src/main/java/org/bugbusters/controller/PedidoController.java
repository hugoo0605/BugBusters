package org.bugbusters.controller;

import org.bugbusters.dto.ItemPedidoDTO;
import org.bugbusters.dto.PedidoDTO;
import org.bugbusters.dto.PedidoResponseDTO;
import org.bugbusters.entity.*;
import org.bugbusters.repository.*;
import org.bugbusters.service.PedidoNotificacionService;
import org.bugbusters.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controlador que gestiona todo lo relacionado con los pedidos del sistema.
 * Permite crear pedidos, obtenerlos, actualizarlos y añadir items.
 */
@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final SesionMesaRepository sesionMesaRepository;
    private final TrabajadorRepository trabajadorRepository;
    private final ProductoRepository productoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final PedidoService pedidoService;
    private final PedidoNotificacionService pedidoNotificacionService;

    public PedidoController(PedidoRepository pedidoRepository,
                            SesionMesaRepository sesionMesaRepository,
                            TrabajadorRepository trabajadorRepository,
                            ProductoRepository productoRepository,
                            ItemPedidoRepository itemPedidoRepository,
                            PedidoService pedidoService,
                            PedidoNotificacionService pedidoNotificacionService) {
        this.pedidoRepository = pedidoRepository;
        this.sesionMesaRepository = sesionMesaRepository;
        this.trabajadorRepository = trabajadorRepository;
        this.productoRepository = productoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
        this.pedidoService = pedidoService;
        this.pedidoNotificacionService = pedidoNotificacionService;
    }

    /**
     * Devuelve una lista de pedidos que están activos (pendiente, en preparación o listos).
     *
     * @return lista de pedidos activos.
     */
    @GetMapping("/activos")
    public List<PedidoResponseDTO> obtenerPedidosActivos() {
        return pedidoRepository.findByEstadoIn(List.of("PENDIENTE", "PREPARACION", "LISTO"))
                .stream()
                .map(PedidoResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Crea un nuevo pedido y sus items.
     *
     * @param pedidoDTO datos del pedido a crear.
     * @return mensaje de éxito o error.
     */
    @PostMapping
    public ResponseEntity<String> crearPedido(@RequestBody PedidoDTO pedidoDTO) {
        try {
            Optional<SesionMesa> sesionMesaOpt = sesionMesaRepository.findById(pedidoDTO.getSesionId());
            if (sesionMesaOpt.isEmpty()) {
                throw new RuntimeException("Sesion no encontrada");
            }

            Optional<Trabajador> trabajadorOpt = trabajadorRepository.findById(pedidoDTO.getTrabajadorId());
            if (trabajadorOpt.isEmpty()) {
                throw new RuntimeException("Trabajador no encontrado");
            }

            Pedido pedido = new Pedido();
            pedido.setEstado(pedidoDTO.getEstado());
            pedido.setFechaCreacion(pedidoDTO.getFechaCreacion());
            pedido.setNotas(pedidoDTO.getNotas());
            pedido.setTotal(pedidoDTO.getTotal());
            pedido.setSesionMesa(sesionMesaOpt.get());
            pedido.setTrabajador(trabajadorOpt.get());

            pedido = pedidoRepository.save(pedido);

            for (ItemPedidoDTO itemDTO : pedidoDTO.getItems()) {
                Optional<Producto> productoOpt = productoRepository.findById(itemDTO.getProductoId());
                if (productoOpt.isEmpty()) {
                    return ResponseEntity.badRequest().body("Producto no encontrado: ID " + itemDTO.getProductoId());
                }

                ItemPedido item = new ItemPedido();
                item.setPedido(pedido);
                item.setProducto(productoOpt.get());
                item.setCantidad(itemDTO.getCantidad());
                item.setPrecioUnitario(itemDTO.getPrecioUnitario());
                item.setEstado("PENDIENTE");
                item.setNotas(itemDTO.getNotas());
                itemPedidoRepository.save(item);
            }

            return ResponseEntity.ok("Pedido guardado correctamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error al guardar el pedido: " + e.getMessage());
        }
    }

    /**
     * Devuelve todos los pedidos registrados en el sistema.
     *
     * @return lista de todos los pedidos.
     */
    @GetMapping
    public List<PedidoResponseDTO> listarPedidos() {
        return pedidoRepository.findAll()
                .stream()
                .map(PedidoResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza el estado de un pedido concreto.
     *
     * @param id     ID del pedido.
     * @param estado nuevo estado a asignar.
     * @return el pedido actualizado.
     */
    @PutMapping("/{id}")
    public Pedido actualizarEstadoPedido(@PathVariable Long id, @RequestParam String estado) {
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        pedidoExistente.setEstado(estado);
        return pedidoRepository.save(pedidoExistente);
    }

    /**
     * Lista los pedidos filtrados por su estado.
     *
     * @param estado estado a buscar (ej. "PENDIENTE").
     * @return lista de pedidos con ese estado.
     */
    @GetMapping("/estado/{estado}")
    public List<PedidoResponseDTO> listarPedidosPorEstado(@PathVariable String estado) {
        return pedidoRepository.findByEstado(estado)
                .stream()
                .map(PedidoResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los items de un pedido específico.
     *
     * @param id ID del pedido.
     * @return lista de items que contiene ese pedido.
     */
    @GetMapping("/{id}/items")
    public List<ItemPedidoDTO> obtenerItemsDePedido(@PathVariable Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        return pedido.getItems().stream()
                .map(ItemPedidoDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Devuelve la información completa de un pedido según su ID.
     *
     * @param id ID del pedido.
     * @return DTO con los datos del pedido.
     */
    @GetMapping("/{id}")
    public PedidoResponseDTO obtenerPedidoPorId(@PathVariable Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        return new PedidoResponseDTO(pedido);
    }

    /**
     * Añade un nuevo item a un pedido existente y notifica a la mesa asociada.
     *
     * @param pedidoId ID del pedido.
     * @param item     datos del item a añadir.
     * @return el pedido actualizado.
     */
    @PostMapping("/{id}/items")
    public ResponseEntity<?> anadirItem(@PathVariable Long pedidoId, @RequestBody ItemPedidoDTO item) {
        Pedido pedidoActualizado = pedidoService.anadirItem(pedidoId, item);

        UUID mesaUUID = pedidoActualizado.getSesionMesa().getId();
        pedidoNotificacionService.notificarCambioEnMesa(mesaUUID, pedidoActualizado);

        return ResponseEntity.ok(pedidoActualizado);
    }

}
