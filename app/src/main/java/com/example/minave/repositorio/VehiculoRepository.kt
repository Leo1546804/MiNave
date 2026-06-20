package com.example.minave.repositorio

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.minave.db.ConexionSQLiteHelper
import com.example.minave.modelos.Usuario
import com.example.minave.modelos.Vehiculo

class VehiculoRepository(contexto: Context) {

    private val ayudante = ConexionSQLiteHelper(contexto)

    // Insertar un nuevo carro a la base de datos
    fun insertarVehiculo(vehiculo: Vehiculo): Long{
        val db: SQLiteDatabase = ayudante.writableDatabase
        val bandeja = ContentValues()

        bandeja.put("placa", vehiculo.placa)
        bandeja.put("marca", vehiculo.marca)
        bandeja.put("modelo", vehiculo.modelo)
        bandeja.put("anio", vehiculo.anio)
        bandeja.put("color", vehiculo.color)
        bandeja.put("tipo_combustible", vehiculo.tipoCombustible)
        bandeja.put("id_usuario", vehiculo.idUsuario)

        return db.insert("vehiculos", null, bandeja)
    }

    fun obtenerVehiculosPorUsuario(idUsuario: Int): ArrayList<Vehiculo>{
        val lista = ArrayList<Vehiculo>()
        val db: SQLiteDatabase = ayudante.readableDatabase

        val consulta = "SELECT * FROM vehiculos WHERE id_usuario = ?"
        val cursor = db.rawQuery(consulta, arrayOf(idUsuario.toString()))

        if(cursor.moveToFirst()){
            do{
                val auto =Vehiculo(
                    id = cursor.getInt(0),
                    placa = cursor.getString(1),
                    marca = cursor.getString(2),
                    modelo = cursor.getString(3),
                    anio = cursor.getInt(4),
                    color = cursor.getString(5),
                    tipoCombustible = cursor.getString(6),
                    idUsuario = cursor.getInt(7)
                )
                lista.add(auto)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    fun actualizarVehiculo(vehiculo: Vehiculo): Boolean{
        val db: SQLiteDatabase = ayudante.writableDatabase
        val bandeja = ContentValues()

        bandeja.put("placa", vehiculo.placa)
        bandeja.put("marca", vehiculo.marca)
        bandeja.put("modelo", vehiculo.modelo)
        bandeja.put("anio", vehiculo.anio)
        bandeja.put("color", vehiculo.color)
        bandeja.put("tipo_combustible", vehiculo.tipoCombustible)

        val filasActualizadas = db.update("vehiculos", bandeja,"id = ?", arrayOf(vehiculo.id.toString()))

        return filasActualizadas > 0
    }

    fun eliminarVehiculo(idVehiculo: Int): Boolean{
        val db: SQLiteDatabase = ayudante.writableDatabase

        val filasEliminadas = db.delete("vehiculos", "id = ?", arrayOf(idVehiculo.toString()))

        return filasEliminadas > 0
    }

}