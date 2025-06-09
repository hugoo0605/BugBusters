package com.bugbusters.staff.api

import com.bugbusters.staff.dto.ItemPedidoDTO
import com.bugbusters.staff.dto.PedidoDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interfaz que define las operaciones de red para gestionar pedidos y sus items
 * mediante Retrofit.
 *
 * Incluye métodos para obtener pedidos activos, consultar items de un pedido,
 * y actualizar estados de pedidos e items.
 */
interface PedidoApi {

    /**
     * Obtiene todos los pedidos activos desde el servidor.
     *
     * @return [Call] con una lista de [PedidoDTO] en caso de éxito.
     */
    @GET("pedidos/activos")
    fun obtenerPedidosActivos(): Call<List<PedidoDTO>>

    /**
     * Obtiene los items pertenecientes a un pedido específico.
     *
     * @param pedidoId ID del pedido cuyos items se desean consultar.
     * @return [Call] con una lista de [ItemPedidoDTO] en caso de éxito.
     */
    @GET("pedidos/{pedidoId}/items")
    fun obtenerItemsDePedido(@Path("pedidoId") pedidoId: Long): Call<List<ItemPedidoDTO>>

    /**
     * Actualiza el estado de un item de pedido.
     *
     * @param itemId ID del item de pedido a actualizar.
     * @param nuevoEstado Cadena con el nuevo estado que se asignará al item.
     * @return [Call] sin contenido ([Void]) que indica el resultado de la operación.
     */
    @PATCH("item_pedido/{id}/estado")
    fun actualizarEstadoItem(
        @Path("id") itemId: Long,
        @Body nuevoEstado: String
    ): Call<Void>

    /**
     * Actualiza el estado de un pedido completo.
     *
     * @param pedidoId ID del pedido a actualizar.
     * @param estado Nuevo estado que se asignará al pedido, enviado como query parameter.
     * @return [Call] sin contenido ([Void]) que indica el resultado de la operación.
     */
    @PUT("pedidos/{id}")
    fun actualizarEstadoPedido(
        @Path("id") pedidoId: Long,
        @Query("estado") estado: String
    ): Call<Void>
}