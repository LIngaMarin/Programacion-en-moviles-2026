package com.tecsup.huellitasvet.model

/**
 * Perfil del usuario. En la tabla usuarios se guarda como texto (Rol.name).
 * CLIENTE = Dueño de la mascota, ADMIN = Veterinario, ADMINISTRADOR = gestiona usuarios y mascotas.
 */
enum class Rol(val etiqueta: String) {
    CLIENTE("Dueño"),
    ADMIN("Veterinario"),
    ADMINISTRADOR("Admin");

    companion object {
        /** Convierte el texto guardado en Room/DataStore al enum (null si no coincide). */
        fun de(nombre: String?): Rol? = entries.find { it.name == nombre }
    }
}
