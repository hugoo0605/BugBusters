package com.bugbusters.staff.dto

data class FacturaDTO(
    val id: Long,
    val pedidoIds: List<Long>,
    val total: Double,
    val fecha: String,
    val estado: String
)
