package org.bugbusters.controller;

import org.bugbusters.dto.FacturaDTO;
import org.bugbusters.service.FacturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador que se encarga de todo lo relacionado con las facturas.
 * Aquí se pueden generar, consultar y marcar facturas como pagadas.
 */
@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    private final FacturaService facturaService;

    /**
     * Constructor para inyectar el servicio de facturas.
     *
     * @param facturaService servicio que contiene la lógica para gestionar facturas.
     */
    public FacturaController(FacturaService facturaService) {
        this.facturaService = facturaService;
    }

    /**
     * Genera una factura para un pedido concreto usando su ID.
     *
     * @param pedidoId ID del pedido.
     * @return los datos de la factura generada.
     */
    @PostMapping("/generar/{pedidoId}")
    public FacturaDTO generarFactura(@PathVariable Long pedidoId) {
        return facturaService.generarFactura(pedidoId);
    }

    /**
     * Genera una factura para una mesa.
     * Agrupa todos los pedidos no pagados de la sesión activa de esa mesa.
     *
     * @param numeroMesa número de la mesa.
     * @return los datos de la factura generada.
     */
    @PostMapping("/mesa/numero/{numeroMesa}")
    public FacturaDTO generarFacturaPorNumeroMesa(@PathVariable int numeroMesa) {
        return facturaService.generarFacturaPorNumeroMesa(numeroMesa);
    }

    /**
     * Devuelve todas las facturas que aún no se han pagado.
     *
     * @return lista de facturas pendientes.
     */
    @GetMapping("/pendientes")
    public List<FacturaDTO> listarFacturasPendientes() {
        return facturaService.listarFacturasPorEstado("NO_PAGADA");
    }

    /**
     * Marca una factura como pagada.
     *
     * @param facturaId ID de la factura.
     * @return respuesta vacía con estado OK si fue exitoso.
     */
    @PutMapping("/{facturaId}/pagar")
    public ResponseEntity<Void> pagarFactura(@PathVariable Long facturaId) {
        facturaService.marcarFacturaPagada(facturaId);
        return ResponseEntity.ok().build();
    }
}
