package com.lucasinga.semana05_clinicasalud.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lucasinga.semana05_clinicasalud.model.Cita
import com.lucasinga.semana05_clinicasalud.navigation.Screen
import com.lucasinga.semana05_clinicasalud.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisCitasScreen(
    navController: NavController,
    citas: MutableList<Cita>,
    onMenuClick: () -> Unit
) {
    // estado para guardar la cita que se desea cancelar
    var citaACancelar by remember { mutableStateOf<Cita?>(null) }

    // diálogo de confirmación para cancelar cita
    if (citaACancelar != null) {
        val cita = citaACancelar!!
        AlertDialog(
            onDismissRequest = { citaACancelar = null },
            title = { Text("Cancelar cita") },
            text = {
                Text("¿Seguro que deseas cancelar tu cita con ${cita.medico} el ${cita.fecha}, ${cita.hora}?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val index = citas.indexOf(cita)
                        if (index != -1) {
                            // reemplaza la cita por una copia con estado Cancelada
                            citas[index] = cita.copy(estado = "Cancelada")
                        }
                        citaACancelar = null
                    }
                ) {
                    Text("Sí, cancelar", color = Color(0xFFB3261E))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { citaACancelar = null }
                ) {
                    Text("No")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis citas", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menú")
                    }
                }
            )
        },
        // FAB: acceso rápido para agendar otra cita, lleva al Inicio
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.Inicio.route) {
                        popUpTo(Screen.Inicio.route) { inclusive = true }
                    }
                },
                containerColor = MoradoClinica,
                contentColor = Color.White
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agendar nueva cita")
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
            items(citas) { cita ->
                TarjetaCita(
                    cita = cita,
                    onCancelarClick = { citaACancelar = cita }
                )
            }
        }
    }
}

@Composable
fun TarjetaCita(
    cita: Cita,
    onCancelarClick: () -> Unit = {}
) {
    // color de fondo de la tarjeta según el estado
    val colorTarjeta = when (cita.estado) {
        "Confirmada" -> LilaClaro
        "Cancelada" -> Color(0xFFF3EFF7)
        else -> GrisTarjeta
    }

    // colores de la etiqueta según el estado
    val (colorTextoEtiqueta, colorFondoEtiqueta) = when (cita.estado) {
        "Confirmada" -> Pair(VerdeConfirmada, VerdeClaro)
        "Cancelada" -> Pair(Color(0xFFB3261E), Color(0xFFFDE7E9))
        else -> Pair(TextoGris, GrisCompletada)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorTarjeta
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(cita.medico, fontWeight = FontWeight.Bold)
            Text(cita.especialidad, fontSize = 13.sp, color = TextoGris)
            Text("${cita.fecha}, ${cita.hora}", fontSize = 13.sp, color = TextoGris)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = cita.estado,
                    fontSize = 12.sp,
                    color = colorTextoEtiqueta,
                    modifier = Modifier
                        .background(
                            color = colorFondoEtiqueta,
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )
                // botón para cancelar solo visible en citas confirmadas
                if (cita.estado == "Confirmada") {
                    TextButton(onClick = onCancelarClick) {
                        Text("Cancelar cita", color = Color(0xFFB3261E))
                    }
                }
            }
        }
    }
}
