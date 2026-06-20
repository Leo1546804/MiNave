package com.example.minave.repositorio

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.minave.db.ConexionSQLiteHelper
import com.example.minave.modelos.Certificado

class CertificadoRepository(private val contexto: Context) {

    private val ayudante = ConexionSQLiteHelper(contexto)
    private val preferencias = contexto.getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE)

    private fun obtenerIdVehiculoActivo(): Int {
        return preferencias.getInt("id_vehiculo_activo", -1)
    }

    fun insertar(certificado: Certificado): Long {
        val db: SQLiteDatabase = ayudante.writableDatabase
        val idVehiculoActivo = obtenerIdVehiculoActivo()
        
        if (idVehiculoActivo == -1) return -1

        val valores = ContentValues().apply {
            put("id_vehiculo", idVehiculoActivo)
            put("tipo_documento", certificado.tipoDocumento)
            put("fecha_emision", certificado.fechaEmision)
            put("fecha_vencimiento", certificado.fechaVencimiento)
            put("empresa_emisora", certificado.empresaEmisora)
            put("costo", certificado.costo)
            put("observaciones", certificado.observaciones)
        }

        return db.insert("certificado", null, valores)
    }

    fun listar(): ArrayList<Certificado> {
        val lista = ArrayList<Certificado>()
        val db: SQLiteDatabase = ayudante.readableDatabase
        val idVehiculoActivo = obtenerIdVehiculoActivo()

        if (idVehiculoActivo == -1) return lista

        val consulta = "SELECT * FROM certificado WHERE id_vehiculo = ?"
        val cursor = db.rawQuery(consulta, arrayOf(idVehiculoActivo.toString()))

        if (cursor.moveToFirst()) {
            do {
                val certificado = Certificado(
                    id = cursor.getInt(0),
                    idVehiculo = cursor.getInt(1),
                    tipoDocumento = cursor.getString(2),
                    fechaEmision = cursor.getString(3),
                    fechaVencimiento = cursor.getString(4),
                    empresaEmisora = cursor.getString(5),
                    costo = cursor.getDouble(6),
                    observaciones = cursor.getString(7)
                )
                lista.add(certificado)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    fun actualizar(certificado: Certificado): Boolean {
        val db: SQLiteDatabase = ayudante.writableDatabase
        val valores = ContentValues().apply {
            put("tipo_documento", certificado.tipoDocumento)
            put("fecha_emision", certificado.fechaEmision)
            put("fecha_vencimiento", certificado.fechaVencimiento)
            put("empresa_emisora", certificado.empresaEmisora)
            put("costo", certificado.costo)
            put("observaciones", certificado.observaciones)
        }

        val filasActualizadas = db.update("certificado", valores, "id = ?", arrayOf(certificado.id.toString()))
        return filasActualizadas > 0
    }

    fun eliminar(idCertificado: Int): Boolean {
        val db: SQLiteDatabase = ayudante.writableDatabase
        val filasEliminadas = db.delete("certificado", "id = ?", arrayOf(idCertificado.toString()))
        return filasEliminadas > 0
    }

    fun obtenerGastoTotalPorVehiculo(idVehiculo: Int): Double {
        var total = 0.0
        val db = ayudante.readableDatabase
        val cursor = db.rawQuery("SELECT SUM(costo) FROM certificado WHERE id_vehiculo = ?", arrayOf(idVehiculo.toString()))
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0)
        }
        cursor.close()
        return total
    }
}
