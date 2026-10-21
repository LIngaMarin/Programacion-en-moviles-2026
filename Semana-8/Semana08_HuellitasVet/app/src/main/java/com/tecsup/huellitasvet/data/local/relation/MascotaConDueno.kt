package com.tecsup.huellitasvet.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Usuario

/**
 * Una mascota junto con su dueño.
 * @Embedded: los campos de la mascota. @Relation: Room busca el Usuario cuyo id = duenoId.
 */
data class MascotaConDueno(
    @Embedded val mascota: Mascota,
    @Relation(
        parentColumn = "duenoId",
        entityColumn = "id"
    )
    val dueno: Usuario
)