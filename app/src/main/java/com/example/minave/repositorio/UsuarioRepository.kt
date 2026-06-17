package com.example.minave.repositorio

import android.R
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.minave.db.ConexionSQLiteHelper

class UsuarioRepository(contexto: Context) {

    private val ayudante = ConexionSQLiteHelper(contexto)

    fun insertarUsuario(
        nombre: String,
        apellido: String,
        correo: String,
        contrasena: String
    ): Long {
        // abrimos la base de daatos en modo escritura
        val db: SQLiteDatabase = ayudante.writableDatabase

        val bandeja = ContentValues()

        bandeja.put("nombre", nombre)
        bandeja.put("apellido", apellido)
        bandeja.put("correo", correo)
        bandeja.put("password", contrasena)
        bandeja.put("fecha_registro", "17/06/2026")

        val resultado = db.insert("usuarios", null, bandeja)

        db.close()

        return resultado
    }

    fun validarIngreso(correo: String, contraseña: String): Boolean {
        //abrimos la base de datos en modo lectura
        val db: SQLiteDatabase = ayudante.readableDatabase

        val consulta = "SELECT * FROM usuarios WHERE correo = ? AND password = ?"

        val parametros = arrayOf(correo, contraseña)

        //cursor es el que recorre las filas de la tabla buscando el resultado
        val cursor = db.rawQuery(consulta, parametros)

        // si se encontró un usuario, el conteo sera mayor a 0
        val existeUsuario = cursor.count > 0

        //cerramos el cursor y la base de datas
        cursor.close()
        db.close()

        // nos devuelve true si el usuario existe y sus datos correctos, de lo contrario es false
        return existeUsuario
    }

}