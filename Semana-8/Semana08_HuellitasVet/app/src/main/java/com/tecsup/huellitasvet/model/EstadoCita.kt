package com.tecsup.huellitasvet.model

/**
 * Estados de una cita. En la tabla citas se guarda como texto (EstadoCita.name).
 * PENDIENTE -> CONFIRMADA -> ATENDIDA. CANCELADA y ATENDIDA son estados finales.
 */
enum class EstadoCita(val etiqueta: String) {
    PENDIENTE("Pendiente"),
    CONFIRMADA("Confirmada"),
    ATENDIDA("Atendida"),
    CANCELADA("Cancelada");

    companion object {
        fun de(nombre: String): EstadoCita = entries.find { it.name == nombre } ?: PENDIENTE
    }
}
