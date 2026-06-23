package com.example.minave.ui.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.minave.databinding.ActivityRegistrarCombustibleBinding
import com.example.minave.modelos.Combustible
import com.example.minave.repositorio.CombustibleRepository
import java.util.Calendar

class RegistrarCombustibleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarCombustibleBinding
    private lateinit var combustibleRepo: CombustibleRepository
    private var modoEdicion = false
    private var idRegistroAEditar = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegistrarCombustibleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        combustibleRepo = CombustibleRepository(this)

        // Configurar selección de fecha
        binding.campoFecha.setOnClickListener { mostrarDatePicker() }

        modoEdicion = intent.getBooleanExtra("modo_edicion", false)

        if (modoEdicion) {
            idRegistroAEditar = intent.getIntExtra("id_registro", -1)
            binding.campoFecha.setText(intent.getStringExtra("fecha"))
            binding.campoLitros.setText(intent.getDoubleExtra("litros", 0.0).toString())
            binding.campoCosto.setText(intent.getDoubleExtra("costo", 0.0).toString())
            binding.campoObservaciones.setText(intent.getStringExtra("observaciones"))

            binding.btnGuardarCombustible.text = "Actualizar Registro"
            binding.txtTituloCombustible.text = "Modificar Tanqueo"
        }

        binding.btnVolverCombustible.setOnClickListener { finish() }

        binding.btnGuardarCombustible.setOnClickListener { guardarDatos() }
    }

    private fun mostrarDatePicker() {
        val calendario = Calendar.getInstance()
        val anio = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        val picker = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            val fechaSeleccionada = "$dayOfMonth/${month + 1}/$year"
            binding.campoFecha.setText(fechaSeleccionada)
        }, anio, mes, dia)

        picker.show()
    }

    private fun guardarDatos() {
        val fecha = binding.campoFecha.text.toString().trim()
        val litrosTexto = binding.campoLitros.text.toString().trim()
        val costoTexto = binding.campoCosto.text.toString().trim()
        val observaciones = binding.campoObservaciones.text.toString().trim()

        if (fecha.isEmpty() || litrosTexto.isEmpty() || costoTexto.isEmpty()) {
            Toast.makeText(this, "Por favor completa los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val preferencias = getSharedPreferences("SesionUsuario", MODE_PRIVATE)
        val idVehiculoActivo = preferencias.getInt("id_vehiculo_activo", -1)

        if (idVehiculoActivo == -1) {
            Toast.makeText(this, "Error: No hay un vehículo activo seleccionado", Toast.LENGTH_SHORT).show()
            return
        }

        val registro = Combustible(
            id = if (modoEdicion) idRegistroAEditar else null,
            idVehiculo = idVehiculoActivo,
            fecha = fecha,
            litros = litrosTexto.toDouble(),
            costo = costoTexto.toDouble(),
            observaciones = observaciones
        )

        val exito = if (modoEdicion) {
            combustibleRepo.actualizarCombustible(registro)
        } else {
            combustibleRepo.insertarCombustible(registro) != -1L
        }

        if (exito) {
            val mensaje = if (modoEdicion) "¡Registro actualizado con éxito!" else "¡Registro guardado con éxito!"
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al guardar en la base de datos", Toast.LENGTH_SHORT).show()
        }
    }
}