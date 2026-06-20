package com.example.minave.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ConexionSQLiteHelper(context: Context) : SQLiteOpenHelper(context, "MiNaveDB.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
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
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS vehiculos")
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        // Activamos el soporte de Llaves Foráneas en SQLite nativo
        db.setForeignKeyConstraintsEnabled(true)
    }
}