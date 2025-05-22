package org.bugbusters.service;

import org.bugbusters.dto.FacturaDTO;
import org.bugbusters.entity.Factura;
import org.bugbusters.entity.FacturaPedido;
import org.bugbusters.entity.Pedido;
import org.bugbusters.entity.SesionMesa;
import org.bugbusters.repository.FacturaPedidoRepository;
import org.bugbusters.repository.FacturaRepository;
import org.bugbusters.repository.PedidoRepository;
import org.bugbusters.repository.SesionMesaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final PedidoRepository pedidoRepository;
    private final SesionMesaRepository sesionMesaRepository;
    private final FacturaPedidoRepository facturaPedidoRepository;

    public FacturaService(FacturaRepository facturaRepository,
                          PedidoRepository pedidoRepository,
                          SesionMesaRepository sesionMesaRepository,
                          FacturaPedidoRepository facturaPedidoRepository) {
        this.facturaRepository = facturaRepository;
        this.pedidoRepository = pedidoRepository;
        this.sesionMesaRepository = sesionMesaRepository;
        this.facturaPedidoRepository = facturaPedidoRepository;
    }

    /**
     * Método existente (facturar un único pedido). Se deja si aún lo quieres conservar.
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
     * Nuevo método: generar factura para todos los pedidos 'ABIERTO' de la sesión activa de una mesa.
     */
    @Transactional
    public FacturaDTO generarFacturaPorMesa(Long mesaId) {
        // 1) Obtener la sesión activa (fechaCierre IS NULL) de la mesa
        SesionMesa sesionActiva = sesionMesaRepository
                .findByMesaIdAndFechaCierreIsNull(mesaId)
                .orElseThrow(() -> new RuntimeException("No hay sesión activa para la mesa con id " + mesaId));

        UUID sesionId = sesionActiva.getId();

        // 2) Buscar únicamente los pedidos con estado "ABIERTO" en esa sesión
        List<Pedido> pedidosPendientes = pedidoRepository.findBySesionMesaIdAndEstado(sesionId, "ENTREGADO");

        if (pedidosPendientes.isEmpty()) {
            throw new RuntimeException("No hay pedidos abiertos para facturar en la mesa " + mesaId);
        }

        // 3) Sumar los totales de cada pedido
        double montoTotalAcumulado = pedidosPendientes.stream()
                .mapToDouble(Pedido::getTotal)
                .sum();

        // 4) Crear y guardar la nueva Factura
        Factura nuevaFactura = new Factura();
        nuevaFactura.setTotal(montoTotalAcumulado);
        nuevaFactura.setFecha(LocalDateTime.now());
        nuevaFactura.setEstado("NO_PAGADA");
        Factura facturaGuardada = facturaRepository.save(nuevaFactura);

        // 5) Por cada pedido 'ABIERTO', crear entrada en factura_pedido y marcar como 'FINALIZADO'
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

        // 6) Construir y devolver el DTO resultante
        FacturaDTO dto = new FacturaDTO();
        dto.setId(facturaGuardada.getId());
        dto.setTotal(facturaGuardada.getTotal());
        dto.setFecha(facturaGuardada.getFecha());
        dto.setEstado(facturaGuardada.getEstado());
        dto.setPedidoIds(pedidosIdsFacturados);

        return dto;
    }
}
