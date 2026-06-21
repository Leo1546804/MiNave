package com.example.minave.repositorio

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.minave.db.ConexionSQLiteHelper
import com.example.minave.modelos.Combustible

class CombustibleRepository(contexto: Context) {

    private val ayudante = ConexionSQLiteHelper(contexto)

    // Insertar un nuevo registro de combustible
    fun insertarCombustible(combustible: Combustible): Long {
        val db: SQLiteDatabase = ayudante.writableDatabase
        val bandeja = ContentValues()

        bandeja.put("id_vehiculo", combustible.idVehiculo)
        bandeja.put("fecha", combustible.fecha)
        bandeja.put("litros", combustible.litros)
        bandeja.put("costo", combustible.costo)
        bandeja.put("observaciones", combustible.observaciones)

        return db.insert("combustible", null, bandeja)
    }

    // Obtener registros filtrados por el vehículo activo
    fun obtenerRegistrosPorVehiculo(idVehiculo: Int): ArrayList<Combustible> {
        val lista = ArrayList<Combustible>()
        val db: SQLiteDatabase = ayudante.readableDatabase

        val consulta = "SELECT * FROM combustible WHERE id_vehiculo = ? ORDER BY id DESC"
        val cursor = db.rawQuery(consulta, arrayOf(idVehiculo.toString()))

        if (cursor.moveToFirst()) {
            do {
                val registro = Combustible(
                    id = cursor.getInt(0),
                    idVehiculo = cursor.getInt(1),
                    fecha = cursor.getString(2),
                    litros = cursor.getDouble(3),
                    costo = cursor.getDouble(4),
                    observaciones = cursor.getString(5)
                )
                lista.add(registro)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    // Actualizar un registro existente
    fun actualizarCombustible(combustible: Combustible): Boolean {
        val db: SQLiteDatabase = ayudante.writableDatabase
        val bandeja = ContentValues()

        bandeja.put("fecha", combustible.fecha)
        bandeja.put("litros", combustible.litros)
        bandeja.put("costo", combustible.costo)
        bandeja.put("observaciones", combustible.observaciones)

        val filasActualizadas = db.update(
            "combustible", 
            bandeja, 
            "id = ?", 
            arrayOf(combustible.id.toString())
        )

        return filasActualizadas > 0
    }

    // Eliminar un registro de combustible
    fun eliminarCombustible(idCombustible: Int): Boolean {
        val db: SQLiteDatabase = ayudante.writableDatabase

        val filasEliminadas = db.delete(
            "combustible", 
            "id = ?", 
            arrayOf(idCombustible.toString())
        )

        return filasEliminadas > 0
    }

    fun obtenerGastoTotalPorVehiculo(idVehiculo: Int): Double {
        var total = 0.0
        val db = ayudante.readableDatabase
        val cursor = db.rawQuery("SELECT SUM(costo) FROM combustible WHERE id_vehiculo = ?", arrayOf(idVehiculo.toString()))
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0)
        }
        cursor.close()
        return total
    }
}