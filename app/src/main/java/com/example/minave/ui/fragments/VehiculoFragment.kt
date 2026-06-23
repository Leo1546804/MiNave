package com.example.minave.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.minave.utils.Utilidades
import com.example.minave.adapter.VehiculoAdapter
import com.example.minave.databinding.FragmentVehiculoBinding
import com.example.minave.repositorio.VehiculoRepository
import com.example.minave.ui.activities.RegistrarVehiculoActivity

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
            binding.rvVehiculos.adapter = VehiculoAdapter(listaNaves) { nave, opcion ->
                when (opcion) {
                    "Editar" -> {
                        val intencion =
                            Intent(requireContext(), RegistrarVehiculoActivity::class.java)

                        //Metemos todos los datos del carro actual en la maleta del Intent
                        intencion.putExtra("modo_edicion", true)
                        intencion.putExtra("id_vehiculo", nave.id ?: 0)
                        intencion.putExtra("placa", nave.placa)
                        intencion.putExtra("marca", nave.marca)
                        intencion.putExtra("modelo", nave.modelo)
                        intencion.putExtra("anio", nave.anio)
                        intencion.putExtra("color", nave.color)
                        intencion.putExtra("combustible", nave.tipoCombustible)

                        startActivity(intencion)
                    }

                    "Eliminar" -> {
                        Utilidades.mostrarDialogoConfirmacion(
                            contexto = requireContext(),
                            titulo = "Eliminar Vehículo",
                            mensaje = "¿Deseas borrar el ${nave.marca} ${nave.modelo}? Se eliminarán también todos sus historiales.",
                            textoBotonConfirmar = "Sí, eliminar"
                        ) {
                            val eliminadoConExito = vehiculoRepo.eliminarVehiculo(nave.id ?: 0)

                            if (eliminadoConExito) {
                                Toast.makeText(
                                    requireContext(),
                                    "Nave eliminada: ${nave.placa}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                actualizarLista()
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Error al intentar eliminar",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
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