package com.tecsup.huellitasvet.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.tecsup.huellitasvet.data.local.entity.Cita
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Servicio

/**
 * Una cita con todo su detalle: la mascota (con su dueño) y el servicio.
 * Relacion anidada de dos niveles: cita -> mascota -> dueño.
 * entity = Mascota::class le dice a Room que la tabla es mascotas,
 * aunque el resultado sea MascotaConDueno.
 */
data class CitaConDetalle(
    @Embedded val cita: Cita,
    @Relation(
        entity = Mascota::class,
        parentColumn = "mascotaId",
        entityColumn = "id"
    )
    val mascota: MascotaConDueno,
    @Relation(
        parentColumn = "servicioId",
        entityColumn = "id"
    )
    val servicio: Servicio
)