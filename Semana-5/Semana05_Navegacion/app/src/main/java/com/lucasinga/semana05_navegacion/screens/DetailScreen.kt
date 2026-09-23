package com.lucasinga.semana05_navegacion.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lucasinga.semana05_navegacion.components.BarraSuperior
import com.lucasinga.semana05_navegacion.components.FilaInformacion
import com.lucasinga.semana05_navegacion.components.FotoPerfil
import com.lucasinga.semana05_navegacion.model.estudiantes

@Composable
fun DetailScreen(navController: NavController, itemId: Int) {
    val alumno = estudiantes.find { it.id == itemId } ?: estudiantes.first()

    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = "Expediente Académico",
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF6750A4),
                                    Color(0xFF625B71)
                                )
                            ),
                            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                        )
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 80.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    FotoPerfil(
                        url = alumno.fotoUrl,
                        tamano = 120.dp,
                        bordeAncho = 4.dp,
                        bordeColor = Color.White,
                        contentDescription = alumno.nombre
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = alumno.nombre,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D1B20),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                text = alumno.carrera,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF6750A4),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE7E0EC))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    FilaInformacion(
                        icono = Icons.Filled.Badge,
                        etiqueta = "ID Estudiante",
                        valor = alumno.codigo
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    FilaInformacion(
                        icono = Icons.Filled.Email,
                        etiqueta = "Correo Electrónico",
                        valor = alumno.correo
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    FilaInformacion(
                        icono = Icons.Filled.School,
                        etiqueta = "Facultad",
                        valor = alumno.facultad
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                    Text(
                        text = "Biografía",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = alumno.biografia,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF49454F)
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
