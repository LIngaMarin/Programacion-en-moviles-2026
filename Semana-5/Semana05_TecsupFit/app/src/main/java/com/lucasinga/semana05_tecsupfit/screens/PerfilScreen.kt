package com.lucasinga.semana05_tecsupfit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lucasinga.semana05_tecsupfit.model.*
import com.lucasinga.semana05_tecsupfit.navigation.BarraInferior
import com.lucasinga.semana05_tecsupfit.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(navController: NavController, reservas: List<Reserva>) {
    // estadísticas calculadas con la lista compartida de reservas
    val clasesTomadas = clasesAnteriores + reservas.count { it.estado == "Completada" }
    val activas = reservas.count { it.estado == "Confirmada" }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mi perfil", fontWeight = FontWeight.Bold) }) },
        bottomBar = { BarraInferior(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(VerdeClaroFit, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("LI", fontWeight = FontWeight.Bold, fontSize = 26.sp, color = VerdeFit)
            }
            Spacer(Modifier.height(12.dp))
            Text(nombreUsuario, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(planUsuario, fontSize = 13.sp, color = TextoGris)
            Spacer(Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CajaDato("$clasesTomadas", "Clases", Modifier.weight(1f))
                CajaDato("$rachaDias", "Racha (días)", Modifier.weight(1f))
                CajaDato("$activas", "Reservas", Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun CajaDato(valor: String, etiqueta: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(GrisTarjeta, RoundedCornerShape(12.dp))
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(valor, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Text(etiqueta, fontSize = 12.sp, color = TextoGris)
    }
}