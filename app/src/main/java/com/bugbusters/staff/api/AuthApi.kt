package com.bugbusters.staff.api

import com.bugbusters.staff.dto.LoginRequest
import com.bugbusters.staff.dto.TrabajadorDTO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Interfaz Retrofit para autenticación.
 */
interface AuthApi {
    /**
     * Llama al endpoint de login con los datos de login.
     * @param request Objeto con email y password.
     * @return Call con la respuesta TrabajadorDTO.
     */
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<TrabajadorDTO>

    /**
     * Llama al endpoint de registro con los datos de registro.
     * @param request Mapa con campos de registro.
     * @return Call con la respuesta genérica.
     */
    @POST("auth/register")
    fun register(@Body request: Map<String, String>): Call<ResponseBody>
}