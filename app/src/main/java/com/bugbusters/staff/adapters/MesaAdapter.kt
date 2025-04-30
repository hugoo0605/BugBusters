package com.bugbusters.staff.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bugbusters.staff.databinding.ItemMesaBinding
import com.bugbusters.staff.models.Mesa

class MesaAdapter(
    private val mesas: List<Mesa>,
    private val onItemClick: (Mesa) -> Unit
) : RecyclerView.Adapter<MesaAdapter.MesaViewHolder>() {

    inner class MesaViewHolder(val binding: ItemMesaBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MesaViewHolder {
        val binding = ItemMesaBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MesaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MesaViewHolder, position: Int) {
        val mesa = mesas[position]
        holder.binding.tvNumeroMesa.text = mesa.numero
        holder.binding.tvEstadoMesa.text = mesa.estado

        holder.itemView.setOnClickListener { onItemClick(mesa) }
    }

    override fun getItemCount() = mesas.size
}