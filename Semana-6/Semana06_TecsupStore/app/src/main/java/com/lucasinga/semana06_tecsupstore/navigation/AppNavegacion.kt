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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember

@Composable
fun AppNavegacion() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    // abrir y cerrar el drawer es una animacion, por eso va con scope.launch
    val scope = rememberCoroutineScope()
    val rutaActual = navController.currentBackStackEntryAsState().value?.destination?.route
    // lista compartida de favoritos: vive aqui arriba para que Inicio, Favoritos y el drawer usen la misma
    val favoritos = remember { mutableStateListOf<Int>() }

    fun cambiarFavorito(id: Int) {
        if (id in favoritos) favoritos.remove(id) else favoritos.add(id)
    }

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
                rutaActual = rutaActual,
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
                InicioScreen(
                    onMenuClick = abrirMenu,
                    favoritos = favoritos,
                    onFavorito = { id -> cambiarFavorito(id) }
                )
            }
            composable(Screen.MisPedidos.route) {
                MisPedidosScreen(onMenuClick = abrirMenu)
            }
            composable(Screen.Favoritos.route) {
                FavoritosScreen(onMenuClick = abrirMenu, favoritos = favoritos)
            }
            composable(Screen.Perfil.route) {
                PerfilScreen(onMenuClick = abrirMenu)
            }
        }
    }
}