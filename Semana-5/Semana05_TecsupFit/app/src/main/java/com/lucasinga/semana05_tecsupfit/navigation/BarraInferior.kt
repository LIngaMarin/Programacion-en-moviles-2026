package com.lucasinga.semana05_tecsupfit.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lucasinga.semana05_tecsupfit.ui.theme.*

// cada pestaña del bottomBar: texto, ícono y ruta a la que lleva
data class Pestaña(val texto: String, val icono: ImageVector, val ruta: String)

val pestañas = listOf(
    Pestaña("Inicio", Icons.Filled.Home, Screen.Inicio.route),
    Pestaña("Reservas", Icons.Filled.DateRange, Screen.Reservas.route),
    Pestaña("Rutinas", Icons.AutoMirrored.Filled.List, Screen.Rutinas.route),
    Pestaña("Perfil", Icons.Filled.Person, Screen.Perfil.route)
)

// bottomBar compartido: cada pantalla principal lo pone en el bottomBar de su Scaffold
@Composable
fun BarraInferior(navController: NavController) {
    // ruta de la pantalla actual, así el bottomBar sabe qué ícono resaltar
    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route

    NavigationBar(containerColor = Color.White) {
        pestañas.forEach { pestaña ->
            NavigationBarItem(
                selected = rutaActual == pestaña.ruta,
                onClick = {
                    navController.navigate(pestaña.ruta) {
                        popUpTo(Screen.Inicio.route)
                        // evita abrir dos veces la misma pestaña
                        launchSingleTop = true
                    }
                },
                icon = { Icon(pestaña.icono, contentDescription = pestaña.texto) },
                label = { Text(pestaña.texto) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = VerdeFit,
                    selectedTextColor = VerdeFit,
                    indicatorColor = VerdeClaroFit
                )
            )
        }
    }
}