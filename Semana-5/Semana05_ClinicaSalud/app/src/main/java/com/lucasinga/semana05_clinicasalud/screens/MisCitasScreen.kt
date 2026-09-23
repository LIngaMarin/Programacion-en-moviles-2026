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
import androidx.compose.runtime.Composable
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
fun MisCitasScreen(navController: NavController, citas: List<Cita>, onMenuClick: () -> Unit) {
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
                TarjetaCita(cita)
            }
        }
    }
}

@Composable
fun TarjetaCita(cita: Cita) {
    // el estado cambia los colores de la etiqueta para diferenciarlos
    val confirmada = cita.estado == "Confirmada"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (confirmada) LilaClaro else GrisTarjeta
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(cita.medico, fontWeight = FontWeight.Bold)
            Text(cita.especialidad, fontSize = 13.sp, color = TextoGris)
            Text("${cita.fecha}, ${cita.hora}", fontSize = 13.sp, color = TextoGris)
            Spacer(Modifier.height(8.dp))
            Text(
                text = cita.estado,
                fontSize = 12.sp,
                color = if (confirmada) VerdeConfirmada else TextoGris,
                modifier = Modifier
                    .background(
                        color = if (confirmada) VerdeClaro else GrisCompletada,
                        shape = RoundedCornerShape(50)
                    )
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}