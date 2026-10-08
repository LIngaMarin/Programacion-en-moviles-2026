package com.saludplus.citas.ui.screens.citas

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun MisCitasScreen(navController: NavController) {
    PantallaEnConstruccion("Mis citas") { navController.navigate(Rutas.PERFIL) }
}
