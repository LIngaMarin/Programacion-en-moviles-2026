package com.lucasinga.semana06_tecsupstore.navigation

sealed class Screen(val route: String) {
    object Inicio : Screen("inicio")
}