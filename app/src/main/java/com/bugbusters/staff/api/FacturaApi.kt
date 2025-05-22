package com.bugbusters.staff.network

import com.bugbusters.staff.dto.FacturaDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface FacturaApi {
    @POST("/api/facturas/generar/{pedidoId}")
    suspend fun generarFacturaPorPedido(
        @Path("pedidoId") pedidoId: Long
    ): Response<FacturaDTO>

    @POST("/api/facturas/mesa/{mesaId}")
    suspend fun generarFacturaPorMesa(
        @Path("mesaId") mesaId: Long
    ): Response<FacturaDTO>

    @GET("/api/facturas/pendientes")
    suspend fun getFacturasPendientes(): Response<List<FacturaDTO>>

    // 2) Marcar una factura como pagada
    @PUT("/api/facturas/{facturaId}/pagar")
    suspend fun pagarFactura(
        @Path("facturaId") facturaId: Long
    ): Response<Void>
}