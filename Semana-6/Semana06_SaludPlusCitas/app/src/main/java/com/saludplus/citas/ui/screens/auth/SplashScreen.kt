package com.saludplus.citas.ui.screens.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun SplashScreen(navController: NavController) {
    PantallaEnConstruccion("Splash") { navController.navigate(Rutas.REGISTRO) }
}
