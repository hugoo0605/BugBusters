package com.bugbusters.staff.api

import com.bugbusters.staff.dto.PedidoDTO
import retrofit2.Call
import retrofit2.http.GET

interface PedidoApi {
    @GET("pedidos/activos")
    fun obtenerPedidosActivos(): Call<List<PedidoDTO>>
}
