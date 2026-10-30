package com.tecsup.huellitasvet.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla "usuarios": los dueños y el veterinario.
 * rol se guarda como texto con Rol.name (CLIENTE, ADMIN o ADMINISTRADOR).
 */
@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val correo: String,
    val clave: String,
    val rol: String,     // Rol.CLIENTE.name, Rol.ADMIN.name o Rol.ADMINISTRADOR.name
    val telefono: String = "",
    val distrito: String = ""
)