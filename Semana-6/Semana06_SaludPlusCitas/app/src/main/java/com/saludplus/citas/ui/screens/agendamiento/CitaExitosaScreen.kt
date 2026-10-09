package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BotonPrimario
import com.saludplus.citas.ui.components.FilaDato
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.FondoApp
import com.saludplus.citas.ui.theme.TextoGris
import com.saludplus.citas.ui.theme.VerdeClaro
import com.saludplus.citas.ui.theme.VerdeDisponible
import com.saludplus.citas.util.textoFecha

@Composable
fun CitaExitosaScreen(navController: NavController, citaId: Int) {
    // con el id que llego se busca la cita recien guardada, y con ella el medico y la especialidad
    val cita = Repositorio.obtenerCita(citaId)
    val medico = Repositorio.obtenerMedico(cita?.medicoId ?: 0)
    val especialidad = Repositorio.obtenerEspecialidad(medico?.especialidadId ?: 0)

    Scaffold(containerColor = FondoApp) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .background(VerdeClaro, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = VerdeDisponible,
                    modifier = Modifier.size(64.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            Text("¡Cita agendada!", fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text("Te esperamos en la clinica", color = TextoGris)
            Spacer(Modifier.height(24.dp))

            // resumen de la cita
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    FilaDato(Icons.Filled.Person, "Medico", medico?.nombre ?: "")
                    FilaDato(Icons.Filled.Favorite, "Especialidad", especialidad?.nombre ?: "")
                    FilaDato(Icons.Filled.DateRange, "Fecha", if (cita != null) textoFecha(cita.fecha) else "")
                    FilaDato(Icons.Filled.Notifications, "Hora", cita?.hora ?: "")
                }
            }
            Spacer(Modifier.height(24.dp))

            BotonPrimario("Ver mis citas") {
                navController.navigate(Rutas.MIS_CITAS) {
                    popUpTo(Rutas.HOME)
                }
            }
            TextButton(onClick = {
                // regresa hasta Inicio (que quedo como base gracias al popUpTo de Confirmar)
                navController.popBackStack(Rutas.HOME, inclusive = false)
            }) {
                Text("Volver al inicio", color = AzulPrimario)
            }
        }
    }
}