package com.saludplus.citas.util

import java.time.DayOfWeek
import java.time.LocalDate

private val nombresDias = listOf("Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo")
private val nombresMeses = listOf(
    "enero", "febrero", "marzo", "abril", "mayo", "junio",
    "julio", "agosto", "setiembre", "octubre", "noviembre", "diciembre"
)

// 5 dias habiles a partir de hoy (semana 0) o de las semanas siguientes (1, 2...)
fun diasHabiles(semana: Int): List<LocalDate> {
    val dias = mutableListOf<LocalDate>()
    var dia = LocalDate.now().plusWeeks(semana.toLong())
    while (dias.size < 5) {
        // se saltan sabados y domingos
        if (dia.dayOfWeek != DayOfWeek.SATURDAY && dia.dayOfWeek != DayOfWeek.SUNDAY) {
            dias.add(dia)
        }
        dia = dia.plusDays(1)
    }
    return dias
}

// "Lun", "Mar", "Mie"...
fun diaCorto(fecha: LocalDate): String {
    return nombresDias[fecha.dayOfWeek.value - 1].take(3)
}

// "Octubre 2026"
fun textoMes(fecha: LocalDate): String {
    val mes = nombresMeses[fecha.monthValue - 1].replaceFirstChar { it.uppercase() }
    return "$mes ${fecha.year}"
}