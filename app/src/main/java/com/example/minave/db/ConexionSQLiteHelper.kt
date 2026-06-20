package com.example.minave.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ConexionSQLiteHelper(context: Context) :
    SQLiteOpenHelper(context, "MiNaveDB.db", null, 4) {

    override fun onCreate(db: SQLiteDatabase) {

        // ... (rest of the tables)
        // Update the certificado table definition in onCreate too
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
        // ... (other tables)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // ...
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE certificado ADD COLUMN costo REAL DEFAULT 0.0")
        }
    }

    override fun onOpen(db: SQLiteDatabase) {
        super.onOpen(db)
        db.setForeignKeyConstraintsEnabled(true)
    }
}