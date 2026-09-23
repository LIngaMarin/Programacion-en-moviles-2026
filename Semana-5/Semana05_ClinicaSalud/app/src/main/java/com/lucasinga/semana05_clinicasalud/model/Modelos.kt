package com.lucasinga.semana05_clinicasalud.model

// datos de cada medico de la lista
data class Medico(
    val id: Int,
    val nombre: String,
    val especialidad: String, // se usa para filtrar con los chips (ej. "Cardiologia")
    val cargo: String, // lo que se muestra en la tarjeta (ej. "Cardiologia")
    val calificacion: Double,
    val reseñas: Int,
    val experiencia: Int,
    val descripcion: String
)

// opcion de fecha para agendar, se muestra como chip ("Vie" / "27")
data class OpcionFecha(
    val dia: String,
    val numero: String,
    val textoCompleto: String // se usa en el resumen: "Viernes 27"
)

// cita agendada, estado puede ser "Confirmada" o "Completada"
data class Cita(
    val medico: String,
    val especialidad: String,
    val fecha: String,
    val hora: String,
    val estado: String
)

// registro del historial medico del paciente
data class RegistroHistorial(
    val fecha: String,
    val motivo: String,
    val medico: String
)