package com.lucasinga.semana05_clinicasalud.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lucasinga.semana05_clinicasalud.model.medicos
import com.lucasinga.semana05_clinicasalud.navigation.Screen
import com.lucasinga.semana05_clinicasalud.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilMedicoScreen(navController: NavController, medicoId: Int) {
    // con el id que llegó por la ruta se busca el médico en la lista
    val medico = medicos.first { it.id == medicoId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil del médico", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
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
                    .size(90.dp)
                    .background(LilaClaro, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = null,
                    tint = MoradoClinica,
                    modifier = Modifier.size(56.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(medico.nombre, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(
                "${medico.cargo} · ${medico.experiencia} años exp.",
                fontSize = 13.sp,
                color = TextoGris
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = null,
                    tint = Dorado,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    "${medico.calificacion} (${medico.reseñas} reseñas)",
                    fontSize = 13.sp,
                    color = TextoGris
                )
            }
            Spacer(Modifier.height(24.dp))
            Text(medico.descripcion, fontSize = 14.sp, modifier = Modifier.fillMaxWidth())

            // empuja el botón hasta abajo de la pantalla
            Spacer(Modifier.weight(1f))

            Button(
                onClick = { navController.navigate(Screen.Agendar.createRoute(medico.id)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MoradoClinica)
            ) {
                Text("Agendar cita", fontWeight = FontWeight.Bold)
            }
        }
    }
}