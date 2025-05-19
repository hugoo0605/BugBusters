package com.bugbusters.staff.api

import com.bugbusters.staff.dto.ItemPedidoDTO
import com.bugbusters.staff.dto.PedidoDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PedidoApi {
    @GET("pedidos/activos")
    fun obtenerPedidosActivos(): Call<List<PedidoDTO>>
    @GET("pedidos/{pedidoId}/items")
    fun obtenerItemsDePedido(@Path("pedidoId") pedidoId: Long): Call<List<ItemPedidoDTO>>

}