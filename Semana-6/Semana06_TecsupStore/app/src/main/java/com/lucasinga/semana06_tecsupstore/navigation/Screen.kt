package com.lucasinga.semana06_tecsupstore.navigation

sealed class Screen(val route: String) {
    object Inicio : Screen("inicio")
    object MisPedidos: Screen("mis_pedidos")
    object Favoritos: Screen("favoritos")
    object Perfil: Screen("perfil")

}