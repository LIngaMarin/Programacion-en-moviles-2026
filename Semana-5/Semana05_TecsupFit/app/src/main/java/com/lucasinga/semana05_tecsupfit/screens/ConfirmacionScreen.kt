package com.lucasinga.semana05_tecsupfit.screens

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
import com.lucasinga.semana05_tecsupfit.model.clases
import com.lucasinga.semana05_tecsupfit.navigation.Screen
import com.lucasinga.semana05_tecsupfit.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmacionScreen(navController: NavController, claseId: Int, horarioIndex: Int) {
    // con los 2 parámetros de la ruta se arma el resumen
    val clase = clases.first { it.id == claseId }
    val horario = clase.horarios[horarioIndex]

    Scaffold(
        topBar = {
            // sin flecha de volver: el cupo ya se reservó
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
                    .background(VerdeClaroFit, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Check,
                    contentDescription = null,
                    tint = VerdeFit,
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(Modifier.height(20.dp))
            Text("¡Cupo reservado!", fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Spacer(Modifier.height(8.dp))
            Text(clase.nombre, color = TextoGris)
            Text("${clase.dia}, ${horario.hora} · ${clase.sala}", color = TextoGris)

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    navController.navigate(Screen.Reservas.route) {
                        popUpTo(Screen.Inicio.route)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VerdeFit)
            ) {
                Text("Ver mis reservas")
            }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                // inclusive = true: se limpia todo y queda un solo Inicio en el historial
                onClick = {
                    navController.navigate(Screen.Inicio.route) {
                        popUpTo(Screen.Inicio.route) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Volver al inicio", color = VerdeFit)
            }
        }
    }
}