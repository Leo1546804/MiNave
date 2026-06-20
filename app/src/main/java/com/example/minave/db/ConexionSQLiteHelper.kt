package com.example.minave.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ConexionSQLiteHelper(context: Context) :
    SQLiteOpenHelper(context, "MiNaveDB.db", null, 3) {

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

        // Tabla de Certificados
        db.execSQL("""
            CREATE TABLE certificado (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                id_vehiculo INTEGER,
                tipo_documento TEXT,
                fecha_emision TEXT,
                fecha_vencimiento TEXT,
                empresa_emisora TEXT,
                observaciones TEXT,
                FOREIGN KEY(id_vehiculo) REFERENCES vehiculos(id) ON DELETE CASCADE
            )
        """.trimIndent())

        // Tabla de Lavadas
        db.execSQL("""
            CREATE TABLE lavadas (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                id_vehiculo INTEGER NOT NULL,
                fecha TEXT,
                tipo TEXT,
                lugar TEXT,
                costo REAL,
                observaciones TEXT,
                FOREIGN KEY(id_vehiculo) REFERENCES vehiculos(id) ON DELETE CASCADE
            )
        """.trimIndent())

        // Tabla de Combustibles
        db.execSQL("""
            CREATE TABLE combustibles (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                id_vehiculo INTEGER NOT NULL,
                fecha TEXT,
                grifo TEXT,
                tipo_combustible TEXT,
                litros REAL,
                precio_litro REAL,
                costo REAL,
                kilometraje INTEGER,
                observaciones TEXT,
                FOREIGN KEY(id_vehiculo) REFERENCES vehiculos(id) ON DELETE CASCADE
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        if (oldVersion < 2) {
            // ... (keep version 2 logic or just ensure tables exist)
            db.execSQL("CREATE TABLE IF NOT EXISTS mantenimiento (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, id_vehiculo INTEGER NOT NULL, tipo_mantenimiento TEXT, fecha TEXT, kilometraje INTEGER, proximo_kilometraje INTEGER, costo REAL, taller TEXT, observaciones TEXT, FOREIGN KEY(id_vehiculo) REFERENCES vehiculos(id) ON DELETE CASCADE)")
            db.execSQL("CREATE TABLE IF NOT EXISTS certificado (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, id_vehiculo INTEGER, tipo_documento TEXT, fecha_emision TEXT, fecha_vencimiento TEXT, empresa_emisora TEXT, observaciones TEXT, FOREIGN KEY(id_vehiculo) REFERENCES vehiculos(id) ON DELETE CASCADE)")
        }
        
        if (oldVersion < 3) {
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS lavadas (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    id_vehiculo INTEGER NOT NULL,
                    fecha TEXT,
                    tipo TEXT,
                    lugar TEXT,
                    costo REAL,
                    observaciones TEXT,
                    FOREIGN KEY(id_vehiculo) REFERENCES vehiculos(id) ON DELETE CASCADE
                )
            """.trimIndent())

            db.execSQL("""
                CREATE TABLE IF NOT EXISTS combustibles (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    id_vehiculo INTEGER NOT NULL,
                    fecha TEXT,
                    grifo TEXT,
                    tipo_combustible TEXT,
                    litros REAL,
                    precio_litro REAL,
                    costo REAL,
                    kilometraje INTEGER,
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