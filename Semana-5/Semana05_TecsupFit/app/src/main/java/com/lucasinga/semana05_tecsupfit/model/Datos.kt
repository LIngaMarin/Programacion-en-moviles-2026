package com.lucasinga.semana05_tecsupfit.model

// datos de ejemplo, todo es local (no hay base de datos)

val nombreUsuario = "Lucas Inga Marín"
val planUsuario = "Plan Premium"
val rachaDias = 3
val clasesAnteriores = 13 // clases tomadas antes de usar la app

// chips de la LazyRow del Inicio
val filtros = listOf("Hoy", "Esta semana")

val clases = listOf(
    Clase(1, "Yoga funcional", "Sala 2", 50, "Ana Ruiz", "Hoy",
        "Posturas y respiración para mejorar la flexibilidad y el equilibrio.",
        listOf(Horario("7:00 am", 6, 15), Horario("12:00 pm", 10, 15), Horario("6:30 pm", 3, 15))),
    Clase(2, "Cross Training", "Sala 1", 45, "Carlos Méndez", "Hoy",
        "Entrenamiento funcional de alta intensidad. Cupos limitados.",
        listOf(Horario("6:00 pm", 8, 12), Horario("7:00 pm", 4, 12), Horario("8:00 pm", 10, 12))),
    Clase(3, "Spinning", "Sala 3", 40, "Lucía Paredes", "Hoy",
        "Ciclismo indoor con música para trabajar la resistencia.",
        listOf(Horario("7:30 am", 12, 20), Horario("5:00 pm", 7, 20), Horario("7:30 pm", 2, 20))),
    Clase(4, "Funcional HIIT", "Sala 1", 30, "Carlos Méndez", "Esta semana",
        "Intervalos cortos de alta intensidad para quemar calorías.",
        listOf(Horario("6:00 am", 10, 12), Horario("1:00 pm", 5, 12), Horario("9:00 pm", 9, 12))),
    Clase(5, "Pilates", "Sala 2", 55, "Ana Ruiz", "Esta semana",
        "Fortalecimiento del core, postura y control del movimiento.",
        listOf(Horario("8:00 am", 4, 10), Horario("11:00 am", 6, 10), Horario("5:30 pm", 8, 10)))
)

// reserva que ya existía, para que "Mis reservas" no empiece vacío
val reservasIniciales = listOf(
    Reserva("Yoga funcional", "Ayer", "7:00 am", "Sala 2", "Completada")
)

val rutinas = listOf(
    Rutina("Tren superior", "Principiante", 6, 30),
    Rutina("Piernas y glúteos", "Intermedio", 8, 40),
    Rutina("Core y abdomen", "Principiante", 5, 20),
    Rutina("Full body", "Avanzado", 10, 50)
)