package com.tecsup.huellitasvet.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * EJEMPLO RESUELTO. Entidad = tabla "servicios" de la base de datos.
 * tipoId -> tipos_servicio.id (opcional). Si el administrador elimina el tipo,
 * onDelete = SET_NULL deja el servicio "Sin tipo" en vez de borrarlo.
 */
@Entity(
    tableName = "servicios",
    foreignKeys = [
        ForeignKey(
            entity = TipoServicio::class,
            parentColumns = ["id"],
            childColumns = ["tipoId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("tipoId")]
)
data class Servicio(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val duracionMin: Int,
    val precio: Double,
    val activo: Boolean = true,
    val icono: String,     // heart, syringe, scissors
    val tipoId: Int? = null   // null = Sin tipo
)
