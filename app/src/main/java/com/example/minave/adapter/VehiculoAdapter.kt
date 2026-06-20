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
        holder.vinculoItem.txtColor.text = "Color: ${nave.color}"
        holder.vinculoItem.txtAnio.text = "Año: ${nave.anio}"
        holder.vinculoItem.txtTipoCombustible.text = nave.tipoCombustible

        holder.vinculoItem.btnMenuOpcionesVehiculo.setOnClickListener { vista ->
            val menu = PopupMenu(vista.context, vista)
            menu.menu.add("Editar")
            menu.menu.add("Eliminar")

            menu.setOnMenuItemClickListener { item ->
                alDarClickOpciones(nave, item.title.toString())
                true
            }
            menu.show()
        }
    }

    override fun getItemCount(): Int {
        return listaVehiculos.size
    }
}