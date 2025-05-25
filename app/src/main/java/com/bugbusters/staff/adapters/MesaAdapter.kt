package com.bugbusters.staff.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.bugbusters.staff.R
import com.bugbusters.staff.dto.MesaDTO // Asegúrate de que esté el paquete correcto

class MesaAdapter(
    private val onMesaClick: (Long) -> Unit
) : RecyclerView.Adapter<MesaAdapter.MesaViewHolder>() {

    private val mesas: MutableList<MesaDTO> = mutableListOf()

    inner class MesaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnMesa: Button = itemView.findViewById(R.id.btnMesa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MesaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mesa, parent, false)
        return MesaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MesaViewHolder, position: Int) {
        val mesa = mesas[position]
        holder.btnMesa.text = "Mesa ${mesa.numero}"
        holder.btnMesa.setOnClickListener {
            onMesaClick(mesa.id ?: -1)
        }
    }

    override fun getItemCount(): Int = mesas.size

    fun actualizarMesas(nuevas: List<MesaDTO>) {
        mesas.clear()
        mesas.addAll(nuevas)
        notifyDataSetChanged()
    }
}