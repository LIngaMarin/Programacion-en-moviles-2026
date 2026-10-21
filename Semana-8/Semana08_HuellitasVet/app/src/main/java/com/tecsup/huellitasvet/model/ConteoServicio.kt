package com.tecsup.huellitasvet.model

/**
 * Resultado de una consulta con GROUP BY: cuantas citas tiene cada servicio.
 * Room llena los campos por el nombre de las columnas del SELECT (nombre, cantidad).
 */
data class ConteoServicio(
    val nombre: String,
    val cantidad: Int
)
