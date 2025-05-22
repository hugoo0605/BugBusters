package com.bugbusters.staff.network

import com.bugbusters.staff.dto.FacturaDTO
import retrofit2.Response
import retrofit2.http.POST
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
}