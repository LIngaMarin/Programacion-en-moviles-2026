package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrimario
import com.saludplus.citas.ui.components.FilaDato
import com.saludplus.citas.ui.components.TarjetaMedico
import com.saludplus.citas.ui.theme.FondoApp
import com.saludplus.citas.ui.theme.RojoError
import com.saludplus.citas.ui.theme.TextoGris
import com.saludplus.citas.util.textoFecha

@Composable
fun ConfirmarCitaScreen(navController: NavController, medicoId: Int, fecha: String, hora: String) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val local = Repositorio.localActual
    var motivo by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    // la cita dura 30 min: la hora final es el siguiente horario de la lista ("09:30" -> "10:00")
    val indice = Repositorio.horariosBase.indexOf(hora)
    val horaFin = Repositorio.horariosBase.getOrNull(indice + 1) ?: "12:30"

    Scaffold(
        topBar = { BarraSuperior("Confirmar cita") { navController.popBackStack() } },
        containerColor = FondoApp
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (medico != null) {
                TarjetaMedico(medico, conCmp = true)
            }

            // resumen con los 3 parametros que llegaron por la ruta
            Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    FilaDato(Icons.Filled.DateRange, "Fecha", textoFecha(fecha))
                    FilaDato(Icons.Filled.Notifications, "Hora", "$hora a $horaFin")
                    FilaDato(Icons.Filled.Person, "Tipo de atencion", "Consulta presencial")
                    // la direccion ahora sale del local elegido
                    FilaDato(Icons.Filled.LocationOn, "Local", local?.nombre ?: "")
                    FilaDato(Icons.Filled.LocationOn, "Direccion", "${local?.direccion ?: ""}, ${local?.distrito ?: ""}")
                }
            }

            Text("Motivo de consulta (opcional)", fontWeight = FontWeight.SemiBold)
            OutlinedTextField(
                value = motivo,
                onValueChange = { motivo = it },
                placeholder = { Text("Consulta de rutina", color = TextoGris) },
                minLines = 3,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            if (error.isNotEmpty()) {
                Text(error, color = RojoError, fontSize = 14.sp)
            }

            BotonPrimario("Agendar cita") {
                val cita = Repositorio.agendarCita(medicoId, fecha, hora, motivo)
                if (cita != null) {
                    navController.navigate(Rutas.citaExitosa(cita.id)) {
                        // borra Especialidades, Medicos, Fecha y hora y Confirmar del historial:
                        // al presionar Atras desde Cita agendada se vuelve directo al Inicio
                        popUpTo(Rutas.HOME)
                    }
                } else {
                    error = "Ese horario ya fue reservado, elige otro"
                }
            }
        }
    }
}