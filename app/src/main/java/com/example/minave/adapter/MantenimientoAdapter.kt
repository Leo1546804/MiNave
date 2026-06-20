package com.example.minave.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.minave.databinding.ItemMantenimientoBinding
import com.example.minave.modelos.Mantenimiento

class MantenimientoAdapter(
    private var listaMantenimientos: List<Mantenimiento>,
    private val alHacerClick: (Mantenimiento) -> Unit
) : RecyclerView.Adapter<MantenimientoAdapter.MantenimientoViewHolder>() {

    inner class MantenimientoViewHolder(val vinculo: ItemMantenimientoBinding) : 
        RecyclerView.ViewHolder(vinculo.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MantenimientoViewHolder {
        val inflador = LayoutInflater.from(parent.context)
        val vinculo = ItemMantenimientoBinding.inflate(inflador, parent, false)
        return MantenimientoViewHolder(vinculo)
    }

    override fun onBindViewHolder(holder: MantenimientoViewHolder, position: Int) {
        val mantenimiento = listaMantenimientos[position]
        
        with(holder.vinculo) {
            txtItemTipo.text = mantenimiento.tipoMantenimiento
            txtItemFecha.text = "Fecha: ${mantenimiento.fecha}"
            txtItemCosto.text = "S/ ${String.format("%.2f", mantenimiento.costo)}"
            txtItemKm.text = "KM: ${mantenimiento.kilometraje}"
            txtItemProximoKm.text = "Próximo: ${mantenimiento.proximoKilometraje}"
            txtItemTaller.text = "Taller: ${mantenimiento.taller}"
            txtItemObservaciones.text = "Obs: ${mantenimiento.observaciones}"

            root.setOnClickListener { alHacerClick(mantenimiento) }
        }
    }

    override fun getItemCount(): Int = listaMantenimientos.size

    fun actualizarLista(nuevaLista: List<Mantenimiento>) {
        listaMantenimientos = nuevaLista
        notifyDataSetChanged()
    }
}
