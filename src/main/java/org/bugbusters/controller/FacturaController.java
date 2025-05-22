package org.bugbusters.controller;

import org.bugbusters.dto.FacturaDTO;
import org.bugbusters.service.FacturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {
    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    /**
     * Factura para UN solo pedido (ya lo tenías):
     */
    @PostMapping("/generar/{pedidoId}")
    public FacturaDTO generarFactura(@PathVariable Long pedidoId) {
        return facturaService.generarFactura(pedidoId);
    }

    /**
     * NUEVO: Factura “por mesa” -> agrupa todos los pedidos no finalizados de la sesión activa.
     */
    @PostMapping("/mesa/{mesaId}")
    public FacturaDTO generarFacturaPorMesa(@PathVariable Long mesaId) {
        return facturaService.generarFacturaPorMesa(mesaId);
    }

    @GetMapping("/pendientes")
    public List<FacturaDTO> listarFacturasPendientes() {
        return facturaService.listarFacturasPorEstado("NO_PAGADA");
    }

    // 2) Marcar una factura como pagada
    @PutMapping("/{facturaId}/pagar")
    public ResponseEntity<Void> pagarFactura(@PathVariable Long facturaId) {
        facturaService.marcarFacturaPagada(facturaId);
        return ResponseEntity.ok().build();
    }
}