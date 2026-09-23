package com.lucasinga.semana05_clinicasalud.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.lucasinga.semana05_clinicasalud.model.citasIniciales
import com.lucasinga.semana05_clinicasalud.model.nombrePaciente
import com.lucasinga.semana05_clinicasalud.screens.*
import com.lucasinga.semana05_clinicasalud.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    // abrir y cerrar el drawer es una animación, por eso se hace con scope.launch
    val scope = rememberCoroutineScope()

    // lista de citas compartida: vive aquí arriba para que Agendar, Mis citas y Perfil usen la misma
    val citas = remember { citasIniciales.toMutableStateList() }

    // ruta de la pantalla actual, sirve para marcar la opción activa del drawer
    val rutaActual = navController.currentBackStackEntryAsState().value?.destination?.route

    val abrirMenu: () -> Unit = { scope.launch { drawerState.open() } }

    fun irA(ruta: String) {
        scope.launch { drawerState.close() }
        navController.navigate(ruta) {
            popUpTo(Screen.Inicio.route)
            // evita abrir dos veces la misma pantalla si ya estás en ella
            launchSingleTop = true
        }
    }

    // el drawer ENVUELVE al contenido (NavHost con las pantallas y sus Scaffold),
    // así el menú se dibuja encima de toda la pantalla, incluida la topBar
    ModalNavigationDrawer(
        drawerState = drawerState,
        // deslizar para abrir solo en las pantallas del menú, no en el flujo de agendar
        gesturesEnabled = rutaActual in listOf(
            Screen.Inicio.route, Screen.MisCitas.route, Screen.Historial.route, Screen.Perfil.route
        ),
        drawerContent = {
            ModalDrawerSheet {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(LilaClaro, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("LI", fontWeight = FontWeight.Bold, color = MoradoClinica)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(nombrePaciente, fontWeight = FontWeight.Bold)
                        Text("Paciente", fontSize = 12.sp, color = TextoGris)
                    }
                }
                HorizontalDivider()
                Spacer(Modifier.height(8.dp))

                ItemMenu("Inicio", Icons.Filled.Home, rutaActual == Screen.Inicio.route) {
                    irA(Screen.Inicio.route)
                }
                ItemMenu("Mis citas", Icons.Filled.DateRange, rutaActual == Screen.MisCitas.route) {
                    irA(Screen.MisCitas.route)
                }
                ItemMenu("Historial médico", Icons.AutoMirrored.Filled.List, rutaActual == Screen.Historial.route) {
                    irA(Screen.Historial.route)
                }
                ItemMenu("Perfil", Icons.Filled.Person, rutaActual == Screen.Perfil.route) {
                    irA(Screen.Perfil.route)
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Inicio.route
        ) {
            // destinos del drawer
            composable(Screen.Inicio.route) {
                InicioScreen(navController, abrirMenu)
            }
            composable(Screen.MisCitas.route) {
                MisCitasScreen(navController, citas, abrirMenu)
            }
            composable(Screen.Historial.route) {
                HistorialScreen(abrirMenu)
            }
            composable(Screen.Perfil.route) {
                PerfilScreen(citas, abrirMenu)
            }

            // flujo secuencial con parámetros tipados Int
            composable(
                route = Screen.PerfilMedico.route,
                arguments = listOf(navArgument("medicoId") { type = NavType.IntType })
            ) { backStackEntry ->
                val medicoId = backStackEntry.arguments?.getInt("medicoId") ?: 1
                PerfilMedicoScreen(navController, medicoId)
            }
            composable(
                route = Screen.Agendar.route,
                arguments = listOf(navArgument("medicoId") { type = NavType.IntType })
            ) { backStackEntry ->
                val medicoId = backStackEntry.arguments?.getInt("medicoId") ?: 1
                AgendarCitaScreen(navController, medicoId, citas)
            }
            composable(
                route = Screen.Confirmacion.route,
                arguments = listOf(
                    navArgument("medicoId") { type = NavType.IntType },
                    navArgument("fechaIndex") { type = NavType.IntType },
                    navArgument("horaIndex") { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val medicoId = backStackEntry.arguments?.getInt("medicoId") ?: 1
                val fechaIndex = backStackEntry.arguments?.getInt("fechaIndex") ?: 0
                val horaIndex = backStackEntry.arguments?.getInt("horaIndex") ?: 0
                ConfirmacionScreen(navController, medicoId, fechaIndex, horaIndex)
            }
        }
    }
}

@Composable
fun ItemMenu(texto: String, icono: ImageVector, seleccionado: Boolean, onClick: () -> Unit) {
    NavigationDrawerItem(
        label = { Text(texto) },
        icon = { Icon(icono, contentDescription = null) },
        selected = seleccionado,
        onClick = onClick,
        modifier = Modifier.padding(horizontal = 12.dp),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = LilaClaro,
            selectedTextColor = MoradoClinica,
            selectedIconColor = MoradoClinica
        )
    )
}