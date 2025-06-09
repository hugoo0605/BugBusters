package com.bugbusters.staff.adapters

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.bugbusters.staff.R
import com.bugbusters.staff.dto.MesaDTO

/**
 * Adaptador para la lista de mesas en un RecyclerView.
 *
 * Permite alternar entre un modo normal (selección de mesa)
 * y un modo de eliminación (donde las mesas tiemblan y pueden eliminarse).
 *
 * @property onMesaClick Lambda que se ejecuta al pulsar una mesa en modo normal.
 * @property onDeleteMesa Lambda que se ejecuta al confirmar la eliminación de una mesa.
 * @property isDeleteMode Indica si está activo el modo eliminación.
 */
class MesaAdapter(
    private val onMesaClick: (Long) -> Unit,
    private val onDeleteMesa: (Long) -> Unit,
    private var isDeleteMode: Boolean = false
) : RecyclerView.Adapter<MesaAdapter.MesaViewHolder>() {

    // Lista de mesas que se muestran en el RecyclerView
    private val mesas: MutableList<MesaDTO> = mutableListOf()

    /**
     * Activa o desactiva el modo de eliminación.
     *
     * @param enable true para activar modo eliminación, false para desactivarlo.
     */
    fun setDeleteMode(enable: Boolean) {
        isDeleteMode = enable
        notifyDataSetChanged() // Actualiza la lista para reflejar el cambio de modo
    }

    /**
     * ViewHolder que representa cada mesa en la lista.
     *
     * @param itemView Vista inflada del layout de cada item.
     */
    inner class MesaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Botón que representa la mesa
        val btnMesa: Button = itemView.findViewById(R.id.btnMesa)
    }

    /**
     * Asocia los datos de una mesa con su ViewHolder.
     *
     * @param holder ViewHolder que se va a configurar.
     * @param position Posición del elemento en la lista.
     */
    override fun onBindViewHolder(holder: MesaViewHolder, position: Int) {
        val mesa = mesas[position]

        // Configura el texto del botón con el número de mesa
        holder.btnMesa.text = "Mesa ${mesa.numero}"

        if (isDeleteMode) {
            // Si está en modo eliminación:
            // Aplica animación de "temblor"
            holder.btnMesa.startAnimation(
                AnimationUtils.loadAnimation(holder.itemView.context, R.anim.shake)
            )
            // Configura el clic para mostrar confirmación de eliminación
            holder.btnMesa.setOnClickListener {
                showDeleteConfirmation(holder.itemView.context, mesa.id ?: -1)
            }
        } else {
            // Si no está en modo eliminación:
            // Quita animación por si la tuviera
            holder.btnMesa.clearAnimation()
            // Configura el clic para ejecutar la acción normal
            holder.btnMesa.setOnClickListener {
                onMesaClick(mesa.id ?: -1)
            }
        }
    }

    /**
     * Muestra un diálogo de confirmación antes de eliminar una mesa.
     *
     * @param context Contexto de la vista.
     * @param mesaId ID de la mesa a eliminar.
     */
    private fun showDeleteConfirmation(context: Context, mesaId: Long) {
        AlertDialog.Builder(context)
            .setTitle("Eliminar mesa")
            .setMessage("¿Estás seguro de querer eliminar esta mesa?")
            .setPositiveButton("Eliminar") { _, _ ->
                onDeleteMesa(mesaId)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    /**
     * Actualiza la lista de mesas mostrada en el RecyclerView.
     *
     * @param nuevas Lista nueva de mesas.
     */
    fun actualizarMesas(nuevas: List<MesaDTO>) {
        mesas.clear()
        mesas.addAll(nuevas)
        notifyDataSetChanged()
    }

    /**
     * Devuelve el número total de elementos en la lista.
     */
    override fun getItemCount() = mesas.size

    /**
     * Crea una nueva instancia de ViewHolder inflando el layout correspondiente.
     *
     * @param parent Vista padre.
     * @param viewType Tipo de vista (no se usa en este caso).
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MesaViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_mesa, parent, false)
    )
}