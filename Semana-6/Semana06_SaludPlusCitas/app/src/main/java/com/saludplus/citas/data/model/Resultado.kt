package com.saludplus.citas.data.model

data class Resultado(
    val id: Int,
    val examen: String,
    val fecha: String,
    val medico: String,
    val estado: String
)
