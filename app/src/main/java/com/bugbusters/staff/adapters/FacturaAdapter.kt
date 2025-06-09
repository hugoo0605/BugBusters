package com.bugbusters.staff.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bugbusters.staff.R
import com.bugbusters.staff.dto.FacturaDTO

/**
 * Adapter para mostrar una lista de facturas en un RecyclerView.
 * Permite actualizar la lista y manejar la acción de marcar una factura como pagada.
 *
 * @param facturas Lista mutable de facturas a mostrar.
 * @param onPagadaClick Callback que se ejecuta al pulsar el botón "Marcar Pagada" de una factura.
 */
class FacturaAdapter(
    private val facturas: MutableList<FacturaDTO>,
    private val onPagadaClick: (FacturaDTO) -> Unit
) : RecyclerView.Adapter<FacturaAdapter.FacturaViewHolder>() {

    /**
     * Crea un ViewHolder para el item factura inflando su layout.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacturaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_factura, parent, false)
        return FacturaViewHolder(view)
    }

    /**
     * Enlaza los datos de la factura con la vista.
     */
    override fun onBindViewHolder(holder: FacturaViewHolder, position: Int) {
        val factura = facturas[position]
        holder.bind(factura)
    }

    /**
     * Retorna el número total de facturas en la lista.
     */
    override fun getItemCount(): Int = facturas.size

    /**
     * Actualiza la lista de facturas y notifica al RecyclerView.
     *
     * @param nuevas Nueva lista de facturas a mostrar.
     */
    fun actualizarLista(nuevas: List<FacturaDTO>) {
        facturas.clear()
        facturas.addAll(nuevas)
        notifyDataSetChanged()
    }

    /**
     * ViewHolder que representa un item de factura.
     *
     * @property itemView Vista del item.
     */
    inner class FacturaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvId: TextView = itemView.findViewById(R.id.tvFacturaId)
        private val tvFecha: TextView = itemView.findViewById(R.id.tvFacturaFecha)
        private val tvTotal: TextView = itemView.findViewById(R.id.tvFacturaTotal)
        private val btnPagada: Button = itemView.findViewById(R.id.btnMarcarPagada)

        /**
         * Vincula la información de la factura a las vistas y configura el botón de marcar como pagada.
         *
         * @param factura Factura a mostrar.
         */
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