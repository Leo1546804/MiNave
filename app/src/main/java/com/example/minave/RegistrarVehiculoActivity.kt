package com.example.minave


import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.minave.databinding.ActivityRegistrarVehiculoBinding
import com.example.minave.modelos.Vehiculo
import com.example.minave.repositorio.VehiculoRepository

class RegistrarVehiculoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrarVehiculoBinding
    private lateinit var vehiculoRepo: VehiculoRepository
    private var modoEdicion = false
    private var idVehiculoAEditar = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //inicializamos binding oñiooo
        binding = ActivityRegistrarVehiculoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //inicializamos el repositorio de vehiculos
        vehiculoRepo = VehiculoRepository(this)

        modoEdicion = intent.getBooleanExtra("modo_edicion", false)

        if(modoEdicion){
            // Sacamos los datos individuales de la mudanza
            idVehiculoAEditar = intent.getIntExtra("id_vehiculo", -1)
            val placaExtra = intent.getStringExtra("placa") ?: ""
            val marcaExtra = intent.getStringExtra("marca") ?: ""
            val modeloExtra = intent.getStringExtra("modelo") ?: ""
            val anioExtra = intent.getIntExtra("anio", 0)
            val colorExtra = intent.getStringExtra("color") ?: ""
            val combustibleExtra = intent.getStringExtra("combustible") ?: ""

            binding.btnGuardarVehiculo.text = "Actualizar Nave"
            binding.txtTitulo.text = "Modificar nave"

            binding.campoPlaca.setText(placaExtra)
            binding.campoMarca.setText(marcaExtra)
            binding.campoModelo.setText(modeloExtra)
            binding.campoAnio.setText(anioExtra.toString())
            binding.campoColor.setText(colorExtra)
            binding.campoCombustible.setText(combustibleExtra)
        }

        //accion del boton volver
        binding.btnVolverRegistro.setOnClickListener {
            finish()
        }

        binding.btnGuardarVehiculo.setOnClickListener {
            val placa = binding.campoPlaca.text.toString().trim()
            val marca = binding.campoMarca.text.toString().trim()
            val modelo = binding.campoModelo.text.toString().trim()
            val anioTexto = binding.campoAnio.text.toString().trim()
            val color = binding.campoColor.text.toString().trim()
            val combustible = binding.campoCombustible.text.toString().trim()

            if(placa.isEmpty() || marca.isEmpty() || modelo.isEmpty() || anioTexto.isEmpty() || color.isEmpty() || combustible.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            } else{
                val anio = anioTexto.toInt()

                // abrimos el archivo fisco que guarda la sesion
                val preferencias = getSharedPreferences("SesionUsuario", MODE_PRIVATE)
                // si no encuentra el ID, por defecto pondrá -1
                val idUsuarioSesion = preferencias.getInt("id_usuario_conectado", -1)

                if (idUsuarioSesion != -1) {

                    if(modoEdicion){
                        val vehiculoActualizado = Vehiculo(
                            id = idVehiculoAEditar,
                            placa = placa,
                            marca = marca,
                            modelo = modelo,
                            anio = anio,
                            color = color,
                            tipoCombustible = combustible,
                            idUsuario = idUsuarioSesion
                        )

                        val exitoUpdate = vehiculoRepo.actualizarVehiculo(vehiculoActualizado)

                        if(exitoUpdate){
                            Toast.makeText(this, "¡Nave actualizada con éxito!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else{
                            Toast.makeText(this, "Error al actualizar los datos en la Base de Datos", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val nuevoVehiculo = Vehiculo(
                        placa = placa,
                        marca = marca,
                        modelo = modelo,
                        anio = anio,
                        color = color,
                        tipoCombustible = combustible,
                        idUsuario = idUsuarioSesion // el vehículo le pertenece al inspector logueado
                    )

                    val idGenerado = vehiculoRepo.insertarVehiculo(nuevoVehiculo)

                    if(idGenerado != -1L){
                        Toast.makeText(this,"¡Nave registrada con éxito!", Toast.LENGTH_SHORT).show()
                        finish() // Cierra la pantalla y regresa
                    } else {
                        Toast.makeText(this, "Error al guardar el vehículo en la Base de Datos", Toast.LENGTH_SHORT).show()
                    }
                  }
                } else{
                    Toast.makeText(this, "Error: No se detectó la sesion del usuario. Vuelve a iniciar sesión.", Toast.LENGTH_LONG).show()
                }
            }
        }

    }
}