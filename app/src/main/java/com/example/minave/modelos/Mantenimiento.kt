package com.example.minave.modelos

data class Mantenimiento(
    val id: Int = 0,
    val idVehiculo: Int,
    val tipoMantenimiento: String,
    val fecha: String,
    val kilometraje: Int,
    val proximoKilometraje: Int,
    val costo: Double,
    val taller: String,
    val observaciones: String
)
