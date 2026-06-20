package com.example.minave.modelos

data class Certificado(
    val id: Int? = null,
    val idVehiculo: Int,
    val tipoDocumento: String,
    val fechaEmision: String,
    val fechaVencimiento: String,
    val empresaEmisora: String,
    val observaciones: String
)
