package com.saludplus.citas.ui.screens.notificaciones

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun NotificacionesScreen(navController: NavController) {
    PantallaEnConstruccion("Notificaciones") { navController.popBackStack() }
}
