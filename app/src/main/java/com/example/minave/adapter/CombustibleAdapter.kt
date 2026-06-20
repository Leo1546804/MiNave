package com.example.minave.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.minave.databinding.ItemCombustibleBinding
import com.example.minave.modelos.Combustible

class CombustibleAdapter(
    private val listaCombustible: List<Combustible>,
    private val alDarClickOpciones: (Combustible, String) -> Unit
) : RecyclerView.Adapter<CombustibleAdapter.CombustibleViewHolder>() {

    class CombustibleViewHolder(val vinculoItem: ItemCombustibleBinding) : RecyclerView.ViewHolder(vinculoItem.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CombustibleViewHolder {
        val binding = ItemCombustibleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CombustibleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CombustibleViewHolder, position: Int) {
        val registro = listaCombustible[position]

        holder.vinculoItem.txtFecha.text = registro.fecha
        holder.vinculoItem.txtCosto.text = "S/ ${String.format("%.2f", registro.costo)}"
        holder.vinculoItem.txtLitros.text = "${registro.litros} Litros"
        holder.vinculoItem.txtObservaciones.text = registro.observaciones

        holder.itemView.setOnLongClickListener { vista ->
            val menuFlotante = PopupMenu(vista.context, vista)
            menuFlotante.menu.add("Editar")
            menuFlotante.menu.add("Eliminar")

            menuFlotante.setOnMenuItemClickListener { item ->
                alDarClickOpciones(registro, item.title.toString())
                true
            }
            menuFlotante.show()
            true
        }
    }

    override fun getItemCount(): Int = listaCombustible.size
}