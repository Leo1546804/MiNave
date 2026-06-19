package com.example.minave

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.minave.databinding.FragmentInicioBinding
import com.example.minave.repositorio.VehiculoRepository
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class InicioFragment : Fragment() {

    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!

    private lateinit var vehiculoRepo: VehiculoRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vehiculoRepo = VehiculoRepository(requireContext())

        // Acción del botón del estado vacio, este boton solo se mostrara en el fragment cuando no haya un vehiculo registrado
        binding.btnIrARegistrar.setOnClickListener {
            // Mandamos al usuario directo al formulario
            val intencion = Intent(requireContext(), RegistrarVehiculoActivity::class.java)
            startActivity(intencion)
        }


        binding.cvSelectorVehiculo.setOnClickListener {
            mostrarDialogoSelectorVehiculo()
        }
    }

        override fun onResume() {
            super.onResume()
            evaluarEstadoDelVehiculo()
        }

        private fun evaluarEstadoDelVehiculo(){
            val preferencias = requireContext().getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE)
            val idUsuarioSesion = preferencias.getInt("id_usuario_conectado", -1)

            if (idUsuarioSesion == -1) return

            val listaCarrosReal = vehiculoRepo.obtenerVehiculosPorUsuario(idUsuarioSesion)

            if(listaCarrosReal.isEmpty()){
                // no hay vehiculos
                binding.layoutSinVehiculo.visibility = View.VISIBLE
                binding.cvSelectorVehiculo.visibility = View.GONE
                binding.layoutDashboardReal.visibility = View.GONE
                preferencias.edit().putInt("id_vehiculo_activo", -1).apply()
            } else{
                // si hay vehiculos
                var idActivo = preferencias.getInt("id_vehiculo_activo", -1)
                var nombreActivo = preferencias.getString("nombre_vehiculo_activo", "")

                // si el ssuario no selecciona un vehiculo, se autoselecciona
                if(idActivo == -1 || listaCarrosReal.none{it.id == idActivo}){
                    val primerCarro = listaCarrosReal[0]
                    idActivo = primerCarro.id ?: -1
                    nombreActivo = "${primerCarro.marca} ${primerCarro.modelo}"

                    preferencias.edit().apply(){
                        putInt("id_vehiculo_activo", idActivo)
                        putString("nombre_vehiculo_activo", nombreActivo)
                        apply()
                    }
                }

                // Pintar los datos en pantalla
                binding.layoutSinVehiculo.visibility = View.GONE
                binding.cvSelectorVehiculo.visibility = View.VISIBLE
                binding.layoutDashboardReal.visibility = View.VISIBLE

                binding.txtVehiculoActivo.text = nombreActivo
            }
        }

        // muestra la lista de carros para cambiar
        private fun mostrarDialogoSelectorVehiculo(){
            val preferencias = requireContext().getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE)
            val idUsuarioSesion = preferencias.getInt("id_usuario_conectado", -1)
            val idCarroActivoActual = preferencias.getInt("id_vehiculo_activo", -1)

            val listaCarros = vehiculoRepo.obtenerVehiculosPorUsuario(idUsuarioSesion)

            //lista de nombres para mostrar en el menú flotante
            val nombresOpciones = mutableListOf<String>()

            listaCarros.forEach { carro ->
                // le ponemos un check al carro que esta actualmente seleccionado
                if(carro.id == idCarroActivoActual){
                    nombresOpciones.add("✓ ${carro.marca} ${carro.modelo}")
                } else {
                    nombresOpciones.add("${carro.marca} ${carro.modelo}")
                }
            }

            // Agregamos a la ultima opcion de registrar uno nuevo
            nombresOpciones.add("➕ Agregar vehículo")

            // Mostramos el dialogo nativo
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Selecciona un vehículo")
                .setItems(nombresOpciones.toTypedArray()) { _, posicionClicada ->

                    if (posicionClicada == nombresOpciones.size - 1){
                        // El usuario tocó la última opción ("Agregar vehículo")
                        val intent = Intent(requireContext(), RegistrarVehiculoActivity::class.java)
                        startActivity(intent)
                    } else{
                        // El usuario seleccionó otro carro de su lista
                        val carroElegido = listaCarros[posicionClicada]

                        // guardamos la nueva eleccion en caja fuerteee
                        preferencias.edit().apply(){
                            putInt("id_vehiculo_activo", carroElegido.id ?: -1)
                            putString("nombre_vehiculo_activo", "${carroElegido.marca} ${carroElegido.modelo}")
                            apply()
                        }

                        Toast.makeText(requireContext(), "Cambiaste a: ${carroElegido.marca}", Toast.LENGTH_SHORT).show()

                        // Recargamos el Dashboard al instante
                        evaluarEstadoDelVehiculo()
                    }
                }
                .show()
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

}