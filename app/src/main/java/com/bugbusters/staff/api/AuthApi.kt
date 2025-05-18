package com.bugbusters.staff.api

import retrofit2.Call
import com.bugbusters.staff.dto.LoginRequest
import com.bugbusters.staff.dto.TrabajadorDTO
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<TrabajadorDTO>  // o ResponseBody si devuelves solo OK
}
