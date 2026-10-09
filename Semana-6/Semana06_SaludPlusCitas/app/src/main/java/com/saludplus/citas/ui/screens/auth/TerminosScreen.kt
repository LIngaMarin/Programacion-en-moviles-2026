package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrimario
import com.saludplus.citas.ui.theme.FondoApp
import com.saludplus.citas.ui.theme.TextoGris

@Composable
fun TerminosScreen(navController: NavController) {
    // cada seccion: titulo + texto
    val secciones = listOf(
        "1. Uso de la aplicacion" to "SaludPlus permite a los pacientes registrarse, buscar especialidades, elegir un medico y agendar citas en la clinica.",
        "2. Datos personales" to "El nombre, telefono y correo se usan solo para identificar al paciente y gestionar sus citas.",
        "3. Reserva de citas" to "Cada cita queda reservada para el medico, el dia y la hora elegidos. Ese horario deja de estar disponible para otros pacientes.",
        "4. Cancelaciones" to "El paciente puede cancelar una cita desde su detalle. Al cancelarla, el horario vuelve a quedar disponible.",
        "5. Emergencias" to "La informacion de la app no reemplaza la atencion medica. Ante una emergencia, acude directamente a la clinica."
    )

    Scaffold(
        topBar = { BarraSuperior("Terminos y condiciones") { navController.popBackStack() } },
        containerColor = FondoApp
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // verticalScroll: el texto es mas largo que la pantalla
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Al registrarte en SaludPlus aceptas lo siguiente:", color = TextoGris)
                secciones.forEach { seccion ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(seccion.first, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                            Text(seccion.second, color = TextoGris, fontSize = 14.sp)
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            BotonPrimario("Aceptar") { navController.popBackStack() }
        }
    }
}
