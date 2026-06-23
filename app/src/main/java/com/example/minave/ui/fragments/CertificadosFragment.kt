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
import com.example.minave.adapter.CertificadoAdapter
import com.example.minave.databinding.FragmentCertificadosBinding
import com.example.minave.repositorio.CertificadoRepository
import com.example.minave.ui.activities.RegistrarCertificadoActivity

class CertificadosFragment : Fragment() {

    private var _vinculo: FragmentCertificadosBinding? = null
    private val vinculo get() = _vinculo!!

    private lateinit var repositorioCertificado: CertificadoRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _vinculo = FragmentCertificadosBinding.inflate(inflater, container, false)
        return vinculo.root
    }

    override fun onViewCreated(vista: View, savedInstanceState: Bundle?) {
        super.onViewCreated(vista, savedInstanceState)

        repositorioCertificado = CertificadoRepository(requireContext())
        vinculo.rvCertificados.layoutManager = LinearLayoutManager(requireContext())

        vinculo.btnAgregarCertificado.setOnClickListener {
            val intencion = Intent(requireContext(), RegistrarCertificadoActivity::class.java)
            startActivity(intencion)
        }
    }

    override fun onResume() {
        super.onResume()
        refrescarListaCertificados()
    }

    private fun refrescarListaCertificados() {
        val listaCertificados = repositorioCertificado.listar()

        if (listaCertificados.isEmpty()) {
            vinculo.rvCertificados.visibility = View.GONE
            vinculo.layoutVacioCertificado.visibility = View.VISIBLE
        } else {
            vinculo.rvCertificados.visibility = View.VISIBLE
            vinculo.layoutVacioCertificado.visibility = View.GONE
            vinculo.rvCertificados.adapter =
                CertificadoAdapter(listaCertificados) { certificado, opcion ->
                    when (opcion) {
                        "Editar" -> {
                            val intencion =
                                Intent(requireContext(), RegistrarCertificadoActivity::class.java)
                            intencion.putExtra("modo_edicion", true)
                            intencion.putExtra("id_certificado", certificado.id ?: -1)
                            intencion.putExtra("id_vehiculo", certificado.idVehiculo)
                            intencion.putExtra("tipo", certificado.tipoDocumento)
                            intencion.putExtra("empresa", certificado.empresaEmisora)
                            intencion.putExtra("emision", certificado.fechaEmision)
                            intencion.putExtra("vencimiento", certificado.fechaVencimiento)
                            intencion.putExtra("costo", certificado.costo)
                            intencion.putExtra("observaciones", certificado.observaciones)
                            startActivity(intencion)
                        }

                        "Eliminar" -> {
                            Utilidades.mostrarDialogoConfirmacion(
                                contexto = requireContext(),
                                titulo = "Eliminar Certificado",
                                mensaje = "¿Estás seguro de que deseas eliminar este certificado?",
                                textoBotonConfirmar = "Eliminar"
                            ) {
                                val idAEliminar = certificado.id ?: -1
                                if (idAEliminar != -1) {
                                    val seElimino = repositorioCertificado.eliminar(idAEliminar)
                                    if (seElimino) {
                                        Toast.makeText(
                                            requireContext(),
                                            "Certificado eliminado",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        refrescarListaCertificados()
                                    } else {
                                        Toast.makeText(
                                            requireContext(),
                                            "No se pudo eliminar",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _vinculo = null
    }
}