package com.tecsup.huellitasvet.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla "tipos_servicio": los tipos que crea el administrador (Consulta, Prevencion, Estetica...).
 * Cada servicio apunta a un tipo con servicios.tipoId (N servicios -> 1 tipo).
 */
@Entity(tableName = "tipos_servicio")
data class TipoServicio(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String
)
