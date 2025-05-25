package com.bugbusters.staff.network

import com.bugbusters.staff.dto.MesaDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MesaApi {
    @GET("mesas")
    suspend fun getMesas(): Response<List<MesaDTO>>

    @POST("mesas")
    suspend fun crearMesa(@Body mesa: MesaDTO): Response<MesaDTO>
}