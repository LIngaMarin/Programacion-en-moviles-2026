package com.tecsup.huellitasvet.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Tabla "citas". Cada cita es de una mascota y de un servicio.
 * mascotaId -> mascotas.id y servicioId -> servicios.id (claves foraneas con su indice).
 * El indice (fecha, hora) acelera la consulta de horarios ocupados.
 * precio es una "foto" del precio del servicio al momento de pedir la cita.
 */
@Entity(
    tableName = "citas",
    foreignKeys = [
        ForeignKey(
            entity = Mascota::class,
            parentColumns = ["id"],
            childColumns = ["mascotaId"]
        ),
        ForeignKey(
            entity = Servicio::class,
            parentColumns = ["id"],
            childColumns = ["servicioId"]
        )
    ],
    indices = [
        Index("mascotaId"),
        Index("servicioId"),
        Index(value = ["fecha", "hora"])
    ]
)
data class Cita(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mascotaId: Int,
    val servicioId: Int,
    val fecha: String,                 // 'aaaa-MM-dd'
    val hora: String,                  // 'HH:mm'
    val nota: String = "",
    val estado: String,                // PENDIENTE, CONFIRMADA, ATENDIDA o CANCELADA
    val precio: Double,
    val diagnostico: String? = null,
    val tratamiento: String? = null,
    val pesoRegistrado: Double? = null,
    val fechaCreacion: Long = System.currentTimeMillis()
)