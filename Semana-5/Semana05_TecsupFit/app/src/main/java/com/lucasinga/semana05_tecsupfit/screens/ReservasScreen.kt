package com.lucasinga.semana05_tecsupfit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lucasinga.semana05_tecsupfit.model.Reserva
import com.lucasinga.semana05_tecsupfit.navigation.BarraInferior
import com.lucasinga.semana05_tecsupfit.navigation.Screen
import com.lucasinga.semana05_tecsupfit.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservasScreen(navController: NavController, reservas: List<Reserva>) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Mis reservas", fontWeight = FontWeight.Bold) })
        },
        bottomBar = { BarraInferior(navController) },
        // FAB: acceso rápido para reservar otra clase, lleva al Inicio
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.Inicio.route) {
                        popUpTo(Screen.Inicio.route) { inclusive = true }
                    }
                },
                containerColor = VerdeFit,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Reservar otra clase")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reservas) { reserva ->
                TarjetaReserva(reserva)
            }
        }
    }
}

@Composable
fun TarjetaReserva(reserva: Reserva) {
    // el estado cambia los colores para diferenciarlos
    val confirmada = reserva.estado == "Confirmada"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (confirmada) VerdeClaroFit else GrisTarjeta
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(reserva.clase, fontWeight = FontWeight.Bold)
            Text("${reserva.dia}, ${reserva.hora} · ${reserva.sala}", fontSize = 13.sp, color = TextoGris)
            Spacer(Modifier.height(8.dp))
            Text(
                text = reserva.estado,
                fontSize = 12.sp,
                color = if (confirmada) Color.White else TextoGris,
                modifier = Modifier
                    .background(
                        color = if (confirmada) VerdeFit else GrisCompletada,
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}