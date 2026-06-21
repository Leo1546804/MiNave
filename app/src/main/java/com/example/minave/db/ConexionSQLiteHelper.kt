package com.example.minave.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ConexionSQLiteHelper(context: Context) : SQLiteOpenHelper(context, "MiNaveDB.db", null, 6) {

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

        db.execSQL("""
            CREATE TABLE combustible (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                id_vehiculo INTEGER,
                fecha TEXT,
                litros REAL,
                costo REAL,
                observaciones TEXT,
                FOREIGN KEY(id_vehiculo) REFERENCES vehiculos(id) ON DELETE CASCADE
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE lavadas (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                id_vehiculo INTEGER,
                fecha TEXT,
                tipo TEXT,
                lugar TEXT,
                costo REAL,
                observaciones TEXT,
                FOREIGN KEY(id_vehiculo) REFERENCES vehiculos(id) ON DELETE CASCADE
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE mantenimiento (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                id_vehiculo INTEGER,
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

        db.execSQL("""
            CREATE TABLE certificado (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                id_vehiculo INTEGER,
                tipo_documento TEXT,
                fecha_emision TEXT,
                fecha_vencimiento TEXT,
                empresa_emisora TEXT,
                costo REAL DEFAULT 0.0,
                observaciones TEXT,
                FOREIGN KEY(id_vehiculo) REFERENCES vehiculos(id) ON DELETE CASCADE
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS certificado")
        db.execSQL("DROP TABLE IF EXISTS mantenimiento")
        db.execSQL("DROP TABLE IF EXISTS lavadas")
        db.execSQL("DROP TABLE IF EXISTS combustible")
        db.execSQL("DROP TABLE IF EXISTS vehiculos")
        db.execSQL("DROP TABLE IF EXISTS usuarios")
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        db.setForeignKeyConstraintsEnabled(true)
    }
}
