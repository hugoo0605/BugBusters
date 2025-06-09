package com.bugbusters.staff.network

import com.bugbusters.staff.dto.FacturaDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * API Retrofit para operaciones relacionadas con facturas.
 */
interface FacturaApi {

    /**
     * Genera una factura a partir del ID de un pedido.
     * @param pedidoId ID del pedido para generar la factura.
     * @return Respuesta con la factura generada.
     */
    @POST("/api/facturas/generar/{pedidoId}")
    suspend fun generarFacturaPorPedido(
        @Path("pedidoId") pedidoId: Long
    ): Response<FacturaDTO>

    /**
     * Genera una factura a partir del número de mesa.
     * @param numeroMesa Número de la mesa para generar la factura.
     * @return Respuesta con la factura generada.
     */
    @POST("/api/facturas/mesa/numero/{numeroMesa}")
    suspend fun generarFacturaPorNumeroMesa(
        @Path("numeroMesa") numeroMesa: Int
    ): Response<FacturaDTO>

    /**
     * Obtiene la lista de facturas pendientes.
     * @return Respuesta con la lista de facturas pendientes.
     */
    @GET("/api/facturas/pendientes")
    suspend fun getFacturasPendientes(): Response<List<FacturaDTO>>

    /**
     * Marca una factura como pagada por su ID.
     * @param facturaId ID de la factura a marcar como pagada.
     * @return Respuesta vacía (Void).
     */
    @PUT("/api/facturas/{facturaId}/pagar")
    suspend fun pagarFactura(
        @Path("facturaId") facturaId: Long
    ): Response<Void>
}