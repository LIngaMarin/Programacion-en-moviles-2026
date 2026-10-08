package com.saludplus.citas.ui.screens.citas

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun DetalleCitaScreen(navController: NavController, citaId: Int) {
    PantallaEnConstruccion("Detalle de cita") { navController.popBackStack() }
}
