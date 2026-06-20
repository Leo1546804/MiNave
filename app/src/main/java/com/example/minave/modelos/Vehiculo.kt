package com.example.minave.modelos

data class Vehiculo(
    val id: Int? = null,
    val placa: String,
    val marca: String,
    val modelo: String,
    val anio: Int,
    val color: String,
    val tipoCombustible: String,
    val idUsuario: Int // Llave foránea para saber a qué usuario le pertenece el carro
)