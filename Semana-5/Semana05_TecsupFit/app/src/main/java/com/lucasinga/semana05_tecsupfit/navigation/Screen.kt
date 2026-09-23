package com.lucasinga.semana05_tecsupfit.navigation

// sealed class: el compilador conoce todas las rutas posibles de la app
sealed class Screen(val route: String) {

    // pestañas del bottomBar
    object Inicio : Screen("inicio")
    object Reservas : Screen("reservas")
    object Rutinas : Screen("rutinas")
    object Perfil : Screen("perfil")

    // flujo secuencial: Inicio -> DetalleClase -> Confirmacion
    object DetalleClase : Screen("detalle/{claseId}") {
        fun createRoute(claseId: Int) = "detalle/$claseId"
    }

    // viaja el id de la clase y la posición del horario elegido
    object Confirmacion : Screen("confirmacion/{claseId}/{horarioIndex}") {
        fun createRoute(claseId: Int, horarioIndex: Int) = "confirmacion/$claseId/$horarioIndex"
    }
}