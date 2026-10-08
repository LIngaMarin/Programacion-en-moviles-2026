package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun CitaExitosaScreen(navController: NavController, citaId: Int) {
    PantallaEnConstruccion("Cita agendada") { navController.navigate(Rutas.MIS_CITAS) }
}
