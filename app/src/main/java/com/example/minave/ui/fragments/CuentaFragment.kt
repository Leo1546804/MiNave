package com.example.minave.ui.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.minave.R
import com.example.minave.utils.Utilidades
import com.example.minave.databinding.FragmentCuentaBinding
import com.example.minave.repositorio.UsuarioRepository
import com.example.minave.ui.activities.LoginActivity

class CuentaFragment : Fragment() {

    private var _binding: FragmentCuentaBinding? = null
    private val binding get() = _binding!!

    private lateinit var usuarioRepository: UsuarioRepository
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCuentaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usuarioRepository = UsuarioRepository(requireContext())
        sharedPreferences = requireActivity().getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE)

        cargarDatosUsuario()

        binding.btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun cargarDatosUsuario(){
        val idUsuarioLogueado = sharedPreferences.getInt("id_usuario_conectado", -1)

        if(idUsuarioLogueado != -1) {
            val usuario = usuarioRepository.obtenerUsuarioPorId(idUsuarioLogueado)

            if(usuario != null){
                binding.tvNombreCuenta.text = "${usuario.nombre} ${usuario.apellido}"
                binding.tvCorreoCuenta.text = usuario.correo
                binding.tvMiembroDesde.text = "Miembro desde: ${usuario.fechaRegistro}"
            }
        }
    }

    private fun cerrarSesion(){
        Utilidades.mostrarDialogoConfirmacion(
            contexto = requireContext(),
            titulo = "Cerrar Sesión",
            mensaje = "¿Estás seguro de que deseas salir de tu cuenta?",
            textoBotonConfirmar = "Cerrar Sesión",
            colorBoton = R.color.azul_principal
        ) {
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()

            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}