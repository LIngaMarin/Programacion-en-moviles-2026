package com.saludplus.citas.ui.screens.resultados

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun ResultadosScreen(navController: NavController) {
    PantallaEnConstruccion("Resultados") { navController.popBackStack() }
}
