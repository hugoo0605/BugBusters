package com.bugbusters.staff.adapters

import com.bugbusters.staff.models.ProductoPedido
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bugbusters.staff.databinding.ItemPedidoBinding


class PedidoAdapter(private val productos: List<ProductoPedido>) :
    RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>() {

    inner class PedidoViewHolder(val binding: ItemPedidoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val binding = ItemPedidoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PedidoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val producto = productos[position]
        holder.binding.tvNombreProducto.text = producto.nombre
        holder.binding.tvCantidad.text = "Cantidad: ${producto.cantidad}"
        holder.binding.tvPrecio.text = "Precio: ${producto.precio}€"
    }

    override fun getItemCount() = productos.size
}