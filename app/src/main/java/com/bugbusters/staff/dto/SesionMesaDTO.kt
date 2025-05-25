package com.bugbusters.staff.dto

import java.util.UUID

data class SesionMesaDTO(
    val id: UUID,
    val fechaApertura: String,
    val fechaCierre: String?,
    val tokenAcceso: String,
    val mesa: MesaDTO
)