package com.lucasinga.semana05_tecsupfit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
fun ReservasScreen(navController: NavController, reservas: MutableList<Reserva>) {
    // Controla qué reserva se va a cancelar para mostrar el diálogo
    var reservaACancelar by remember { mutableStateOf<Reserva?>(null) }

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
                TarjetaReserva(
                    reserva = reserva,
                    onCancelarClick = { reservaACancelar = reserva },
                )
            }
        }

        // Diálogo de confirmación para cancelar reserva
        reservaACancelar?.let { reserva ->
            AlertDialog(
                onDismissRequest = { reservaACancelar = null },
                title = { Text("Cancelar reserva") },
                text = { Text("¿Seguro que deseas cancelar tu clase de ${reserva.clase} el ${reserva.dia}, ${reserva.hora}?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val index = reservas.indexOf(reserva)
                            if (index != -1) {
                                reservas[index] = reserva.copy(estado = "Cancelada")
                            }
                            reservaACancelar = null
                        }
                    ) {
                        Text("Sí, cancelar", color = Color(0xFFB3261E))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { reservaACancelar = null }) {
                        Text("No")
                    }
                }
            )
        }
    }
}

@Composable
fun TarjetaReserva(
    reserva: Reserva,
    onCancelarClick: () -> Unit = {},
) {
    val confirmada = reserva.estado == "Confirmada"
    val cancelada = reserva.estado == "Cancelada"

    // Colores según el estado de la reserva
    val tarjetaColor = when {
        confirmada -> VerdeClaroFit
        cancelada -> Color(0xFFF0F0F0)
        else -> GrisTarjeta
    }

    val etiquetaFondo = when {
        confirmada -> VerdeFit
        cancelada -> Color(0xFFFDE7E9)
        else -> GrisCompletada
    }

    val etiquetaTexto = when {
        confirmada -> Color.White
        cancelada -> Color(0xFFB3261E)
        else -> TextoGris
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = tarjetaColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(reserva.clase, fontWeight = FontWeight.Bold)
            Text("${reserva.dia}, ${reserva.hora} · ${reserva.sala}", fontSize = 13.sp, color = TextoGris)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = reserva.estado,
                    fontSize = 12.sp,
                    color = etiquetaTexto,
                    modifier = Modifier
                        .background(
                            color = etiquetaFondo,
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )
                if (confirmada) {
                    TextButton(onClick = onCancelarClick) {
                        Text("Cancelar reserva", color = Color(0xFFB3261E))
                    }
                }
            }
        }
    }
}
