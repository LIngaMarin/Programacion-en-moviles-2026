package com.lucasinga.semana05_navegacion.navigation

sealed class Screen(val route: String) {

    object Home : Screen("home")
    object List : Screen("list")
    object Profile : Screen("profile")

    // ruta con argumento: {itemId} se reemplaza por el valor real al navegar
    object Detail : Screen("detail/{itemId}") {
        fun createRoute(itemId: Int): String = "detail/$itemId"
    }
}