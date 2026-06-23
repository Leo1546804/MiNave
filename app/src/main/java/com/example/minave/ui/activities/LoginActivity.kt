package com.example.minave.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.minave.ui.fragments.MainActivity
import com.example.minave.ui.activities.RegistroActivity
import com.example.minave.databinding.ActivityLoginBinding
import com.example.minave.repositorio.UsuarioRepository

class LoginActivity : AppCompatActivity() {

    private lateinit var vinculo: ActivityLoginBinding
    private lateinit var usuarioRepo: UsuarioRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vinculo = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(vinculo.root)

        // inicializamos el repositorio
        usuarioRepo = UsuarioRepository(this)

        // Acción al pulsar INICIAR SESIÓN
        vinculo.btnIniciarSesion.setOnClickListener {
            val correoIntroducido = vinculo.campoCorreo.text.toString().trim()
            val contrasenaIntroducida = vinculo.campoContrasena.text.toString().trim()

            if (correoIntroducido.isEmpty() || contrasenaIntroducida.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {

                val esValido = usuarioRepo.validarIngreso(correoIntroducido, contrasenaIntroducida)

                if(esValido){

                    // llamamos a la funcion obtener por correo del repositorio
                    val idUsuarioReal = usuarioRepo.obtenerIdPorCorreo(correoIntroducido)

                    // abrimos el archivo fisico que android guardará oculto en la memoria del telefono
                    val preferencias = getSharedPreferences("SesionUsuario", MODE_PRIVATE)
                    // damos permiso para escribir dentro de este archivo
                    val editor = preferencias.edit()
                    // etiqueta clave - valor
                    editor.putInt("id_usuario_conectado", idUsuarioReal)
                    //guardar cambios
                    editor.apply()

                    Toast.makeText(this, "¡Ingreso correcto! Bienvenido", Toast.LENGTH_SHORT).show()

                    // si el usuario existe, ingresamos a la app
                    val intencion = Intent(this, MainActivity::class.java)
                    startActivity(intencion)
                    finish()
                } else{
                    Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Acción al pulsar CREAR CUENTA
        vinculo.btnCrearCuenta.setOnClickListener {
            val intencion = Intent(this, RegistroActivity::class.java)
            startActivity(intencion)
        }
    }
}