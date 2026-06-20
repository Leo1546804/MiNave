package com.example.minave.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.minave.databinding.ItemMantenimientoBinding
import com.example.minave.modelos.Mantenimiento
import java.util.Locale

class MantenimientoAdapter(
    private var listaMantenimientos: List<Mantenimiento>,
    private val alEditar: (Mantenimiento) -> Unit,
    private val alEliminar: (Mantenimiento) -> Unit
) : RecyclerView.Adapter<MantenimientoAdapter.MantenimientoViewHolder>() {

    inner class MantenimientoViewHolder(val binding: ItemMantenimientoBinding) : 
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MantenimientoViewHolder {
        val binding = ItemMantenimientoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MantenimientoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MantenimientoViewHolder, position: Int) {
        val mantenimiento = listaMantenimientos[position]
        
        with(holder.binding) {
            txtItemTipo.text = mantenimiento.tipoMantenimiento
            txtItemTaller.text = mantenimiento.taller
            txtItemFecha.text = mantenimiento.fecha
            txtItemCosto.text = String.format(Locale.getDefault(), "S/ %.2f", mantenimiento.costo)
            txtItemKm.text = "Km: ${mantenimiento.kilometraje}"
            txtItemProximoKm.text = "Próximo: ${mantenimiento.proximoKilometraje}"

            btnMenuOpcionesMantenimiento.setOnClickListener { vista ->
                mostrarMenu(vista, mantenimiento)
            }
        }
    }

    private fun mostrarMenu(view: View, mantenimiento: Mantenimiento) {
        val popup = PopupMenu(view.context, view)
        popup.menu.add("Editar")
        popup.menu.add("Eliminar")
        
        popup.setOnMenuItemClickListener { item ->
            when (item.title) {
                "Editar" -> {
                    alEditar(mantenimiento)
                    true
                }
                "Eliminar" -> {
                    alEliminar(mantenimiento)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    override fun getItemCount(): Int = listaMantenimientos.size

    fun actualizarLista(nuevaLista: List<Mantenimiento>) {
        listaMantenimientos = nuevaLista
        notifyDataSetChanged()
    }
}
