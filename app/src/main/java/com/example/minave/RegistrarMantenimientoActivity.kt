package com.example.minave

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.minave.databinding.ActivityRegistrarMantenimientoBinding
import com.example.minave.modelos.Mantenimiento
import com.example.minave.repositorio.MantenimientoRepository

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

        vinculo.btnVolver.setOnClickListener { finish() }

        vinculo.btnGuardarMantenimiento.setOnClickListener {
            val tipo = vinculo.campoTipo.text.toString().trim()
            val fecha = vinculo.campoFecha.text.toString().trim()
            val kmTexto = vinculo.campoKilometraje.text.toString().trim()
            val proximoKmTexto = vinculo.campoProximoKm.text.toString().trim()
            val costoTexto = vinculo.campoCosto.text.toString().trim()
            val taller = vinculo.campoTaller.text.toString().trim()
            val obs = vinculo.campoObservaciones.text.toString().trim()

            if (tipo.isEmpty() || fecha.isEmpty() || kmTexto.isEmpty() || proximoKmTexto.isEmpty() || costoTexto.isEmpty()) {
                Toast.makeText(this, "Completa los campos obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val mantenimiento = Mantenimiento(
                id = if (modoEdicion) idMantenimientoAEditar else 0,
                idVehiculo = 0, // El repositorio se encarga de asignar el vehículo activo
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
                Toast.makeText(this, "¡Registro guardado!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error al guardar en la base de datos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
