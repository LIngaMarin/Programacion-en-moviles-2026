package com.lucasinga.semana06_tecsupstore.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lucasinga.semana06_tecsupstore.components.AppDrawer
import com.lucasinga.semana06_tecsupstore.screens.FavoritosScreen
import com.lucasinga.semana06_tecsupstore.screens.InicioScreen
import com.lucasinga.semana06_tecsupstore.screens.MisPedidosScreen
import com.lucasinga.semana06_tecsupstore.screens.PerfilScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    // abrir y cerrar el drawer es una animacion, por eso va con scope.launch
    val scope = rememberCoroutineScope()

    val abrirMenu: () -> Unit = { scope.launch { drawerState.open() } }
    val cerrarMenu: () -> Unit = { scope.launch { drawerState.close() } }

    fun irA(ruta: String) {
        cerrarMenu()
        navController.navigate(ruta) {
            // deja Inicio como base y no apila pantallas al ir y venir
            popUpTo(Screen.Inicio.route)
            // evita abrir dos veces la misma pantalla si ya estas en ella
            launchSingleTop = true
        }
    }

    // el drawer envuelve al NavHost, asi se dibuja encima de toda la pantalla
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                onNavegar = { ruta -> irA(ruta) },
                onCerrarMenu = cerrarMenu
            )
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Inicio.route
        ) {
            composable(Screen.Inicio.route) {
                InicioScreen(onMenuClick = abrirMenu)
            }
            composable(Screen.MisPedidos.route) {
                MisPedidosScreen(onMenuClick = abrirMenu)
            }
            composable(Screen.Favoritos.route) {
                FavoritosScreen(onMenuClick = abrirMenu)
            }
            composable(Screen.Perfil.route) {
                PerfilScreen(onMenuClick = abrirMenu)
            }
        }
    }
}