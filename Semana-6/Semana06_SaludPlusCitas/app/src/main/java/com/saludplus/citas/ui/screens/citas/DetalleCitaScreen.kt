package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.FilaDato
import com.saludplus.citas.ui.theme.FondoApp
import com.saludplus.citas.ui.theme.RojoError
import com.saludplus.citas.ui.theme.TextoGris
import com.saludplus.citas.util.textoFecha

@Composable
fun DetalleCitaScreen(navController: NavController, citaId: Int) {
    val cita = Repositorio.obtenerCita(citaId)
    val medico = Repositorio.obtenerMedico(cita?.medicoId ?: 0)
    val especialidad = Repositorio.obtenerEspecialidad(medico?.especialidadId ?: 0)
    // controla si el AlertDialog de confirmacion se muestra o no
    var mostrarDialogo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { BarraSuperior("Detalle de cita") { navController.popBackStack() } },
        containerColor = FondoApp
    ) { padding ->
        if (cita == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Esta cita ya no existe", color = TextoGris)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
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
                        FilaDato(Icons.Filled.DateRange, "Fecha", textoFecha(cita.fecha))
                        FilaDato(Icons.Filled.Notifications, "Hora", cita.hora)
                        FilaDato(Icons.Filled.Info, "Motivo", cita.motivo.ifBlank { "Sin motivo" })
                    }
                }

                Button(
                    onClick = { mostrarDialogo = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = RojoError)
                ) {
                    Text("Cancelar cita", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                }
            }

            if (mostrarDialogo) {
                AlertDialog(
                    onDismissRequest = { mostrarDialogo = false },
                    title = { Text("Cancelar cita") },
                    text = { Text("¿Seguro que quieres cancelar tu cita con ${medico?.nombre ?: ""}?") },
                    confirmButton = {
                        TextButton(onClick = {
                            // removeIf: la cita sale de la lista y su horario vuelve a estar libre
                            Repositorio.cancelarCita(citaId)
                            mostrarDialogo = false
                            navController.popBackStack()
                        }) {
                            Text("Si, cancelar", color = RojoError)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { mostrarDialogo = false }) {
                            Text("No")
                        }
                    }
                )
            }
        }
    }
}
