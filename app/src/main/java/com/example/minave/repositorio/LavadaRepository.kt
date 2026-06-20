package com.example.minave.repositorio

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.minave.db.ConexionSQLiteHelper
import com.example.minave.modelos.Lavada

class LavadaRepository(private val contexto: Context) {

    private val ayudante = ConexionSQLiteHelper(contexto)
    private val preferencias = contexto.getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE)

    private fun obtenerIdVehiculoActivo(): Int {
        return preferencias.getInt("id_vehiculo_activo", -1)
    }

    fun insertar(lavada: Lavada): Long {
        val db: SQLiteDatabase = ayudante.writableDatabase
        val idVehiculoActivo = obtenerIdVehiculoActivo()
        
        if (idVehiculoActivo == -1) return -1

        val valores = ContentValues().apply {
            put("id_vehiculo", idVehiculoActivo)
            put("fecha", lavada.fecha)
            put("tipo", lavada.tipo)
            put("lugar", lavada.lugar)
            put("costo", lavada.costo)
            put("observaciones", lavada.observaciones)
        }

        return db.insert("lavadas", null, valores)
    }

    fun listar(): ArrayList<Lavada> {
        val lista = ArrayList<Lavada>()
        val db: SQLiteDatabase = ayudante.readableDatabase
        val idVehiculoActivo = obtenerIdVehiculoActivo()

        if (idVehiculoActivo == -1) return lista

        val cursor = db.rawQuery("SELECT * FROM lavadas WHERE id_vehiculo = ?", arrayOf(idVehiculoActivo.toString()))

        if (cursor.moveToFirst()) {
            do {
                val lavada = Lavada(
                    id = cursor.getInt(0),
                    idVehiculo = cursor.getInt(1),
                    fecha = cursor.getString(2),
                    tipo = cursor.getString(3),
                    lugar = cursor.getString(4),
                    costo = cursor.getDouble(5),
                    observaciones = cursor.getString(6)
                )
                lista.add(lavada)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    fun actualizar(lavada: Lavada): Boolean {
        val db: SQLiteDatabase = ayudante.writableDatabase
        val valores = ContentValues().apply {
            put("fecha", lavada.fecha)
            put("tipo", lavada.tipo)
            put("lugar", lavada.lugar)
            put("costo", lavada.costo)
            put("observaciones", lavada.observaciones)
        }

        val filasActualizadas = db.update("lavadas", valores, "id = ?", arrayOf(lavada.id.toString()))
        return filasActualizadas > 0
    }

    fun eliminar(idLavada: Int): Boolean {
        val db: SQLiteDatabase = ayudante.writableDatabase
        val filasEliminadas = db.delete("lavadas", "id = ?", arrayOf(idLavada.toString()))
        return filasEliminadas > 0
    }
}
