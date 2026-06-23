package com.example.minave

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minave.adapter.MantenimientoAdapter
import com.example.minave.databinding.FragmentMantenimientoBinding
import com.example.minave.modelos.Mantenimiento
import com.example.minave.repositorio.MantenimientoRepository


class MantenimientoFragment : Fragment() {

    private var _vinculo: FragmentMantenimientoBinding? = null
    private val vinculo get() = _vinculo!!

    private lateinit var mantenimientoRepo: MantenimientoRepository
    private lateinit var adaptador: MantenimientoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _vinculo = FragmentMantenimientoBinding.inflate(inflater, container, false)
        return vinculo.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mantenimientoRepo = MantenimientoRepository(requireContext())

        configurarRecyclerView()
        configurarBotonAccion()
    }

    private fun configurarRecyclerView() {
        adaptador = MantenimientoAdapter(
            listaMantenimientos = emptyList(),
            alEditar = { mantenimiento -> abrirPantallaEdicion(mantenimiento) },
            alEliminar = { mantenimiento -> confirmarEliminacion(mantenimiento) }
        )

        vinculo.rvMantenimientos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adaptador
        }
    }

    private fun abrirPantallaEdicion(mantenimiento: Mantenimiento) {
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

    private fun confirmarEliminacion(mantenimiento: Mantenimiento) {
        AlertDialog.Builder(requireContext())
            .setTitle("Eliminar Registro")
            .setMessage("¿Estás seguro de que deseas eliminar este mantenimiento?")
            .setPositiveButton("Sí, eliminar") { _, _ ->
                val filasAfectadas = mantenimientoRepo.eliminarMantenimiento(mantenimiento.id)
                if (filasAfectadas > 0) {
                    Toast.makeText(requireContext(), "Mantenimiento eliminado", Toast.LENGTH_SHORT).show()
                    cargarDatosDesdeRepositorio()
                } else {
                    Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun configurarBotonAccion() {
        vinculo.btnAgregarMantenimiento.setOnClickListener {
            val intencion = Intent(requireContext(), RegistrarMantenimientoActivity::class.java)
            intencion.putExtra("modo_edicion", false)
            startActivity(intencion)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarDatosDesdeRepositorio()
    }

    private fun cargarDatosDesdeRepositorio() {
        val lista = mantenimientoRepo.listarMantenimientosPorVehiculo()
        
        if (lista.isEmpty()) {
            vinculo.rvMantenimientos.visibility = View.GONE
            vinculo.layoutVacioMantenimiento.visibility = View.VISIBLE
        } else {
            vinculo.rvMantenimientos.visibility = View.VISIBLE
            vinculo.layoutVacioMantenimiento.visibility = View.GONE
            adaptador.actualizarLista(lista)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _vinculo = null
    }
}
