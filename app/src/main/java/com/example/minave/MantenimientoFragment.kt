package com.example.minave

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minave.adapter.MantenimientoAdapter
import com.example.minave.databinding.FragmentMantenimientoBinding
import com.example.minave.repositorio.MantenimientoRepository

/**
 * Fragmento encargado de la visualización y gestión del historial de mantenimientos.
 *
 * Este componente permite al usuario visualizar una lista de todos los servicios
 * realizados al vehículo actualmente seleccionado, además de proveer acceso
 * a la creación y edición de registros mediante un FloatingActionButton.
 *
 * PROYECTO: AutoControl
 */
class MantenimientoFragment : Fragment() {

    // Referencia al binding para acceso seguro a las vistas del layout
    private var _vinculo: FragmentMantenimientoBinding? = null
    private val vinculo get() = _vinculo!!

    // Dependencias de lógica de negocio y datos
    private lateinit var mantenimientoRepo: MantenimientoRepository
    private lateinit var adaptador: MantenimientoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflado del layout utilizando ViewBinding
        _vinculo = FragmentMantenimientoBinding.inflate(inflater, container, false)
        return vinculo.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicialización del repositorio de datos
        mantenimientoRepo = MantenimientoRepository(requireContext())

        // Configuración de la lista (RecyclerView)
        configurarRecyclerView()

        // Configuración del botón flotante para agregar nuevos mantenimientos
        configurarBotonAccion()
    }

    /**
     * Configura el RecyclerView estableciendo su LayoutManager y el adaptador personalizado.
     * Define la acción de navegación hacia la edición al interactuar con un elemento.
     */
    private fun configurarRecyclerView() {
        adaptador = MantenimientoAdapter(emptyList()) { mantenimiento ->
            // Navegación a RegistrarMantenimientoActivity en modo EDICIÓN
            val intencion = Intent(requireContext(), RegistrarMantenimientoActivity::class.java).apply {
                putExtra("modo_edicion", true)
                putExtra("id", mantenimiento.id)
                putExtra("tipo", mantenimiento.tipoMantenimiento)
                putExtra("fecha", mantenimiento.fecha)
                putExtra("km", mantenimiento.kilometraje)
                putExtra("proximo_km", mantenimiento.proximoKilometraje)
                putExtra("costo", mantenimiento.costo)
                putExtra("taller", mantenimiento.taller)
                putExtra("obs", mantenimiento.observaciones)
            }
            startActivity(intencion)
        }

        vinculo.rvMantenimientos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adaptador
        }
    }

    /**
     * Define el comportamiento del botón de acción principal (FAB).
     */
    private fun configurarBotonAccion() {
        vinculo.btnAgregarMantenimiento.setOnClickListener {
            // Navegación a RegistrarMantenimientoActivity en modo CREACIÓN
            val intencion = Intent(requireContext(), RegistrarMantenimientoActivity::class.java)
            intencion.putExtra("modo_edicion", false)
            startActivity(intencion)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresco automático de la lista al volver al fragmento
        cargarDatosDesdeRepositorio()
    }

    /**
     * Recupera los registros desde SQLite filtrados por el vehículo activo
     * y actualiza la interfaz de usuario.
     */
    private fun cargarDatosDesdeRepositorio() {
        val lista = mantenimientoRepo.listarMantenimientosPorVehiculo()
        adaptador.actualizarLista(lista)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Liberación del binding para prevenir fugas de memoria (Memory Leaks)
        _vinculo = null
    }
}
