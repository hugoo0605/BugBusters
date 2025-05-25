package com.bugbusters.staff.dto

data class MesaDTO(
    val id: Long? = null,
    val numero: Int,
    val estado: String,
    val capacidad: Int,
    val ubicacion: String
)