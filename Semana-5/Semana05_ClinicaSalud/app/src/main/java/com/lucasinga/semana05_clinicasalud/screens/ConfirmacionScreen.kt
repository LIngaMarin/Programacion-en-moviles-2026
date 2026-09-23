package com.lucasinga.semana05_clinicasalud.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lucasinga.semana05_clinicasalud.model.fechas
import com.lucasinga.semana05_clinicasalud.model.horas
import com.lucasinga.semana05_clinicasalud.model.medicos
import com.lucasinga.semana05_clinicasalud.navigation.Screen
import com.lucasinga.semana05_clinicasalud.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmacionScreen(navController: NavController, medicoId: Int, fechaIndex: Int, horaIndex: Int) {
    // con los 3 parámetros de la ruta se arma el resumen
    val medico = medicos.first { it.id == medicoId }
    val fecha = fechas[fechaIndex]
    val hora = horas[horaIndex]

    Scaffold(
        topBar = {
            // sin flecha de volver: la cita ya se agendó
            TopAppBar(title = { Text("Confirmación", fontWeight = FontWeight.Bold) })
        }
    ) { padding ->
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
                    .size(80.dp)
                    .background(VerdeClaro, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = null,
                    tint = VerdeConfirmada,
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(Modifier.height(20.dp))
            Text("¡Cita agendada!", fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Spacer(Modifier.height(8.dp))
            Text(medico.nombre, color = TextoGris)
            Text("${fecha.textoCompleto}, $hora", color = TextoGris)

            Spacer(Modifier.height(32.dp))

            Button(
                // inclusive = true: se limpia todo y queda un solo Inicio en el historial
                onClick = {
                    navController.navigate(Screen.Inicio.route) {
                        popUpTo(Screen.Inicio.route) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MoradoClinica)
            ) {
                Text("Volver al inicio")
            }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = {
                    navController.navigate(Screen.MisCitas.route) {
                        popUpTo(Screen.Inicio.route)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Ver mis citas", color = MoradoClinica)
            }
        }
    }
}