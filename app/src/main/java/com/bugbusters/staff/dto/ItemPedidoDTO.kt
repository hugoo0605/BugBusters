package com.bugbusters.staff.dto

data class ItemPedidoDTO(
    val id: Long,
    val cantidad: Int,
    val precioUnitario: Double,
    var estado: String,
    val notas: String?,
    val nombreProducto: String
)
