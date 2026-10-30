package com.tecsup.huellitasvet.model

/** Tipo de servicio de la veterinaria. En la tabla servicios se guarda como texto (TipoServicio.name). */
enum class TipoServicio(val etiqueta: String) {
    CONSULTA("Consulta"),
    PREVENCION("Prevención"),
    ESTETICA("Estética"),
    CIRUGIA("Cirugía"),
    LABORATORIO("Laboratorio");

    companion object {
        fun de(nombre: String?): TipoServicio = entries.find { it.name == nombre } ?: CONSULTA
    }
}
