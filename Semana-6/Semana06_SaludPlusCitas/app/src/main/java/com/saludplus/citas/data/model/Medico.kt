package com.saludplus.citas.data.model

data class Medico(
    val id: Int,
    val nombre: String,
    val especialidadId: Int,
    val titulo: String,
    val calificacion: Double,
    val resenas: Int,
    val disponibilidad: String,
    val cmp: String,
    // ids de los locales donde atiende (por defecto en todos)
    val locales: List<Int> = listOf(1, 2, 3, 4, 5, 6)
)