package com.bugbusters.staff.api

import com.bugbusters.staff.dto.FacturaDTO
import retrofit2.Response
import retrofit2.http.POST;
import retrofit2.http.Path;

interface FacturaApi {

    @POST("facturas/factura/mesa/{mesaId}")
    suspend fun generarFacturaPorMesa(
        @Path("mesaId") mesaId: Long
    ): Response<FacturaDTO>
}