package org.bugbusters.service;

import org.bugbusters.dto.FacturaDTO;
import org.bugbusters.entity.Factura;
import org.bugbusters.entity.Pedido;
import org.bugbusters.repository.FacturaRepository;
import org.bugbusters.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final PedidoRepository pedidoRepository;

    public FacturaService(FacturaRepository facturaRepository, PedidoRepository pedidoRepository) {
        this.facturaRepository = facturaRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public FacturaDTO generarFactura(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        Factura factura = new Factura();
        factura.setPedido(pedido);
        factura.setTotal(pedido.getTotal());
        factura.setFecha(LocalDateTime.now());
        factura.setEstado("NO_PAGADA");

        Factura guardada = facturaRepository.save(factura);

        // Convertimos a DTO
        FacturaDTO dto = new FacturaDTO();
        dto.setId(guardada.getId());
        dto.setPedidoId(pedidoId);
        dto.setTotal(guardada.getTotal());
        dto.setFecha(guardada.getFecha());
        dto.setEstado(guardada.getEstado());

        return dto;
    }
}
