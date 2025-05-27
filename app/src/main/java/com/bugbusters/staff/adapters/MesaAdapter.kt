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

class MesaAdapter(
    private val onMesaClick: (Long) -> Unit,
    private val onDeleteMesa: (Long) -> Unit,
    private var isDeleteMode: Boolean = false
) : RecyclerView.Adapter<MesaAdapter.MesaViewHolder>() {

    private val mesas: MutableList<MesaDTO> = mutableListOf()

    fun setDeleteMode(enable: Boolean) {
        isDeleteMode = enable
        notifyDataSetChanged()
    }

    inner class MesaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnMesa: Button = itemView.findViewById(R.id.btnMesa)
    }

    override fun onBindViewHolder(holder: MesaViewHolder, position: Int) {
        val mesa = mesas[position]
        holder.btnMesa.text = "Mesa ${mesa.numero}"

        if (isDeleteMode) {
            // Modo eliminación
            holder.btnMesa.startAnimation(AnimationUtils.loadAnimation(holder.itemView.context, R.anim.shake))
            holder.btnMesa.setOnClickListener {
                showDeleteConfirmation(holder.itemView.context, mesa.id ?: -1)
            }
        } else {
            holder.btnMesa.clearAnimation()
            holder.btnMesa.setOnClickListener {
                onMesaClick(mesa.id ?: -1)
            }
        }
    }

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

    fun actualizarMesas(nuevas: List<MesaDTO>) {
        mesas.clear()
        mesas.addAll(nuevas)
        notifyDataSetChanged()
    }

    override fun getItemCount() = mesas.size
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MesaViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_mesa, parent, false)
    )
}