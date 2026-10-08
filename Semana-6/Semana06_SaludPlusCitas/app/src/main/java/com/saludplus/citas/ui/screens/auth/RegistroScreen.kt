package com.saludplus.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun RegistroScreen(navController: NavController) {
    PantallaEnConstruccion("Registro") { navController.navigate(Rutas.LOGIN) }
}
