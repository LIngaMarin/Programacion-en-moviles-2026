package com.saludplus.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun TerminosScreen(navController: NavController) {
    PantallaEnConstruccion("Terminos y condiciones") { navController.popBackStack() }
}
