package com.example.minave.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.minave.R
import com.example.minave.databinding.ItemLavadaBinding
import com.example.minave.modelos.Lavada

class LavadaAdapter(
    private var listaLavadas: List<Lavada>,
    private val onEliminar: (Int) -> Unit,
    private val onEditar: (Lavada) -> Unit
) : RecyclerView.Adapter<LavadaAdapter.LavadaViewHolder>() {

    class LavadaViewHolder(val binding: ItemLavadaBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LavadaViewHolder {
        val binding = ItemLavadaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LavadaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LavadaViewHolder, position: Int) {
        val lavada = listaLavadas[position]
        holder.binding.apply {
            tvTipoLavado.text = lavada.tipo
            tvLugarLavado.text = lavada.lugar
            tvFechaLavado.text = lavada.fecha
            tvCostoLavado.text = "S/ ${String.format("%.2f", lavada.costo)}"

            btnMenuOpcionesLavada.setOnClickListener {
                mostrarMenu(it, lavada)
            }
        }
    }

    private fun mostrarMenu(view: View, lavada: Lavada) {
        val popup = PopupMenu(view.context, view)
        popup.menu.add("Editar")
        popup.menu.add("Eliminar")
        
        popup.setOnMenuItemClickListener { item ->
            when (item.title) {
                "Editar" -> {
                    onEditar(lavada)
                    true
                }
                "Eliminar" -> {
                    onEliminar(lavada.id)
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    override fun getItemCount(): Int = listaLavadas.size

    fun actualizarLista(nuevaLista: List<Lavada>) {
        listaLavadas = nuevaLista
        notifyDataSetChanged()
    }
}
