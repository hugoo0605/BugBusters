package com.bugbusters.staff.api

import com.bugbusters.staff.dto.ItemPedidoDTO
import com.bugbusters.staff.dto.PedidoDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path

interface PedidoApi {
    @GET("pedidos/activos")
    fun obtenerPedidosActivos(): Call<List<PedidoDTO>>

    @GET("pedidos/{pedidoId}/items")
    fun obtenerItemsDePedido(@Path("pedidoId") pedidoId: Long): Call<List<ItemPedidoDTO>>

    @PATCH("item_pedido/{id}/estado")
    fun actualizarEstadoItem(
        @Path("id") itemId: Long,
        @Body nuevoEstado: String
    ): Call<Void>

    @PUT("pedidos/{id}")
    fun actualizarEstadoPedido(
        @Path("id") pedidoId: Long,
        @retrofit2.http.Query("estado") estado: String
    ): Call<Void>

}