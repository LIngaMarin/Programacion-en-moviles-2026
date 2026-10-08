package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun ConfirmarCitaScreen(navController: NavController, medicoId: Int, fecha: String, hora: String) {
    PantallaEnConstruccion("Confirmar cita ($fecha $hora)") {
        navController.navigate(Rutas.citaExitosa(1)) { popUpTo(Rutas.HOME) }
    }
}
