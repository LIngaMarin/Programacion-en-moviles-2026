package com.tecsup.huellitasvet.model

import java.time.LocalTime

/**
 * Horarios de atencion de la clinica: de 09:00 a 17:30 cada 30 minutos (18 horarios).
 * Se guardan como texto 'HH:mm' (LocalTime.toString() devuelve "09:00", "09:30", ...).
 */
object Horarios {
    private val INICIO: LocalTime = LocalTime.of(9, 0)
    private const val CANTIDAD = 18
    private const val INTERVALO_MIN = 30L

    val delDia: List<String> = (0 until CANTIDAD).map { i ->
        INICIO.plusMinutes(INTERVALO_MIN * i).toString()
    }
}
