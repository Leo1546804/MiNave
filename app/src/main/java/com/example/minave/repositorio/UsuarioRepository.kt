package com.example.minave.repositorio

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.minave.db.ConexionSQLiteHelper
import com.example.minave.modelos.Usuario
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UsuarioRepository(contexto: Context) {

    private val ayudante = ConexionSQLiteHelper(contexto)

    fun insertarUsuario(usuario: Usuario): Long {
        val db: SQLiteDatabase = ayudante.writableDatabase
        val bandeja = ContentValues()

        // En lugar de variables sueltas, extraemos los datos directamente del objeto
        bandeja.put("nombre", usuario.nombre)
        bandeja.put("apellido", usuario.apellido)
        bandeja.put("correo", usuario.correo)
        bandeja.put("password", usuario.contrasenia)
        bandeja.put("fecha_registro", usuario.fechaRegistro)

        val resultado = db.insert("usuarios", null, bandeja)
        //db.close()

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
        //db.close()

        // nos devuelve true si el usuario existe y sus datos correctos, de lo contrario es false
        return existeUsuario
    }

}