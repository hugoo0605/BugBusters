package org.bugbusters.service;

import org.bugbusters.dto.FacturaDTO;
import org.bugbusters.entity.*;
import org.bugbusters.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Servicio para manejar facturas y su relación con pedidos y sesiones de mesas.
 * Permite generar facturas, listar por estado y marcar facturas como pagadas.
 */
@Service
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final PedidoRepository pedidoRepository;
    private final SesionMesaRepository sesionMesaRepository;
    private final FacturaPedidoRepository facturaPedidoRepository;
    private final MesaRepository mesaRepository;

    public FacturaService(FacturaRepository facturaRepository,
                          PedidoRepository pedidoRepository,
                          SesionMesaRepository sesionMesaRepository,
                          FacturaPedidoRepository facturaPedidoRepository,
                          MesaRepository mesaRepository) {
        this.facturaRepository = facturaRepository;
        this.pedidoRepository = pedidoRepository;
        this.sesionMesaRepository = sesionMesaRepository;
        this.facturaPedidoRepository = facturaPedidoRepository;
        this.mesaRepository = mesaRepository;
    }

    /**
     * Genera una factura para un pedido dado.
     * Cambia el estado del pedido a FINALIZADO.
     *
     * @param pedidoId id del pedido a facturar
     * @return datos de la factura creada
     */
    public FacturaDTO generarFactura(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        Factura factura = new Factura();
        factura.setTotal(pedido.getTotal());
        factura.setFecha(LocalDateTime.now());
        factura.setEstado("NO_PAGADA");

        Factura guardada = facturaRepository.save(factura);

        // Crear relación con pedido
        FacturaPedido fp = new FacturaPedido();
        fp.setFactura(guardada);
        fp.setPedido(pedido);
        facturaPedidoRepository.save(fp);

        // Marcar el pedido como FINALIZADO
        pedido.setEstado("FINALIZADO");
        pedidoRepository.save(pedido);

        // Construir DTO
        FacturaDTO dto = new FacturaDTO();
        dto.setId(guardada.getId());
        dto.setTotal(guardada.getTotal());
        dto.setFecha(guardada.getFecha());
        dto.setEstado(guardada.getEstado());
        dto.setPedidoIds(List.of(pedidoId));

        return dto;
    }

    /**
     * Genera una factura para todos los pedidos entregados
     * de la sesión activa de una mesa (por número).
     * Cambia el estado de esos pedidos a FINALIZADO.
     *
     * @param numeroMesa número de la mesa
     * @return datos de la factura creada con todos los pedidos
     */
    @Transactional
    public FacturaDTO generarFacturaPorNumeroMesa(int numeroMesa) {
        // Buscar la mesa por número
        Mesa mesa = mesaRepository.findByNumero(numeroMesa)
                .orElseThrow(() -> new RuntimeException("No existe ninguna mesa con número " + numeroMesa));

        Long mesaId = mesa.getId();

        // Obtener la sesión activa (sin fecha de cierre) de la mesa
        SesionMesa sesionActiva = sesionMesaRepository
                .findByMesaIdAndFechaCierreIsNull(mesaId)
                .orElseThrow(() -> new RuntimeException("No hay sesión activa para la mesa con número " + numeroMesa));

        UUID sesionId = sesionActiva.getId();

        // Buscar pedidos con estado "ENTREGADO" en esa sesión
        List<Pedido> pedidosPendientes = pedidoRepository.findBySesionMesaIdAndEstado(sesionId, "ENTREGADO");

        if (pedidosPendientes.isEmpty()) {
            throw new RuntimeException("No hay pedidos entregados para facturar en la mesa " + numeroMesa);
        }

        // Sumar el total de los pedidos
        double montoTotalAcumulado = pedidosPendientes.stream()
                .mapToDouble(Pedido::getTotal)
                .sum();

        // Crear y guardar la factura
        Factura nuevaFactura = new Factura();
        nuevaFactura.setTotal(montoTotalAcumulado);
        nuevaFactura.setFecha(LocalDateTime.now());
        nuevaFactura.setEstado("NO_PAGADA");
        Factura facturaGuardada = facturaRepository.save(nuevaFactura);

        // Crear relación factura-pedido y marcar pedidos como FINALIZADO
        List<Long> pedidosIdsFacturados = new ArrayList<>();
        for (Pedido p : pedidosPendientes) {
            FacturaPedido fp = new FacturaPedido();
            fp.setFactura(facturaGuardada);
            fp.setPedido(p);
            facturaPedidoRepository.save(fp);

            p.setEstado("FINALIZADO");
            pedidoRepository.save(p);

            pedidosIdsFacturados.add(p.getId());
        }

        // Construir DTO con datos de la factura
        FacturaDTO dto = new FacturaDTO();
        dto.setId(facturaGuardada.getId());
        dto.setTotal(facturaGuardada.getTotal());
        dto.setFecha(facturaGuardada.getFecha());
        dto.setEstado(facturaGuardada.getEstado());
        dto.setPedidoIds(pedidosIdsFacturados);

        return dto;
    }

    /**
     * Lista todas las facturas que tienen un estado específico.
     *
     * @param estado estado para filtrar (ejemplo: "PAGADA")
     * @return lista de facturas con ese estado
     */
    public List<FacturaDTO> listarFacturasPorEstado(String estado) {
        List<Factura> facturas = facturaRepository.findByEstado(estado);
        return facturas.stream().map(f -> {
            FacturaDTO dto = new FacturaDTO();
            dto.setId(f.getId());
            dto.setTotal(f.getTotal());
            dto.setFecha(f.getFecha());
            dto.setEstado(f.getEstado());
            // Cargar pedidos asociados
            List<Long> pedidosIds = f.getFacturasPedidos().stream()
                    .map(fp -> fp.getPedido().getId())
                    .collect(Collectors.toList());
            dto.setPedidoIds(pedidosIds);
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * Marca una factura como PAGADA.
     *
     * @param facturaId id de la factura a actualizar
     */
    @Transactional
    public void marcarFacturaPagada(Long facturaId) {
        Factura f = facturaRepository.findById(facturaId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada"));
        f.setEstado("PAGADA");
        facturaRepository.save(f);
    }
}
