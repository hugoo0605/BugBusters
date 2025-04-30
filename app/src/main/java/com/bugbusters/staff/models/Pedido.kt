package com.bugbusters.staff.models
data class Pedido(
    val id: Int,
    val mesaId: Int,
    val productos: List<ProductoPedido>,
    val estado: String
)
