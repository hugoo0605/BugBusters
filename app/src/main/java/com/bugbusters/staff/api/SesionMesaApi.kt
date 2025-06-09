package com.bugbusters.staff.api

import com.bugbusters.staff.dto.SesionMesaDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Interfaz que define las operaciones de red relacionadas con las sesiones de mesas,
 * utilizando Retrofit.
 *
 * Permite consultar las sesiones asociadas a una mesa específica.
 */
interface SesionMesaApi {

    /**
     * Obtiene todas las sesiones asociadas a una mesa específica.
     *
     * @param mesaId ID de la mesa cuyas sesiones se desean consultar.
     * @return [Response] con una lista de [SesionMesaDTO] en caso de éxito.
     */
    @GET("sesiones/mesa/{mesaId}")
    suspend fun obtenerSesionesPorMesa(@Path("mesaId") mesaId: Long): Response<List<SesionMesaDTO>>
}