package com.example.minave.ui.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.minave.databinding.ActivityRegistrarCertificadoBinding
import com.example.minave.modelos.Certificado
import com.example.minave.repositorio.CertificadoRepository
import java.util.Calendar

class RegistrarCertificadoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarCertificadoBinding
    private lateinit var repositorio: CertificadoRepository
    private var modoEdicion = false
    private var idCertificadoEditar = -1
    private var idVehiculoRelacionado = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarCertificadoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repositorio = CertificadoRepository(this)


        val bundle = intent.extras
        if (bundle != null) {
            modoEdicion = bundle.getBoolean("modo_edicion", false)
            if (modoEdicion) {
                binding.txtTituloCertificado.text = "Editar Certificado"
                binding.btnGuardarCertificado.text = "Actualizar Registro"
                idCertificadoEditar = bundle.getInt("id_certificado", -1)
                idVehiculoRelacionado = bundle.getInt("id_vehiculo", -1)

                binding.campoTipoDocumento.setText(bundle.getString("tipo", ""))
                binding.campoEmpresaEmisora.setText(bundle.getString("empresa", ""))
                binding.campoFechaEmision.setText(bundle.getString("emision", ""))
                binding.campoFechaVencimiento.setText(bundle.getString("vencimiento", ""))
                binding.campoCostoCertificado.setText(bundle.getDouble("costo", 0.0).toString())
                binding.campoObservaciones.setText(bundle.getString("observaciones", ""))
            }
        }


        binding.campoFechaEmision.setOnClickListener { mostrarSelectorFecha(binding.campoFechaEmision) }
        binding.campoFechaVencimiento.setOnClickListener { mostrarSelectorFecha(binding.campoFechaVencimiento) }

        binding.btnVolverCertificado.setOnClickListener {
            finish()
        }

        binding.btnGuardarCertificado.setOnClickListener {
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
        val tipo = binding.campoTipoDocumento.text.toString().trim()
        val empresa = binding.campoEmpresaEmisora.text.toString().trim()
        val emision = binding.campoFechaEmision.text.toString().trim()
        val vencimiento = binding.campoFechaVencimiento.text.toString().trim()
        val costoStr = binding.campoCostoCertificado.text.toString().trim()
        val observaciones = binding.campoObservaciones.text.toString().trim()

        if (tipo.isNotEmpty()) {
            val valorCosto = costoStr.toDoubleOrNull() ?: 0.0

            val certificado = Certificado(
                id = if (modoEdicion) idCertificadoEditar else null,
                idVehiculo = idVehiculoRelacionado,
                tipoDocumento = tipo,
                fechaEmision = emision,
                fechaVencimiento = vencimiento,
                empresaEmisora = empresa,
                costo = valorCosto,
                observaciones = observaciones
            )

            val exito = if (modoEdicion) {
                repositorio.actualizar(certificado)
            } else {
                repositorio.insertar(certificado) != -1L
            }

            if (exito) {
                val mensaje = if (modoEdicion) "Certificado actualizado con éxito" else "Certificado registrado con éxito"
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error: Asegúrate de tener un vehículo activo en Inicio", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "El tipo de documento es obligatorio", Toast.LENGTH_SHORT).show()
        }
    }
}