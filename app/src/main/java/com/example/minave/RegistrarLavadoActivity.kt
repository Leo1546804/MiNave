package com.example.minave

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.minave.databinding.ActivityRegistrarLavadoBinding
import com.example.minave.modelos.Lavada
import com.example.minave.repositorio.LavadaRepository
import java.util.Calendar

class RegistrarLavadoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarLavadoBinding
    private lateinit var repositorio: LavadaRepository
    private var modoEdicion = false
    private var idLavadaEditar = -1
    private var idVehiculoRelacionado = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarLavadoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repositorio = LavadaRepository(this)

        val bundle = intent.extras
        if (bundle != null) {
            modoEdicion = bundle.getBoolean("modo_edicion", false)
            if (modoEdicion) {
                binding.txtTituloLavado.text = "Editar Lavado"
                binding.btnGuardarLavado.text = "Actualizar Registro"
                idLavadaEditar = bundle.getInt("id_lavada", -1)
                idVehiculoRelacionado = bundle.getInt("id_vehiculo", -1)
                
                binding.campoTipoLavado.setText(bundle.getString("tipo", ""))
                binding.campoLugarLavado.setText(bundle.getString("lugar", ""))
                binding.campoFechaLavado.setText(bundle.getString("fecha", ""))
                binding.campoCostoLavado.setText(bundle.getDouble("costo", 0.0).toString())
                binding.campoObservacionesLavado.setText(bundle.getString("observaciones", ""))
            }
        }

        binding.campoFechaLavado.setOnClickListener { mostrarSelectorFecha(binding.campoFechaLavado) }

        binding.btnVolverLavado.setOnClickListener {
            finish()
        }

        binding.btnGuardarLavado.setOnClickListener {
            guardarDatos()
        }
    }

    private fun mostrarSelectorFecha(editText: EditText) {
        val calendario = Calendar.getInstance()
        val anio = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        val selector = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val fechaSeleccionada = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
            editText.setText(fechaSeleccionada)
        }, anio, mes, dia)

        selector.show()
    }

    private fun guardarDatos() {
        val tipo = binding.campoTipoLavado.text.toString()
        val lugar = binding.campoLugarLavado.text.toString()
        val fecha = binding.campoFechaLavado.text.toString()
        val costoStr = binding.campoCostoLavado.text.toString()
        val observaciones = binding.campoObservacionesLavado.text.toString()

        if (tipo.isNotEmpty() && costoStr.isNotEmpty()) {
            val lavada = Lavada(
                id = if (modoEdicion) idLavadaEditar else 0,
                idVehiculo = idVehiculoRelacionado,
                fecha = fecha,
                tipo = tipo,
                lugar = lugar,
                costo = costoStr.toDoubleOrNull() ?: 0.0,
                observaciones = observaciones
            )

            val exito = if (modoEdicion) {
                repositorio.actualizar(lavada)
            } else {
                repositorio.insertar(lavada) > 0
            }

            if (exito) {
                val mensaje = if (modoEdicion) "Lavado actualizado con éxito" else "Lavado registrado con éxito"
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error: Selecciona un vehículo en Inicio primero", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Completa el tipo y el costo", Toast.LENGTH_SHORT).show()
        }
    }
}
