package com.bugbusters.staff.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://bugbustersspring.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val facturaApi: FacturaApi by lazy {
        retrofit.create(FacturaApi::class.java)
    }

    val mesaApi: MesaApi by lazy {
        retrofit.create(MesaApi::class.java)
    }
}