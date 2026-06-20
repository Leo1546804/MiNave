package com.example.minave.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.minave.databinding.ItemCertificadoBinding
import com.example.minave.modelos.Certificado

class CertificadoAdapter(
    private val listaCertificados: List<Certificado>,
    private val alClickOpciones: (Certificado, String) -> Unit
) : RecyclerView.Adapter<CertificadoAdapter.CertificadoViewHolder>() {

    class CertificadoViewHolder(val binding: ItemCertificadoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CertificadoViewHolder {
        val binding = ItemCertificadoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CertificadoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CertificadoViewHolder, position: Int) {
        val certificado = listaCertificados[position]

        holder.binding.tvTipoDocumento.text = certificado.tipoDocumento
        holder.binding.tvEmpresa.text = certificado.empresaEmisora
        holder.binding.tvVencimiento.text = "Vence: ${certificado.fechaVencimiento}"

        holder.binding.btnMenuOpciones.setOnClickListener { vista ->
            val menu = PopupMenu(vista.context, vista)
            menu.menu.add("Editar")
            menu.menu.add("Eliminar")

            menu.setOnMenuItemClickListener { item ->
                alClickOpciones(certificado, item.title.toString())
                true
            }
            menu.show()
        }
    }

    override fun getItemCount(): Int = listaCertificados.size
}
