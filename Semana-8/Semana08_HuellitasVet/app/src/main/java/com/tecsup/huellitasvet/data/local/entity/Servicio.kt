package com.tecsup.huellitasvet.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tecsup.huellitasvet.model.TipoServicio

/** EJEMPLO RESUELTO. Entidad = tabla "servicios" de la base de datos. */
@Entity(tableName = "servicios")
data class Servicio(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val duracionMin: Int,
    val precio: Double,
    val activo: Boolean = true,
    val icono: String,     // heart, syringe, scissors
    val tipo: String = TipoServicio.CONSULTA.name   // CONSULTA, PREVENCION, ESTETICA, CIRUGIA o LABORATORIO
)
