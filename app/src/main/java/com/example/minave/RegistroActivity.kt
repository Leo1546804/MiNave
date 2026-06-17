package com.example.minave

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.minave.databinding.ActivityRegistroBinding
import com.example.minave.modelos.Usuario
import com.example.minave.repositorio.UsuarioRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegistroActivity : AppCompatActivity() {

    private lateinit var vinculoRegistro: ActivityRegistroBinding

    // declaramos el repositorio que creamos en la otra carpeta para poder usar sus metodos
    private lateinit var usuarioRepo: UsuarioRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vinculoRegistro = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(vinculoRegistro.root)

        // inicializamos el repositorio en esta pantalla
        usuarioRepo = UsuarioRepository(this)

        vinculoRegistro.btnRegistrarUsuario.setOnClickListener {
            val nombre = vinculoRegistro.campoNombre.text.toString().trim()
            val apellido = vinculoRegistro.campoApellido.text.toString().trim()
            val correo = vinculoRegistro.campoCorreoRegistro.text.toString().trim()
            val contrasena = vinculoRegistro.campoContrasenaRegistro.text.toString().trim()

            //validamos campos vacios
            if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {

                //Conseguimos la fecha de hoy para guardarla en el modelo
                val formatoFecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val fechaActual = formatoFecha.format(Date())

                // Creamos el objeto USUARIO
                val nuevoUsuario = Usuario(
                    nombre = nombre,
                    apellido = apellido,
                    correo = correo,
                    contrasenia = contrasena,
                    fechaRegistro = fechaActual
                )

                val idGenerado = usuarioRepo.insertarUsuario(nuevoUsuario)

                if(idGenerado != -1L){
                    Toast.makeText(this, "¡Usuario registrado con éxito! ID: $idGenerado", Toast.LENGTH_LONG).show()
                    finish()
                } else{
                    Toast.makeText(this, "Error al guardar en la Base de Datos", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Accion al pulsar el boton de volver, volvemos al login
        vinculoRegistro.btnVolverLogin.setOnClickListener {
            finish()
        }
    }
}