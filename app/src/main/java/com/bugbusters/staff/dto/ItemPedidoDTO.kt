package com.bugbusters.staff.dto

data class ItemPedidoDTO(
    val id: Long,
    val cantidad: Int,
    val precioUnitario: Double,
    val estado: String,
    val notas: String?,
    val nombreProducto: String
)
