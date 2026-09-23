package com.lucasinga.semana05_clinicasalud.navigation

// sealed class: el compilador conoce todas las rutas posibles de la app

sealed class Screen(val route: String) {

    // destinos del drawer (menu lateral)
    object Inicio : Screen("inicio")
    object MisCitas : Screen("mis_citas")
    object Historial : Screen("historial")
    object Perfil : Screen("perfil")

    // flujo secuencial: Inicio -> PerfilMedico -> Agendar -> Confirmacion
    object PerfilMedico : Screen("perfil_medico/{medicoId}") {
        fun createRoute(medicoId: Int) = "perfil_medico/$medicoId"
    }

    object Agendar : Screen("agendar/{medicoId}") {
        fun createRoute(medicoId: Int) = "agendar/$medicoId"
    }

    // se mandan las posiciones de la fecha y hora elegidas en listas
    object Confirmacion : Screen("confirmacion/{medicoId}/{fechaIndex}/{horaIndex}") {
        fun createRoute(medicoId: Int, fechaIndex: Int, horaIndex: Int) =
            "confirmacion/$medicoId/$fechaIndex/$horaIndex"
    }
}