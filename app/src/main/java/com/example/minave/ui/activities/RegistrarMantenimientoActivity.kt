package com.example.minave.ui.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.minave.databinding.ActivityRegistrarMantenimientoBinding
import com.example.minave.modelos.Mantenimiento
import com.example.minave.repositorio.MantenimientoRepository
import java.util.Calendar

class RegistrarMantenimientoActivity : AppCompatActivity() {

    private lateinit var vinculo: ActivityRegistrarMantenimientoBinding
    private lateinit var mantenimientoRepo: MantenimientoRepository
    private var modoEdicion = false
    private var idMantenimientoAEditar = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vinculo = ActivityRegistrarMantenimientoBinding.inflate(layoutInflater)
        setContentView(vinculo.root)

        mantenimientoRepo = MantenimientoRepository(this)

        modoEdicion = intent.getBooleanExtra("modo_edicion", false)

        if (modoEdicion) {
            configurarModoEdicion()
        }

        // Listener para el selector de fecha
        vinculo.campoFecha.setOnClickListener { mostrarSelectorFecha() }

        vinculo.btnVolver.setOnClickListener { finish() }

        vinculo.btnGuardarMantenimiento.setOnClickListener { guardarRegistro() }
    }

    private fun configurarModoEdicion() {
        idMantenimientoAEditar = intent.getIntExtra("id", -1)
        vinculo.txtTituloMantenimiento.text = "Editar Mantenimiento"
        vinculo.campoTipo.setText(intent.getStringExtra("tipo"))
        vinculo.campoFecha.setText(intent.getStringExtra("fecha"))
        vinculo.campoKilometraje.setText(intent.getIntExtra("km", 0).toString())
        vinculo.campoProximoKm.setText(intent.getIntExtra("proximo_km", 0).toString())
        vinculo.campoCosto.setText(intent.getDoubleExtra("costo", 0.0).toString())
        vinculo.campoTaller.setText(intent.getStringExtra("taller"))
        vinculo.campoObservaciones.setText(intent.getStringExtra("obs"))
        vinculo.btnGuardarMantenimiento.text = "Actualizar Registro"
    }

    private fun mostrarSelectorFecha() {
        val calendario = Calendar.getInstance()
        val anio = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH)
        val dia = calendario.get(Calendar.DAY_OF_MONTH)

        val selectorFecha = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            // Formateamos la fecha a DD/MM/AAAA para consistencia
            val fechaSeleccionada = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year)
            vinculo.campoFecha.setText(fechaSeleccionada)
        }, anio, mes, dia)

        selectorFecha.show()
    }

    private fun guardarRegistro() {
        val tipo = vinculo.campoTipo.text.toString().trim()
        val fecha = vinculo.campoFecha.text.toString().trim()
        val kmTexto = vinculo.campoKilometraje.text.toString().trim()
        val proximoKmTexto = vinculo.campoProximoKm.text.toString().trim()
        val costoTexto = vinculo.campoCosto.text.toString().trim()
        val taller = vinculo.campoTaller.text.toString().trim()
        val obs = vinculo.campoObservaciones.text.toString().trim()

        if (tipo.isEmpty() || fecha.isEmpty() || kmTexto.isEmpty() || proximoKmTexto.isEmpty() || costoTexto.isEmpty()) {
            Toast.makeText(this, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        val mantenimiento = Mantenimiento(
            id = if (modoEdicion) idMantenimientoAEditar else 0,
            idVehiculo = 0, // El repo asigna el vehículo activo internamente
            tipoMantenimiento = tipo,
            fecha = fecha,
            kilometraje = kmTexto.toInt(),
            proximoKilometraje = proximoKmTexto.toInt(),
            costo = costoTexto.toDouble(),
            taller = taller,
            observaciones = obs
        )

        val exito = if (modoEdicion) {
            mantenimientoRepo.actualizarMantenimiento(mantenimiento) > 0
        } else {
            mantenimientoRepo.insertarMantenimiento(mantenimiento) != -1L
        }

        if (exito) {
            val mensaje = if (modoEdicion) "Mantenimiento actualizado con éxito" else "Mantenimiento registrado con éxito"
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al guardar en la base de datos", Toast.LENGTH_SHORT).show()
        }
    }
}