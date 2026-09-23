package com.lucasinga.semana05_tecsupfit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lucasinga.semana05_tecsupfit.model.reservasIniciales
import com.lucasinga.semana05_tecsupfit.screens.*

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // lista compartida: Detalle agrega, Reservas y Perfil la leen
    val reservas = remember { reservasIniciales.toMutableStateList() }

    NavHost(navController = navController, startDestination = Screen.Inicio.route) {
        // pestañas del bottomBar
        composable(Screen.Inicio.route) { InicioScreen(navController) }
        composable(Screen.Reservas.route) { ReservasScreen(navController, reservas) }
        composable(Screen.Rutinas.route) { RutinasScreen(navController) }
        composable(Screen.Perfil.route) { PerfilScreen(navController, reservas) }

        // flujo secuencial con parámetros Int
        composable(
            route = Screen.DetalleClase.route,
            arguments = listOf(navArgument("claseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val claseId = backStackEntry.arguments?.getInt("claseId") ?: 1
            DetalleClaseScreen(navController, claseId, reservas)
        }
        composable(
            route = Screen.Confirmacion.route,
            arguments = listOf(
                navArgument("claseId") { type = NavType.IntType },
                navArgument("horarioIndex") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val claseId = backStackEntry.arguments?.getInt("claseId") ?: 1
            val horarioIndex = backStackEntry.arguments?.getInt("horarioIndex") ?: 0
            ConfirmacionScreen(navController, claseId, horarioIndex)
        }
    }
}