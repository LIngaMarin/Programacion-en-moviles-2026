package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun EspecialidadesScreen(navController: NavController) {
    PantallaEnConstruccion("Especialidades") { navController.navigate(Rutas.medicos(3)) }
}
