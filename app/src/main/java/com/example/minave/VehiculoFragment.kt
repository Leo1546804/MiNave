package com.example.minave

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.minave.databinding.FragmentVehiculoBinding

class VehiculoFragment : Fragment() {

    private var _binding: FragmentVehiculoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVehiculoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)


        binding.btnAgregarVehiculo.setOnClickListener {
            // Creamos un Intent nativo para brincar a la pantalla de registro
            val intencion = Intent(requireContext(), RegistrarVehiculoActivity::class.java)
            startActivity(intencion)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //Rompemos la referencia para evitar fugas de memoria
        _binding = null
    }
}