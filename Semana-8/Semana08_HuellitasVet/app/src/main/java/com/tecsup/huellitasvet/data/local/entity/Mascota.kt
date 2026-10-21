package com.tecsup.huellitasvet.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Tabla "mascotas". Cada mascota pertenece a un dueño (N mascotas -> 1 dueño).
 * duenoId es clave foranea hacia usuarios.id y tiene su indice.
 * especie y sexo se guardan como texto (Especie.name y Sexo.name).
 */
@Entity(
    tableName = "mascotas",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["duenoId"]
        )
    ],
    indices = [Index("duenoId")]
)
data class Mascota(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val duenoId: Int,
    val nombre: String,
    val especie: String,       // PERRO, GATO u OTRO
    val raza: String,
    val anioNacimiento: Int,
    val peso: Double,
    val sexo: String           // MACHO o HEMBRA
)