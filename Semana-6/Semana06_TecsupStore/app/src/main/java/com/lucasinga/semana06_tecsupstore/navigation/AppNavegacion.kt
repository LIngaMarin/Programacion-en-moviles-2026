package com.lucasinga.semana06_tecsupstore.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lucasinga.semana06_tecsupstore.screens.InicioScreen

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Inicio.route
    ) {
        composable(Screen.Inicio.route) {
            InicioScreen()
        }
    }
}