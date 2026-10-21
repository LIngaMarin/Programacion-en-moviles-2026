package com.tecsup.huellitasvet.ui.navigation

/** ENTREGADO. Todas las rutas de la app y las funciones para armarlas con sus parametros. */
object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"

    // ---------- Grafos (uno por perfil) ----------
    const val GRAFO_DUENO = "grafo_dueno"
    const val GRAFO_VETERINARIO = "grafo_veterinario"

    // ---------- Dueño ----------
    const val INICIO = "inicio"
    const val MASCOTAS = "mascotas"
    const val FORMULARIO_MASCOTA = "formulario_mascota?mascotaId={mascotaId}"
    const val FICHA_MASCOTA = "ficha_mascota/{mascotaId}"
    const val PEDIR_CITA = "pedir_cita?mascotaId={mascotaId}"
    const val REPROGRAMAR_CITA = "reprogramar_cita/{citaId}"
    const val MIS_CITAS = "mis_citas"
    const val PERFIL = "perfil"

    // ---------- Veterinario ----------
    const val PANEL = "panel"
    const val AGENDA = "agenda"
    const val ATENDER_CITA = "atender_cita/{citaId}"
    const val PACIENTES = "pacientes"
    const val FICHA_PACIENTE = "ficha_paciente/{mascotaId}"
    const val SERVICIOS = "servicios"
    const val FORMULARIO_SERVICIO = "formulario_servicio?servicioId={servicioId}"
    const val AJUSTES = "ajustes"

    // ---------- Funciones para navegar con parametros ----------
    // id opcional: sin id = crear nuevo, con id = editar
    fun formularioMascota(mascotaId: Int? = null) =
        if (mascotaId == null) "formulario_mascota" else "formulario_mascota?mascotaId=$mascotaId"

    fun fichaMascota(mascotaId: Int) = "ficha_mascota/$mascotaId"

    fun pedirCita(mascotaId: Int? = null) =
        if (mascotaId == null) "pedir_cita" else "pedir_cita?mascotaId=$mascotaId"

    fun reprogramarCita(citaId: Int) = "reprogramar_cita/$citaId"

    fun atenderCita(citaId: Int) = "atender_cita/$citaId"

    fun fichaPaciente(mascotaId: Int) = "ficha_paciente/$mascotaId"

    fun formularioServicio(servicioId: Int? = null) =
        if (servicioId == null) "formulario_servicio" else "formulario_servicio?servicioId=$servicioId"
}
