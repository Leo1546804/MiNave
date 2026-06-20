package com.example.minave.repositorio

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.minave.db.ConexionSQLiteHelper
import com.example.minave.modelos.Mantenimiento

class MantenimientoRepository(contexto: Context) {

    private val ayudante = ConexionSQLiteHelper(contexto)
    private val preferencias = contexto.getSharedPreferences("SesionUsuario", Context.MODE_PRIVATE)

    private fun obtenerIdVehiculoActivo(): Int {
        return preferencias.getInt("id_vehiculo_activo", -1)
    }

    fun insertarMantenimiento(mantenimiento: Mantenimiento): Long {
        val db: SQLiteDatabase = ayudante.writableDatabase
        val idVehiculo = obtenerIdVehiculoActivo()
        
        if (idVehiculo == -1) return -1L

        val bandeja = ContentValues().apply {
            put("id_vehiculo", idVehiculo)
            put("tipo_mantenimiento", mantenimiento.tipoMantenimiento)
            put("fecha", mantenimiento.fecha)
            put("kilometraje", mantenimiento.kilometraje)
            put("proximo_kilometraje", mantenimiento.proximoKilometraje)
            put("costo", mantenimiento.costo)
            put("taller", mantenimiento.taller)
            put("observaciones", mantenimiento.observaciones)
        }

        val resultado = db.insert("mantenimiento", null, bandeja)
        db.close()
        return resultado
    }

    fun listarMantenimientosPorVehiculo(): List<Mantenimiento> {
        val lista = mutableListOf<Mantenimiento>()
        val idVehiculo = obtenerIdVehiculoActivo()
        
        if (idVehiculo == -1) return lista

        val db: SQLiteDatabase = ayudante.readableDatabase
        val consulta = """
            SELECT id, id_vehiculo, tipo_mantenimiento, fecha, kilometraje, proximo_kilometraje, costo, taller, observaciones 
            FROM mantenimiento 
            WHERE id_vehiculo = ?
            ORDER BY id DESC
        """.trimIndent()

        val cursor = db.rawQuery(consulta, arrayOf(idVehiculo.toString()))

        if (cursor.moveToFirst()) {
            do {
                val mantenimiento = Mantenimiento(
                    id = cursor.getInt(0),
                    idVehiculo = cursor.getInt(1),
                    tipoMantenimiento = cursor.getString(2),
                    fecha = cursor.getString(3),
                    kilometraje = cursor.getInt(4),
                    proximoKilometraje = cursor.getInt(5),
                    costo = cursor.getDouble(6),
                    taller = cursor.getString(7),
                    observaciones = cursor.getString(8)
                )
                lista.add(mantenimiento)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun actualizarMantenimiento(mantenimiento: Mantenimiento): Int {
        val db: SQLiteDatabase = ayudante.writableDatabase
        val bandeja = ContentValues().apply {
            put("tipo_mantenimiento", mantenimiento.tipoMantenimiento)
            put("fecha", mantenimiento.fecha)
            put("kilometraje", mantenimiento.kilometraje)
            put("proximo_kilometraje", mantenimiento.proximoKilometraje)
            put("costo", mantenimiento.costo)
            put("taller", mantenimiento.taller)
            put("observaciones", mantenimiento.observaciones)
        }

        val resultado = db.update("mantenimiento", bandeja, "id = ?", arrayOf(mantenimiento.id.toString()))
        db.close()
        return resultado
    }

    fun eliminarMantenimiento(id: Int): Int {
        val db: SQLiteDatabase = ayudante.writableDatabase
        val resultado = db.delete("mantenimiento", "id = ?", arrayOf(id.toString()))
        db.close()
        return resultado
    }
}
