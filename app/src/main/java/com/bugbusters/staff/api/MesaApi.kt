package com.bugbusters.staff.network

import com.bugbusters.staff.dto.MesaDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Interfaz que define las operaciones de red para gestionar mesas
 * mediante Retrofit.
 *
 * Contiene métodos para obtener, crear y eliminar mesas a través
 * de peticiones HTTP hacia la API REST.
 */
interface MesaApi {

    /**
     * Obtiene la lista completa de mesas desde el servidor.
     *
     * @return [Response] que contiene una lista de [MesaDTO] en caso de éxito.
     */
    @GET("mesas")
    suspend fun getMesas(): Response<List<MesaDTO>>

    /**
     * Crea una nueva mesa en el servidor.
     *
     * @param mesa Objeto [MesaDTO] con los datos de la mesa a crear.
     * @return [Response] que contiene la mesa creada, con su ID asignada.
     */
    @POST("mesas")
    suspend fun crearMesa(@Body mesa: MesaDTO): Response<MesaDTO>

    /**
     * Elimina una mesa existente en el servidor.
     *
     * @param id ID de la mesa a eliminar.
     * @return [Response] sin contenido ([Void]) que indica el estado de la operación.
     */
    @DELETE("mesas/{id}")
    suspend fun eliminarMesa(@Path("id") id: Long): Response<Void>
}