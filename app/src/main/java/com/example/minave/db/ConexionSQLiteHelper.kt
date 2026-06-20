package com.example.minave.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ConexionSQLiteHelper(context: Context) : SQLiteOpenHelper(context, "MiNaveDB.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        // Tabla de Usuarios
        db.execSQL("""
            CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                nombre TEXT,
                apellido TEXT,
                correo TEXT,
                password TEXT,
                fecha_registro TEXT
            )
        """.trimIndent())

        // Tabla de Vehículos
        db.execSQL("""
            CREATE TABLE vehiculos(
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                placa TEXT,
                marca TEXT,
                modelo TEXT,
                anio INTEGER,
                color TEXT,
                tipo_combustible TEXT,
                id_usuario INTEGER,
                FOREIGN KEY(id_usuario) REFERENCES usuarios(id) ON DELETE CASCADE
            )
        """.trimIndent())

        // Tabla de Mantenimientos
        db.execSQL("""
            CREATE TABLE mantenimiento (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                id_vehiculo INTEGER NOT NULL,
                tipo_mantenimiento TEXT,
                fecha TEXT,
                kilometraje INTEGER,
                proximo_kilometraje INTEGER,
                costo REAL,
                taller TEXT,
                observaciones TEXT,
                FOREIGN KEY(id_vehiculo) REFERENCES vehiculos(id) ON DELETE CASCADE
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("""
                CREATE TABLE mantenimiento (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    id_vehiculo INTEGER NOT NULL,
                    tipo_mantenimiento TEXT,
                    fecha TEXT,
                    kilometraje INTEGER,
                    proximo_kilometraje INTEGER,
                    costo REAL,
                    taller TEXT,
                    observaciones TEXT,
                    FOREIGN KEY(id_vehiculo) REFERENCES vehiculos(id) ON DELETE CASCADE
                )
            """.trimIndent())
        }
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        db.setForeignKeyConstraintsEnabled(true)
    }
}
