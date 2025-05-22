package com.bugbusters.staff.dto

import java.time.LocalDateTime

data class FacturaDTO(
    val id: Long,
    val pedidoIds: List<Long>,
    val total: Double,
    val fecha: String,
    val estado: String
)
