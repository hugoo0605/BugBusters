package com.bugbusters.staff.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bugbusters.staff.databinding.ItemProductoBinding
import com.bugbusters.staff.dto.ItemPedidoDTO

class ItemPedidoAdapter(
    private val items: List<ItemPedidoDTO>
) : RecyclerView.Adapter<ItemPedidoAdapter.ItemViewHolder>() {

    private val estados = listOf("PENDIENTE", "PREPARACION", "LISTO", "ENTREGADO", "CANCELADO")

    inner class ItemViewHolder(val binding: ItemProductoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemProductoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        val context = holder.binding.root.context
        val notaMostrada = if (item.notas.isNullOrBlank()) "Sin notas" else item.notas
        holder.binding.tvNombreProducto.text = item.nombreProducto
        holder.binding.tvCantidad.text = "Cantidad: ${item.cantidad}"
        holder.binding.tvPrecio.text = "Precio: ${item.precioUnitario}€"
        holder.binding.tvEstadoActual.text = "Estado actual: ${item.estado}"
        holder.binding.tvNota.text = "Nota: $notaMostrada"
        val spinnerAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, estados)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        holder.binding.spinnerEstado.adapter = spinnerAdapter

        val index = estados.indexOf(item.estado)
        holder.binding.spinnerEstado.setSelection(if (index >= 0) index else 0)

        holder.binding.spinnerEstado.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    item.estado = estados[pos]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
    }

    override fun getItemCount(): Int = items.size

    fun marcarTodosComoFinalizado() {
        for (item in items) {
            item.estado = "ENTREGADO"
        }
        notifyDataSetChanged()
    }

    fun getItemsActualizados(): List<ItemPedidoDTO> = items
}
