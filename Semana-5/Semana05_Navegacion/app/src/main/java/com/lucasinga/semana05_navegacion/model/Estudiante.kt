package com.lucasinga.semana05_navegacion.model

data class Estudiante(
    val id: Int,
    val nombre: String,
    val carrera: String,
    val codigo: String,
    val correo: String,
    val facultad: String,
    val biografia: String,
    val fotoUrl: String
)

val estudiantes = listOf(
    Estudiante(
        id = 1,
        nombre = "Lucas Inga Marín",
        carrera = "Ingeniería de Sistemas",
        codigo = "2024-0001",
        correo = "lucas.inga@example.com",
        facultad = "Ingeniería y Tecnología",
        biografia = "Estudiante destacado con interés en desarrollo Android.",
        fotoUrl = "https://randomuser.me/api/portraits/men/32.jpg"
    ),
    Estudiante(
        id = 2,
        nombre = "Maria Garcia",
        carrera = "Arquitectura",
        codigo = "2024-0002",
        correo = "maria.garcia@example.com",
        facultad = "Arquitectura y Urbanismo",
        biografia = "Apasionada por el diseño sostenible y los espacios urbanos.",
        fotoUrl = "https://randomuser.me/api/portraits/women/44.jpg"
    ),
    Estudiante(
        id = 3,
        nombre = "Carlos Perez",
        carrera = "Medicina",
        codigo = "2024-0003",
        correo = "carlos.perez@example.com",
        facultad = "Ciencias de la Salud",
        biografia = "Interesado en la investigación clínica y la salud pública.",
        fotoUrl = "https://randomuser.me/api/portraits/men/75.jpg"
    ),
    Estudiante(
        id = 4,
        nombre = "Ana Lopez",
        carrera = "Derecho",
        codigo = "2024-0004",
        correo = "ana.lopez@example.com",
        facultad = "Derecho y Ciencias Políticas",
        biografia = "Enfocada en derecho corporativo y resolución de conflictos.",
        fotoUrl = "https://randomuser.me/api/portraits/women/68.jpg"
    ),
    Estudiante(
        id = 5,
        nombre = "Luis Ramirez",
        carrera = "Administración",
        codigo = "2024-0005",
        correo = "luis.ramirez@example.com",
        facultad = "Ciencias Empresariales",
        biografia = "Orientado a la gestión de proyectos y el emprendimiento.",
        fotoUrl = "https://randomuser.me/api/portraits/men/51.jpg"
    )
)
