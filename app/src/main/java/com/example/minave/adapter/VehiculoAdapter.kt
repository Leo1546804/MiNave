package com.example.minave.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.minave.databinding.ItemVehiculoBinding
import com.example.minave.modelos.Vehiculo

class VehiculoAdapter(
    private val listaVehiculos: List<Vehiculo>,
    private val alDarClickOpciones: (Vehiculo, String) -> Unit // Callback para comunicar acciones al Fragment
) : RecyclerView.Adapter<VehiculoAdapter.VehiculoViewHolder>() {

    class VehiculoViewHolder(val vinculoItem: ItemVehiculoBinding) : RecyclerView.ViewHolder(vinculoItem.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehiculoViewHolder {
        val binding = ItemVehiculoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VehiculoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VehiculoViewHolder, position: Int) {
        val nave = listaVehiculos[position]

        holder.vinculoItem.txtMarcaModelo.text = "${nave.marca} ${nave.modelo}"
        holder.vinculoItem.txtPlaca.text = nave.placa.uppercase()
        holder.vinculoItem.txtDetalles.text = "Año: ${nave.anio} • Color: ${nave.color} • ${nave.tipoCombustible}"

        //Al mantener presionado el elemento, abrimos las opciones
        holder.itemView.setOnLongClickListener { vista ->
            val menuFlotante = PopupMenu(vista.context, vista)
            menuFlotante.menu.add("Editar")
            menuFlotante.menu.add("Eliminar")

            menuFlotante.setOnMenuItemClickListener { item ->
                // le avisamos al fragment que auto se tocó y que se desea hacer
                alDarClickOpciones(nave, item.title.toString())
                true
            }
            menuFlotante.show()
            true
        }
    }

    override fun getItemCount(): Int {
        return listaVehiculos.size
    }
}