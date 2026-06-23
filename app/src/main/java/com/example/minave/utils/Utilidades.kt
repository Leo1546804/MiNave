package com.example.minave.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.minave.databinding.DialogoConfirmacionBinding

object Utilidades {

    fun mostrarDialogoConfirmacion(
        contexto: Context,
        titulo: String,
        mensaje: String,
        textoBotonConfirmar: String = "Eliminar",
        colorBoton: Int? = null,
        accionConfirmar: () -> Unit
    ) {
        val inflater = LayoutInflater.from(contexto)
        val binding = DialogoConfirmacionBinding.inflate(inflater)

        val builder = AlertDialog.Builder(contexto)
        builder.setView(binding.root)
        val dialogo = builder.create()

        dialogo.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.tvTituloDialogo.text = titulo
        binding.tvMensajeDialogo.text = mensaje
        binding.btnConfirmarDialogo.text = textoBotonConfirmar

        colorBoton?.let {
            binding.btnConfirmarDialogo.backgroundTintList = ContextCompat.getColorStateList(contexto, it)
        }

        binding.btnCancelarDialogo.setOnClickListener {
            dialogo.dismiss()
        }

        binding.btnConfirmarDialogo.setOnClickListener {
            accionConfirmar()
            dialogo.dismiss()
        }

        dialogo.show()
    }
}