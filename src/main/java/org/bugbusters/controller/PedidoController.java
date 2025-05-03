package org.bugbusters.controller;

import org.bugbusters.dto.PedidoDTO;
import org.bugbusters.dto.PedidoResponseDTO;
import org.bugbusters.entity.Pedido;
import org.bugbusters.entity.SesionMesa;
import org.bugbusters.entity.Trabajador;
import org.bugbusters.repository.PedidoRepository;
import org.bugbusters.repository.SesionMesaRepository;
import org.bugbusters.repository.TrabajadorRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final SesionMesaRepository sesionMesaRepository;
    private final TrabajadorRepository trabajadorRepository;

    public PedidoController(PedidoRepository pedidoRepository,
                            SesionMesaRepository sesionMesaRepository,
                            TrabajadorRepository trabajadorRepository) {
        this.pedidoRepository = pedidoRepository;
        this.sesionMesaRepository = sesionMesaRepository;
        this.trabajadorRepository = trabajadorRepository;
    }

    // Crear un nuevo pedido
    @PostMapping
    public Pedido crearPedido(@RequestBody PedidoDTO pedidoDTO) {
        // Obtener la sesión correspondiente al UUID
        Optional<SesionMesa> sesionMesaOpt = sesionMesaRepository.findById(pedidoDTO.getSesionId());
        if (sesionMesaOpt.isEmpty()) {
            throw new RuntimeException("Sesion no encontrada");
        }

        // Obtener el trabajador correspondiente
        Optional<Trabajador> trabajadorOpt = trabajadorRepository.findById(pedidoDTO.getTrabajadorId());
        if (trabajadorOpt.isEmpty()) {
            throw new RuntimeException("Trabajador no encontrado");
        }

        // Crear el objeto Pedido y asignar las relaciones
        Pedido pedido = new Pedido();
        pedido.setEstado(pedidoDTO.getEstado());
        pedido.setFechaCreacion(pedidoDTO.getFechaCreacion());
        pedido.setNotas(pedidoDTO.getNotas());
        pedido.setTotal(pedidoDTO.getTotal());
        pedido.setSesionMesa(sesionMesaOpt.get());
        pedido.setTrabajador(trabajadorOpt.get());

        // Guardar y devolver el pedido
        return pedidoRepository.save(pedido);
    }

    // Obtener todos los pedidos
    @GetMapping
    public List<PedidoResponseDTO> listarPedidos() {
        return pedidoRepository.findAll()
                .stream()
                .map(PedidoResponseDTO::new)
                .collect(Collectors.toList());
    }


    // Obtener pedidos por estado
    @GetMapping("/estado/{estado}")
    public List<Pedido> listarPedidosPorEstado(@PathVariable String estado) {
        return pedidoRepository.findByEstado(estado);
    }

    // Actualizar estado de un pedido
    @PutMapping("/{id}")
    public Pedido actualizarEstadoPedido(@PathVariable Long id, @RequestParam String estado) {
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        pedidoExistente.setEstado(estado);
        return pedidoRepository.save(pedidoExistente);
    }
}
