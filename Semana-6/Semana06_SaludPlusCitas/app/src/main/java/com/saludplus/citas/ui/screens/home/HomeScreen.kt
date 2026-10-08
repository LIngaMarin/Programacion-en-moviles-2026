package com.saludplus.citas.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun HomeScreen(navController: NavController) {
    PantallaEnConstruccion("Inicio") { navController.navigate(Rutas.ESPECIALIDADES) }
}
