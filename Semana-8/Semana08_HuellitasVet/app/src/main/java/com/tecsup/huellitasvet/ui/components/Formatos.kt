package com.tecsup.huellitasvet.ui.components

import java.time.LocalDate
import java.util.Locale

// ENTREGADO. Funciones para mostrar fechas y montos en español en las pantallas.

private val DIAS = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
private val MESES = listOf("ene", "feb", "mar", "abr", "may", "jun", "jul", "ago", "set", "oct", "nov", "dic")

/** "Mié" */
fun diaCorto(fecha: LocalDate): String = DIAS[fecha.dayOfWeek.value - 1]

/** "2026-10-21" -> "Mié 21 oct" */
fun fechaCorta(iso: String): String {
    val f = LocalDate.parse(iso)
    return "${diaCorto(f)} ${f.dayOfMonth} ${MESES[f.monthValue - 1]}"
}

/** "2026-10-21" -> "21 oct" */
fun diaMes(iso: String): String {
    val f = LocalDate.parse(iso)
    return "${f.dayOfMonth} ${MESES[f.monthValue - 1]}"
}

/** 3420.0 -> "S/ 3,420.00" */
fun soles(monto: Double): String = String.format(Locale.US, "S/ %,.2f", monto)

/** 4.3 -> "4.3 kg" */
fun kilos(peso: Double): String = String.format(Locale.US, "%.1f kg", peso)


/** Edad de la mascota: año actual - año de nacimiento. 2023 -> "3 años" */
fun edad(anioNacimiento: Int): String {
    val anios = LocalDate.now().year - anioNacimiento
    return when {
        anios <= 0 -> "Menos de 1 año"
        anios == 1 -> "1 año"
        else -> "$anios años"
    }
}