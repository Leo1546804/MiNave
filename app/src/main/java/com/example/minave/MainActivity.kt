package com.example.minave

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.minave.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarPrincipal)

        // Creamos el gatillo que conectará el botón con el menú deslizante
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbarPrincipal,
            R.string.abrir_menu,
            R.string.cerrar_menu
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState() // Esto dibuja las 3 rayitas mágicamente

        // Cargar la pantalla de Inicio por defecto al abrir la app
        if (savedInstanceState == null) {
            cambiarFragmento(InicioFragment())
            binding.navegacionLateral.setCheckedItem(R.id.nav_inicio)
        }

        // escucuchar los clics del menu lateral
        binding.navegacionLateral.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_inicio -> cambiarFragmento(InicioFragment())
                R.id.nav_vehiculos -> cambiarFragmento(VehiculoFragment())
                R.id.nav_combustible -> cambiarFragmento(CombustibleFragment())
                R.id.nav_mantenimiento -> cambiarFragmento(MantenimientoFragment())
                R.id.nav_lavadas -> cambiarFragmento(LavadasFragment())

                R.id.nav_certificados -> cambiarFragmento(CertificadosFragment())
                R.id.nav_cerrar_sesion -> {
                    Toast.makeText(this, "Cerrando Sesión", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
            }
            // Al hacer clic en cualquier opción, cerramos el menú lateral con estilo
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            true
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.drawerLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Función limpia para reemplazar el centro de la pantalla que usaremos para cambiar los fragmentos segun los clics que se hagan en el menu lateral
    private fun cambiarFragmento(fragmento: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.contenedorFragmentos, fragmento)
            .commit()
    }

    // Si el usuario presiona el botón "Atrás" del celular y el menú está abierto, lo cierra primero
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}