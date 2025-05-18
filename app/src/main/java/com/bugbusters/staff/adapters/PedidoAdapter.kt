package com.bugbusters.staff.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bugbusters.staff.databinding.ItemPedidoBinding
import com.bugbusters.staff.dto.PedidoDTO

class PedidoAdapter(private val pedidos: List<PedidoDTO>) :
    RecyclerView.Adapter<PedidoAdapter.PedidoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val binding = ItemPedidoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PedidoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        holder.bind(pedidos[position])
    }

    override fun getItemCount(): Int = pedidos.size

    class PedidoViewHolder(private val binding: ItemPedidoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pedido: PedidoDTO) {
            binding.tvPedidoId.text = "Pedido #${pedido.id}"
            binding.tvEstado.text = "Estado: ${pedido.estado}"
        }
    }
}
