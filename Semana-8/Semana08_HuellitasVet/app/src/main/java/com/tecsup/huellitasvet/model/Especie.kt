package com.tecsup.huellitasvet.model

/** Especie de la mascota. En la tabla mascotas se guarda como texto (Especie.name). */
enum class Especie(val etiqueta: String) {
    PERRO("Perro"),
    GATO("Gato"),
    OTRO("Otro");

    companion object {
        fun de(nombre: String): Especie = entries.find { it.name == nombre } ?: OTRO
    }
}
