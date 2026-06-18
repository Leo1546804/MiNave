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
import com.example.minave.adapter.VehiculoAdapter
import com.example.minave.databinding.FragmentVehiculoBinding
import com.example.minave.repositorio.VehiculoRepository

class VehiculoFragment : Fragment() {

    private var _binding: FragmentVehiculoBinding? = null
    private val binding get() = _binding!!

    private lateinit var vehiculoRepo: VehiculoRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentVehiculoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)

        vehiculoRepo = VehiculoRepository(requireContext())
        binding.rvVehiculos.layoutManager = LinearLayoutManager(requireContext())

        binding.btnAgregarVehiculo.setOnClickListener {
            // Creamos un Intent nativo para brincar a la pantalla de registro
            val intencion = Intent(requireContext(), RegistrarVehiculoActivity::class.java)
            startActivity(intencion)
        }
    }

    override fun onResume() {
        super.onResume()
        actualizarLista()
    }

    private fun actualizarLista(){
        val preferencias = requireContext().getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE)
        val idUsuarioSesion = preferencias.getInt("id_usuario_conectado", -1)

        if(idUsuarioSesion != -1){
            // llamamos al metodo listar del repositorio
            val listaNaves = vehiculoRepo.obtenerVehiculosPorUsuario(idUsuarioSesion)

            //inflamos el adaptador pasandole la listaa y escuchando la opcion que presionó
            binding.rvVehiculos.adapter = VehiculoAdapter(listaNaves){ nave, opcion ->
                when(opcion){
                    "editar" -> {
                        Toast.makeText(requireContext(), "Editar: ${nave.placa}", Toast.LENGTH_SHORT).show()
                    }
                    "Eliminar" ->{
                        Toast.makeText(requireContext(), "Eliminar de base de datos: ${nave.placa}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //Rompemos la referencia para evitar fugas de memoria
        _binding = null
    }
}