package com.bugbusters.staff.dto

data class FacturaDTO(
    val id: Long,
    val estado: String,
    val fecha: String,
    val montoTotal: Double,
    val pedidos: List<Long>
)