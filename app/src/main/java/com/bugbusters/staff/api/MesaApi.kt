package com.bugbusters.staff.network

import com.bugbusters.staff.dto.MesaDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MesaApi {
    @GET("mesas")
    suspend fun getMesas(): Response<List<MesaDTO>>

    @POST("mesas")
    suspend fun crearMesa(@Body mesa: MesaDTO): Response<MesaDTO>

    @DELETE("mesas/{id}")
    suspend fun eliminarMesa(@Path("id") id: Long): Response<Void>
}