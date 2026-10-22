package com.tecsup.huellitasvet.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Tabla "vacunas". Cada vacuna esta vinculada a una mascota (N vacunas -> 1 mascota).
 * mascotaId es clave foranea hacia mascotas.id y tiene su indice.
 * fecha: 'aaaa-MM-dd' (fecha de aplicacion)
 * proximaDosis: 'aaaa-MM-dd' opcional (puede ser nula)
 */
@Entity(
    tableName = "vacunas",
    foreignKeys = [
        ForeignKey(
            entity = Mascota::class,
            parentColumns = ["id"],
            childColumns = ["mascotaId"]
        )
    ],
    indices = [Index("mascotaId")]
)
data class Vacuna(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mascotaId: Int,
    val nombre: String,
    val fecha: String,           // 'aaaa-MM-dd'
    val proximaDosis: String? = null // 'aaaa-MM-dd' o null
)
