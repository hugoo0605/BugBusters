package com.bugbusters.staff.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bugbusters.staff.databinding.ItemProductoBinding
import com.bugbusters.staff.dto.ItemPedidoDTO

/**
 * Adapter para mostrar y gestionar una lista de items de pedido en un RecyclerView.
 * Permite visualizar la información de cada producto, su estado, y cambiar el estado mediante un Spinner.
 *
 * @param items Lista de objetos ItemPedidoDTO que representan los productos del pedido.
 */
class ItemPedidoAdapter(
    private val items: List<ItemPedidoDTO>
) : RecyclerView.Adapter<ItemPedidoAdapter.ItemViewHolder>() {

    // Lista fija de estados posibles para un item de pedido
    private val estados = listOf("PENDIENTE", "PREPARACION", "LISTO", "ENTREGADO", "CANCELADO")

    /**
     * ViewHolder que contiene las vistas de un item de producto usando View Binding.
     *
     * @property binding Binding generado para el layout item_producto.xml
     */
    inner class ItemViewHolder(val binding: ItemProductoBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * Infla el layout del item y crea el ViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemProductoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    /**
     * Vincula los datos del item de pedido a las vistas, incluyendo el estado y la nota.
     * Configura el spinner para cambiar el estado y actualiza el objeto en consecuencia.
     */
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

        // Listener para actualizar el estado en el objeto cuando se selecciona otro valor
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

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // No se requiere acción aquí
                }
            }
    }

    /**
     * Retorna la cantidad de items en la lista.
     */
    override fun getItemCount(): Int = items.size

    /**
     * Cambia el estado de todos los items a "ENTREGADO" y notifica al RecyclerView.
     */
    fun marcarTodosComoFinalizado() {
        for (item in items) {
            item.estado = "ENTREGADO"
        }
        notifyDataSetChanged()
    }

    /**
     * Devuelve la lista de items actualizada con los estados modificados.
     */
    fun getItemsActualizados(): List<ItemPedidoDTO> = items
}