package com.saludplus.citas.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.theme.AzulClaro
import com.saludplus.citas.ui.theme.AzulPrimario

data class OpcionBarra(val titulo: String, val icono: ImageVector, val ruta: String)

// barra de abajo con los 4 destinos; rutaActual dice cual se pinta de azul
@Composable
fun BarraInferior(navController: NavController, rutaActual: String) {
    val opciones = listOf(
        OpcionBarra("Inicio", Icons.Filled.Home, Rutas.HOME),
        OpcionBarra("Citas", Icons.Filled.DateRange, Rutas.MIS_CITAS),
        OpcionBarra("Resultados", Icons.Filled.Info, Rutas.RESULTADOS),
        OpcionBarra("Perfil", Icons.Filled.Person, Rutas.PERFIL)
    )

    NavigationBar(containerColor = Color.White) {
        opciones.forEach { opcion ->
            NavigationBarItem(
                selected = rutaActual == opcion.ruta,
                onClick = {
                    if (rutaActual != opcion.ruta) {
                        navController.navigate(opcion.ruta) {
                            // Inicio queda como base: no se apilan pantallas al ir y venir
                            popUpTo(Rutas.HOME)
                            launchSingleTop = true
                        }
                    }
                },
                icon = { Icon(opcion.icono, contentDescription = opcion.titulo) },
                label = { Text(opcion.titulo) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AzulPrimario,
                    selectedTextColor = AzulPrimario,
                    indicatorColor = AzulClaro
                )
            )
        }
    }
}