package org.bugbusters.controller;

import org.bugbusters.dto.FacturaDTO;
import org.bugbusters.service.FacturaService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    private final FacturaService facturaService;

    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    @PostMapping("/generar/{pedidoId}")
    public FacturaDTO generarFactura(@PathVariable Long pedidoId) {
        return facturaService.generarFactura(pedidoId);
    }
}
