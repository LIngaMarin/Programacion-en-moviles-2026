package com.lucasinga.semana05_tecsupfit.model

// horario de una clase con sus cupos
data class Horario(
    val hora: String,
    val cuposDisponibles: Int,
    val cuposTotales: Int
)

// clase del gimnasio; dia es "Hoy" o "Esta semana" y se usa para el filtro
data class Clase(
    val id: Int,
    val nombre: String,
    val sala: String,
    val duracion: Int,
    val instructor: String,
    val dia: String,
    val descripcion: String,
    val horarios: List<Horario>
)

// reserva hecha, estado puede ser "Confirmada" o "Completada"
data class Reserva(
    val clase: String,
    val dia: String,
    val hora: String,
    val sala: String,
    val estado: String
)

data class Rutina(
    val nombre: String,
    val nivel: String,
    val ejercicios: Int,
    val duracion: Int
)