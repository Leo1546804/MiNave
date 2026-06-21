package com.example.minave.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.minave.databinding.ItemLavadaBinding
import com.example.minave.modelos.Lavada

class LavadaAdapter(
    private var listaLavadas: List<Lavada>,
    private val alDarClickOpciones: (Lavada, String) -> Unit
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
            tvObservacionesLavada.text = lavada.observaciones.ifEmpty { "Servicio de limpieza vehicular" }

            btnMenuOpcionesLavada.setOnClickListener { vista ->
                val popup = PopupMenu(vista.context, vista)
                popup.menu.add("Editar")
                popup.menu.add("Eliminar")
                
                popup.setOnMenuItemClickListener { item ->
                    alDarClickOpciones(lavada, item.title.toString())
                    true
                }
                popup.show()
            }
        }
    }

    override fun getItemCount(): Int = listaLavadas.size

    fun actualizarLista(nuevaLista: List<Lavada>) {
        listaLavadas = nuevaLista
        notifyDataSetChanged()
    }
}
