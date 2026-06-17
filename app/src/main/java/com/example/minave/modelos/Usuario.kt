package com.example.minave.modelos

data class Usuario (
    val id: Int? = null,
    val nombre: String,
    val apellido: String,
    val correo: String,
    val contrasenia: String,
    val fechaRegistro: String
)