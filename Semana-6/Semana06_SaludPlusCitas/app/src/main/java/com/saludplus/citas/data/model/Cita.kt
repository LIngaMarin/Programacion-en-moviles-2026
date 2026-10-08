package com.saludplus.citas.data.model

data class Cita(
    val id: Int,
    val telefonoUsuario: String,
    val medicoId: Int,
    val fecha: String,
    val hora: String,
    val motivo: String
)