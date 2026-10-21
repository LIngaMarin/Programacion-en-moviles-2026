package com.tecsup.huellitasvet.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.dueno.SplashScreen
import com.tecsup.huellitasvet.ui.theme.VetTheme
import com.tecsup.huellitasvet.ui.veterinario.ServiciosScreen
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel
import com.tecsup.huellitasvet.viewmodel.vetViewModel

/**
 * GUIADO. Grafo de navegacion de la app.
 * Por ahora: Splash -> Servicios (ejemplo resuelto). El resto se agrega paso a paso.
 */
@Composable
fun NavVet(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Rutas.SPLASH) {

        composable(Rutas.SPLASH) {
            // TODO (Paso 6): "Comenzar" y "Ya tengo una cuenta" van al Login;
            // si ya hay un perfil guardado en DataStore, saltar directo a Inicio o Panel
            SplashScreen(
                onComenzar = { navController.navigate(Rutas.SERVICIOS) },
                onYaTengoCuenta = { navController.navigate(Rutas.SERVICIOS) }
            )
        }

        // TODO (Paso 6): composable(Rutas.LOGIN)
        // TODO (Pasos 7 al 10 y 13): grafo del dueño (Rutas.GRAFO_DUENO) con su barra inferior
        // TODO (Pasos 11 al 13): grafo del veterinario (Rutas.GRAFO_VETERINARIO) con su barra inferior

        // Ejemplo resuelto: pantalla de servicios con los colores del veterinario
        composable(Rutas.SERVICIOS) {
            VetTheme(rol = Rol.ADMIN) {
                val vm = vetViewModel { app -> ServiciosViewModel(app.repository) }
                ServiciosScreen(
                    vm = vm,
                    onEditar = { /* TODO (Paso 13): navController.navigate(Rutas.formularioServicio(it)) */ },
                    onNuevo = { /* TODO (Paso 13): navController.navigate(Rutas.formularioServicio()) */ },
                    onAtras = { navController.popBackStack() }
                )
            }
        }
    }
}
