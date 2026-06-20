package com.example.minave.modelos

data class Combustible(
    val id: Int? = null,
    val idVehiculo: Int,
    val fecha: String,
    val litros: Double,
    val costo: Double,
    val observaciones: String
)