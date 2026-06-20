package com.example.minave

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minave.adapter.LavadaAdapter
import com.example.minave.databinding.FragmentLavadasBinding
import com.example.minave.modelos.Lavada
import com.example.minave.repositorio.LavadaRepository

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
        val adapter = LavadaAdapter(
            lista,
            onEliminar = { id ->
                if (repositorio.eliminar(id)) {
                    Toast.makeText(requireContext(), "Registro eliminado", Toast.LENGTH_SHORT).show()
                    cargarLavadas()
                }
            },
            onEditar = { lavada ->
                val intent = Intent(requireContext(), RegistrarLavadoActivity::class.java).apply {
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
        )
        binding.rvLavadas.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
