package com.example.minave.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minave.utils.Utilidades
import com.example.minave.adapter.LavadaAdapter
import com.example.minave.databinding.FragmentLavadasBinding
import com.example.minave.repositorio.LavadaRepository
import com.example.minave.ui.activities.RegistrarLavadoActivity

class LavadasFragment : Fragment() {

    private var _binding: FragmentLavadasBinding? = null
    private val binding get() = _binding!!

    private lateinit var repositorio: LavadaRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLavadasBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repositorio = LavadaRepository(requireContext())
        binding.rvLavadas.layoutManager = LinearLayoutManager(requireContext())

        binding.btnAgregarLavada.setOnClickListener {
            val intent = Intent(requireContext(), RegistrarLavadoActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        cargarLavadas()
    }

    private fun cargarLavadas() {
        val lista = repositorio.listar()

        if (lista.isEmpty()) {
            binding.rvLavadas.visibility = View.GONE
            binding.layoutVacioLavada.visibility = View.VISIBLE
        } else {
            binding.rvLavadas.visibility = View.VISIBLE
            binding.layoutVacioLavada.visibility = View.GONE
            binding.rvLavadas.adapter = LavadaAdapter(lista) { lavada, opcion ->
                when (opcion) {
                    "Editar" -> {
                        val intent =
                            Intent(requireContext(), RegistrarLavadoActivity::class.java).apply {
                                putExtra("modo_edicion", true)
                                putExtra("id_lavada", lavada.id)
                                putExtra("id_vehiculo", lavada.idVehiculo)
                                putExtra("tipo", lavada.tipo)
                                putExtra("lugar", lavada.lugar)
                                putExtra("fecha", lavada.fecha)
                                putExtra("costo", lavada.costo)
                                putExtra("observaciones", lavada.observaciones)
                            }
                        startActivity(intent)
                    }

                    "Eliminar" -> {
                        Utilidades.mostrarDialogoConfirmacion(
                            contexto = requireContext(),
                            titulo = "Eliminar Registro",
                            mensaje = "¿Estás seguro de que deseas eliminar este registro de lavado?",
                            textoBotonConfirmar = "Eliminar"
                        ) {
                            if (repositorio.eliminar(lavada.id)) {
                                Toast.makeText(
                                    requireContext(),
                                    "Registro eliminado",
                                    Toast.LENGTH_SHORT
                                ).show()
                                cargarLavadas()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}