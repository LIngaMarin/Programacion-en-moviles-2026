package com.saludplus.citas.data.repository

import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.model.Usuario

// object: una sola instancia para toda la app, asi todas las pantallas usan las mismas listas
object Repositorio {

    // ---------- Usuarios ----------
    val usuarios = mutableListOf(
        Usuario("Lucas Inga Marin", "987654321", "lucas@tecsup.edu.pe", "123456")
    )
    var usuarioActual: Usuario? = null

    // ---------- Especialidades ----------
    val especialidades = listOf(
        Especialidad(1, "Medicina General", "Atencion integral", "🩺"),
        Especialidad(2, "Pediatria", "Niños y adolescentes", "🧒"),
        Especialidad(3, "Ginecologia", "Salud de la mujer", "🌸"),
        Especialidad(4, "Cardiologia", "Corazon y vasos sanguineos", "❤️"),
        Especialidad(5, "Dermatologia", "Piel, cabello y uñas", "🧴"),
        Especialidad(6, "Traumatologia", "Huesos y articulaciones", "🦴"),
        Especialidad(7, "Oftalmologia", "Salud visual", "👁️")
    )

    // ---------- Medicos ----------
    val medicos = listOf(
        Medico(1, "Dr. Carlos Ramos", 1, "Medico general", 4.6, 64, "Disponible hoy", "23451"),
        Medico(2, "Dra. Lucia Vega", 1, "Medica general", 4.8, 102, "Disponible mañana", "23452"),
        Medico(3, "Dr. Jorge Salas", 2, "Pediatra", 4.7, 85, "Disponible hoy", "34561"),
        Medico(4, "Dra. Ana Torres", 3, "Ginecologa", 4.9, 120, "Disponible hoy", "12345"),
        Medico(5, "Dra. Claudia Rojas", 3, "Ginecologa", 4.8, 95, "Disponible mañana", "12346"),
        Medico(6, "Dr. Luis Ramirez", 3, "Ginecologo", 4.7, 88, "Disponible hoy", "12347"),
        Medico(7, "Dra. Mariana Soto", 3, "Ginecologa", 4.6, 76, "Disponible esta semana", "12348"),
        Medico(8, "Dr. Miguel Paredes", 4, "Cardiologo", 4.8, 110, "Disponible hoy", "45671"),
        Medico(9, "Dra. Sofia Medina", 5, "Dermatologa", 4.5, 58, "Disponible mañana", "56781"),
        Medico(10, "Dr. Raul Castillo", 6, "Traumatologo", 4.4, 47, "Disponible hoy", "67891"),
        Medico(11, "Dra. Elena Quispe", 7, "Oftalmologa", 4.7, 69, "Disponible esta semana", "78901")
    )

    // ---------- Fechas y horarios (lista fija en la Fase 1) ----------
    val diasDisponibles = listOf("Lun 15", "Mar 16", "Mie 17", "Jue 18", "Vie 19")
    val horariosBase = listOf("08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00")

    // ---------- Citas ----------
    // cita de otro paciente: ese horario ya no debe salir disponible
    val citas = mutableListOf(
        Cita(1, "999888777", 4, "Mar 16", "09:00", "Control")
    )
    private var siguienteIdCita = 2

    // ================= FUNCIONES =================

    fun registrarUsuario(nombres: String, telefono: String, correo: String, contrasena: String): Boolean {
        // any: revisa si ya hay alguien con ese telefono
        if (usuarios.any { it.telefono == telefono }) return false
        val nuevo = Usuario(nombres, telefono, correo, contrasena)
        usuarios.add(nuevo)
        // al registrarse ya queda con la sesion iniciada
        usuarioActual = nuevo
        return true
    }

    fun iniciarSesion(telefono: String, contrasena: String): Boolean {
        // find: devuelve el usuario que coincide, o null si no hay ninguno
        val usuario = usuarios.find { it.telefono == telefono && it.contrasena == contrasena }
        usuarioActual = usuario
        return usuario != null
    }

    fun cerrarSesion() {
        usuarioActual = null
    }

    fun buscarEspecialidades(texto: String): List<Especialidad> {
        // contains con ignoreCase: "card" encuentra "Cardiologia"
        return especialidades.filter { it.nombre.contains(texto, ignoreCase = true) }
    }

    fun especialidadesDestacadas(): List<Especialidad> {
        // take(3): solo las 3 primeras para la fila del Inicio
        return especialidades.take(3)
    }

    fun obtenerEspecialidad(id: Int): Especialidad? {
        return especialidades.find { it.id == id }
    }

    fun obtenerMedico(id: Int): Medico? {
        return medicos.find { it.id == id }
    }

    fun obtenerCita(id: Int): Cita? {
        return citas.find { it.id == id }
    }

    fun medicosPorEspecialidad(especialidadId: Int): List<Medico> {
        // solo los de esa especialidad, el mejor calificado primero
        return medicos
            .filter { it.especialidadId == especialidadId }
            .sortedByDescending { it.calificacion }
    }

    fun buscarMedicos(especialidadId: Int, texto: String): List<Medico> {
        return medicosPorEspecialidad(especialidadId)
            .filter { it.nombre.contains(texto, ignoreCase = true) }
    }

    fun horariosDisponibles(medicoId: Int, fecha: String): List<String> {
        // 1. horas ya reservadas con ese medico ese dia (filter + map)
        val ocupados = citas
            .filter { it.medicoId == medicoId && it.fecha == fecha }
            .map { it.hora }
        // 2. de todos los horarios, solo los que no estan ocupados (filter)
        return horariosBase.filter { it !in ocupados }
    }

    fun agendarCita(medicoId: Int, fecha: String, hora: String, motivo: String): Cita? {
        val usuario = usuarioActual
        if (usuario == null) return null
        // any: si ese medico ya tiene una cita ese dia a esa hora, no se puede agendar
        if (citas.any { it.medicoId == medicoId && it.fecha == fecha && it.hora == hora }) return null

        val cita = Cita(siguienteIdCita, usuario.telefono, medicoId, fecha, hora, motivo)
        siguienteIdCita++
        citas.add(cita)
        return cita
    }

    fun citasDelUsuario(): List<Cita> {
        // TODO: filter + sortedWith
        return emptyList()
    }

    fun cancelarCita(citaId: Int) {
        // TODO: removeIf
    }
}