package com.lucasinga.semana05_clinicasalud.model

// datos de ejemplo, todo es local (no hay base de datos)

val nombrePaciente = "Lucas Inga Marin"

// chips de la LazyRoz del Inicio, "Todos" muestra todos los medicos

val especialidades = listOf("Todos", "Cardiologia", "Pediatria", "Dermatologia")

val medicos = listOf(
    Medico(1, "Dra. Ana Torres", "Cardiologia", "Cardiologa", 4.9, 128, 12, "Especialista en arritmias e hipertension, formacion en la Clinica Mayo."),
    Medico(2, "Dr. Luis Vega", "Pediatria", "Pediatra", 4.7, 96, 9, "Atencion integral del niño, control de crecimiento y desarrollo."),
    Medico(3, "Dra. Rosa Diaz", "Dermatologia", "Dermatologa", 4.8, 110, 10, "Tratamiento de acne, alergias de la piel y dermatologia estetica."),
    Medico(4, "Dr. Jorge Salas", "Cardiologia", "Cardiologo", 4.6, 74, 15, "Prevencion cardiovascular y control de presion arterial"),
    Medico(5, "Dra. Carmen Rios", "Pediatria", "Pediatra", 4.8, 88, 7, "Vacunacion, control del niño sano y enfermedades respiratorias."),
)

// opciones para agendar (seleccion unica)
val fechas = listOf(
    OpcionFecha("Jue", "26", "Jueves 26"),
    OpcionFecha("Vie", "27", "Viernes 27"),
    OpcionFecha("Sab", "28", "Sabado 28")
)

val horas = listOf("9:00 am", "10:30 am", "3:00 pm")

// cita que ya existia antes de usar la app, para que "Mis citas" no empiece vacio

val citasIniciales = listOf(
    Cita("Dr. Luis Vega", "Pediatria", "Miercoles 15", "3:00 pm", "Completada")
)

val historial = listOf(
    RegistroHistorial("15/09/2026", "Control general", "Dr. Luis Vega"),
    RegistroHistorial("02/08/2026", "Evaluacion de la piel", "Dra. Rosa Diaz"),
    RegistroHistorial("20/06/2026", "Electrocardiograma de rutina", "Dra. Ana Torres"),
)