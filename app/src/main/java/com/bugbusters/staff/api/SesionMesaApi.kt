package com.bugbusters.staff.api

import com.bugbusters.staff.dto.SesionMesaDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SesionMesaApi {
    @GET("sesiones/mesa/{mesaId}")
    suspend fun obtenerSesionesPorMesa(@Path("mesaId") mesaId: Long): Response<List<SesionMesaDTO>>
    // …otros endpoints…
}