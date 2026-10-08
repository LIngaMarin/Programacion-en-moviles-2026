package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.PantallaEnConstruccion

@Composable
fun FechaHoraScreen(navController: NavController, medicoId: Int) {
    PantallaEnConstruccion("Fecha y hora (medico $medicoId)") {
        navController.navigate(Rutas.confirmar(medicoId, "Lun 15", "08:00"))
    }
}
