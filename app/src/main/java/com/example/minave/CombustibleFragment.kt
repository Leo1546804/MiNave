package com.example.minave

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minave.adapter.CombustibleAdapter
import com.example.minave.databinding.FragmentCombustibleBinding
import com.example.minave.repositorio.CombustibleRepository

class CombustibleFragment : Fragment() {

    private var _binding: FragmentCombustibleBinding? = null
    private val binding get() = _binding!!

    private lateinit var combustibleRepo: CombustibleRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCombustibleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        combustibleRepo = CombustibleRepository(requireContext())
        binding.rvCombustible.layoutManager = LinearLayoutManager(requireContext())

        binding.btnAgregarCombustible.setOnClickListener {
            val intencion = Intent(requireContext(), RegistrarCombustibleActivity::class.java)
            startActivity(intencion)
        }
    }

    override fun onResume() {
        super.onResume()
        actualizarLista()
    }

    private fun actualizarLista() {
        val preferencias = requireContext().getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE)
        val idVehiculoActivo = preferencias.getInt("id_vehiculo_activo", -1)

        if (idVehiculoActivo != -1) {
            val listaRegistros = combustibleRepo.obtenerRegistrosPorVehiculo(idVehiculoActivo)
            
            binding.rvCombustible.adapter = CombustibleAdapter(listaRegistros) { registro, opcion ->
                when (opcion) {
                    "Editar" -> {
                        val intencion = Intent(requireContext(), RegistrarCombustibleActivity::class.java)
                        intencion.putExtra("modo_edicion", true)
                        intencion.putExtra("id_registro", registro.id)
                        intencion.putExtra("fecha", registro.fecha)
                        intencion.putExtra("litros", registro.litros)
                        intencion.putExtra("costo", registro.costo)
                        intencion.putExtra("observaciones", registro.observaciones)
                        startActivity(intencion)
                    }
                    "Eliminar" -> {
                        val exito = combustibleRepo.eliminarCombustible(registro.id ?: 0)
                        if (exito) {
                            Toast.makeText(requireContext(), "Registro eliminado", Toast.LENGTH_SHORT).show()
                            actualizarLista()
                        }
                    }
                }
            }
        } else {
            Toast.makeText(requireContext(), "No hay un vehículo activo seleccionado", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}