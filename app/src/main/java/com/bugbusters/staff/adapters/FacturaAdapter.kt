package com.bugbusters.staff.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bugbusters.staff.R
import com.bugbusters.staff.dto.FacturaDTO

class FacturaAdapter(
    private val facturas: MutableList<FacturaDTO>,
    private val onPagadaClick: (FacturaDTO) -> Unit
) : RecyclerView.Adapter<FacturaAdapter.FacturaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacturaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_factura, parent, false)
        return FacturaViewHolder(view)
    }

    override fun onBindViewHolder(holder: FacturaViewHolder, position: Int) {
        val factura = facturas[position]
        holder.bind(factura)
    }

    override fun getItemCount(): Int = facturas.size

    fun actualizarLista(nuevas: List<FacturaDTO>) {
        facturas.clear()
        facturas.addAll(nuevas)
        notifyDataSetChanged()
    }

    inner class FacturaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvId: TextView = itemView.findViewById(R.id.tvFacturaId)
        private val tvFecha: TextView = itemView.findViewById(R.id.tvFacturaFecha)
        private val tvTotal: TextView = itemView.findViewById(R.id.tvFacturaTotal)
        private val btnPagada: Button = itemView.findViewById(R.id.btnMarcarPagada)

        fun bind(factura: FacturaDTO) {
            tvId.text = "ID: ${factura.id}"
            tvFecha.text = "Fecha: ${factura.fecha}"
            tvTotal.text = "Total: ${factura.total} €"

            btnPagada.setOnClickListener {
                onPagadaClick(factura)
            }
        }
    }
}
