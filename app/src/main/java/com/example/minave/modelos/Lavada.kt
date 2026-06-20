package com.example.minave.modelos

import java.io.Serializable

data class Lavada(
    val id: Int = 0,
    val idVehiculo: Int,
    val fecha: String,
    val tipo: String,
    val lugar: String,
    val costo: Double,
    val observaciones: String
) : Serializable
