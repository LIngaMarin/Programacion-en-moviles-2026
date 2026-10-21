package com.tecsup.huellitasvet.model

/** Sexo de la mascota. En la tabla mascotas se guarda como texto (Sexo.name). */
enum class Sexo(val etiqueta: String) {
    MACHO("Macho"),
    HEMBRA("Hembra");

    companion object {
        fun de(nombre: String): Sexo = entries.find { it.name == nombre } ?: MACHO
    }
}
