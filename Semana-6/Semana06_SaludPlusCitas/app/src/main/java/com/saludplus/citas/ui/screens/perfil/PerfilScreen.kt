package com.saludplus.citas.ui.screens.perfil

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun PerfilScreen(navController: NavController) {
    PantallaEnConstruccion("Perfil") { navController.navigate(Rutas.SPLASH) }
}
