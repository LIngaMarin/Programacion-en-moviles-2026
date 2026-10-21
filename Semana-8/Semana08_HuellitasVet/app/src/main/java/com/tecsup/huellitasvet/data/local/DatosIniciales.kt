package com.tecsup.huellitasvet.data.local

import com.tecsup.huellitasvet.data.local.entity.Cita
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.model.Sexo
import java.time.LocalDate

/**
 * Datos de prueba que se cargan solo la primera vez que se abre la app.
 * Solo datos ficticios (Ley 29733).
 */
object DatosIniciales {

    suspend fun sembrar(db: VetDatabase) {
        // Si ya hay usuarios, la base ya fue sembrada antes: no se hace nada
        if (db.usuarioDao().contar() > 0) return

        // El orden importa por las claves foraneas: primero los "padres"
        db.servicioDao().insertarTodos(servicios)
        db.usuarioDao().insertarTodos(usuarios)
        db.mascotaDao().insertarTodas(mascotas)
        db.citaDao().insertarTodas(citas())
    }

    // ---------- Servicios (Desparasitacion empieza inactivo) ----------
    private val servicios = listOf(
        Servicio(id = 1, nombre = "Consulta general", descripcion = "Evaluación completa del estado de salud", duracionMin = 30, precio = 60.0, icono = "heart"),
        Servicio(id = 2, nombre = "Vacunación", descripcion = "Aplicación de vacunas según su calendario", duracionMin = 30, precio = 45.0, icono = "syringe"),
        Servicio(id = 3, nombre = "Baño y corte", descripcion = "Baño, secado y corte de pelo", duracionMin = 60, precio = 50.0, icono = "scissors"),
        Servicio(id = 4, nombre = "Desparasitación", descripcion = "Tratamiento antiparasitario interno y externo", duracionMin = 15, precio = 25.0, activo = false, icono = "heart")
    )

    // ---------- Usuarios: 2 dueños y 1 veterinario ----------
    private val usuarios = listOf(
        Usuario(id = 1, nombre = "Ana García", correo = "ana@correo.test", clave = "1234", rol = Rol.CLIENTE.name),
        Usuario(id = 2, nombre = "Luis Torres", correo = "luis@correo.test", clave = "1234", rol = Rol.CLIENTE.name),
        Usuario(id = 3, nombre = "Dra. Rojas", correo = "vet@huellitas.test", clave = "admin", rol = Rol.ADMIN.name)
    )

    // ---------- Mascotas: Luna y Max son de Ana; Rocky y Michi de Luis ----------
    private val mascotas = listOf(
        Mascota(id = 1, duenoId = 1, nombre = "Luna", especie = Especie.GATO.name, raza = "Siamesa", anioNacimiento = 2023, peso = 4.2, sexo = Sexo.HEMBRA.name),
        Mascota(id = 2, duenoId = 1, nombre = "Max", especie = Especie.PERRO.name, raza = "Labrador", anioNacimiento = 2021, peso = 24.5, sexo = Sexo.MACHO.name),
        Mascota(id = 3, duenoId = 2, nombre = "Rocky", especie = Especie.PERRO.name, raza = "Bulldog", anioNacimiento = 2020, peso = 18.0, sexo = Sexo.MACHO.name),
        Mascota(id = 4, duenoId = 2, nombre = "Michi", especie = Especie.GATO.name, raza = "Mestizo", anioNacimiento = 2024, peso = 3.6, sexo = Sexo.MACHO.name)
    )

    // ---------- Citas: pasadas (atendidas), de hoy y de los proximos dias ----------
    // Las fechas se calculan desde hoy para que siempre haya citas pasadas, de hoy y futuras
    private fun dia(n: Long): String = LocalDate.now().plusDays(n).toString()

    private fun citas() = listOf(
        // Pasadas ATENDIDAS con diagnostico, tratamiento y peso (para el historial clinico)
        Cita(mascotaId = 1, servicioId = 2, fecha = dia(-20), hora = "10:00", estado = EstadoCita.ATENDIDA.name, precio = 45.0,
            diagnostico = "Vacuna triple felina aplicada. Sin reacciones.", tratamiento = "Refuerzo en 12 meses", pesoRegistrado = 4.2),
        Cita(mascotaId = 1, servicioId = 1, fecha = dia(-45), hora = "09:30", estado = EstadoCita.ATENDIDA.name, precio = 60.0,
            diagnostico = "Control de rutina. Sin hallazgos.", tratamiento = "Ninguno", pesoRegistrado = 4.0),
        Cita(mascotaId = 1, servicioId = 3, fecha = dia(-80), hora = "11:00", estado = EstadoCita.ATENDIDA.name, precio = 50.0,
            diagnostico = "Higiene general. Pelaje en buen estado.", tratamiento = "Ninguno", pesoRegistrado = 3.9),
        Cita(mascotaId = 2, servicioId = 1, fecha = dia(-12), hora = "10:30", estado = EstadoCita.ATENDIDA.name, precio = 60.0,
            diagnostico = "Otitis leve en oído derecho.", tratamiento = "Gotas óticas por 7 días", pesoRegistrado = 24.5),
        Cita(mascotaId = 3, servicioId = 2, fecha = dia(-5), hora = "15:00", estado = EstadoCita.ATENDIDA.name, precio = 45.0,
            diagnostico = "Vacuna antirrábica anual aplicada.", tratamiento = "Control en 12 meses", pesoRegistrado = 18.0),
        // De hoy
        Cita(mascotaId = 1, servicioId = 2, fecha = dia(0), hora = "16:30", estado = EstadoCita.CONFIRMADA.name, precio = 45.0, nota = "Refuerzo de vacuna"),
        Cita(mascotaId = 3, servicioId = 1, fecha = dia(0), hora = "17:00", estado = EstadoCita.PENDIENTE.name, precio = 60.0),
        // Proximos dias
        Cita(mascotaId = 2, servicioId = 3, fecha = dia(1), hora = "10:00", estado = EstadoCita.PENDIENTE.name, precio = 50.0),
        Cita(mascotaId = 4, servicioId = 1, fecha = dia(2), hora = "11:00", estado = EstadoCita.PENDIENTE.name, precio = 60.0),
        Cita(mascotaId = 1, servicioId = 1, fecha = dia(3), hora = "15:30", estado = EstadoCita.CONFIRMADA.name, precio = 60.0),
        Cita(mascotaId = 3, servicioId = 3, fecha = dia(5), hora = "09:30", estado = EstadoCita.CANCELADA.name, precio = 50.0)
    )
}