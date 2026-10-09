package com.saludplus.citas.navigation

import android.net.Uri

object Rutas {
    // auth
    const val SPLASH = "splash"
    const val REGISTRO = "registro"
    const val LOGIN = "login"
    const val TERMINOS = "terminos"

    // home
    const val HOME = "home"
    const val LOCALES = "locales"
    const val MIS_DOCTORES = "mis_doctores"

    // agendamiento (las que llevan {} reciben parametros)
    const val ESPECIALIDADES = "especialidades"
    const val MEDICOS = "medicos/{especialidadId}"
    const val FECHA_HORA = "fecha_hora/{medicoId}"
    const val CONFIRMAR = "confirmar/{medicoId}/{fecha}/{hora}"
    const val CITA_EXITOSA = "cita_exitosa/{citaId}"

    // citas, perfil, resultados, notificaciones
    const val MIS_CITAS = "mis_citas"
    const val DETALLE_CITA = "detalle_cita/{citaId}"
    const val PERFIL = "perfil"
    const val RESULTADOS = "resultados"
    const val NOTIFICACIONES = "notificaciones"

    // arman la ruta con el valor real
    fun medicos(especialidadId: Int) = "medicos/$especialidadId"
    fun fechaHora(medicoId: Int) = "fecha_hora/$medicoId"
    // Uri.encode: la fecha "Mar 16" tiene espacio y la hora "09:00" tiene ":"
    fun confirmar(medicoId: Int, fecha: String, hora: String) =
        "confirmar/$medicoId/${Uri.encode(fecha)}/${Uri.encode(hora)}"
    fun citaExitosa(citaId: Int) = "cita_exitosa/$citaId"
    fun detalleCita(citaId: Int) = "detalle_cita/$citaId"
}