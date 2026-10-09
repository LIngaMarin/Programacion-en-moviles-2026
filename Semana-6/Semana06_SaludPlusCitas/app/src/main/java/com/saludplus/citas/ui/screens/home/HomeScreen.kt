package com.saludplus.citas.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.theme.AzulClaro
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.FondoApp
import com.saludplus.citas.ui.theme.Morado
import com.saludplus.citas.ui.theme.MoradoClaro
import com.saludplus.citas.ui.theme.Naranja
import com.saludplus.citas.ui.theme.NaranjaClaro
import com.saludplus.citas.ui.theme.TextoGris
import com.saludplus.citas.ui.theme.VerdeClaro
import com.saludplus.citas.ui.theme.VerdeDisponible

@Composable
fun HomeScreen(navController: NavController) {
    // solo el primer nombre: "Lucas Inga Marin" -> "Lucas"
    val nombre = Repositorio.usuarioActual?.nombres?.substringBefore(" ") ?: ""

    Scaffold(containerColor = FondoApp) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // saludo + campana
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("¡Hola, $nombre!", fontSize = 26.sp, fontWeight = FontWeight.Bold)
                        Text("¿Que deseas hacer hoy?", color = TextoGris)
                    }
                    IconButton(onClick = { navController.navigate(Rutas.NOTIFICACIONES) }) {
                        Icon(Icons.Filled.Notifications, contentDescription = "Notificaciones", tint = AzulPrimario)
                    }
                }
            }

            // las 4 tarjetas de accion, en 2 filas de 2
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TarjetaAccion("Agendar cita", Icons.Filled.DateRange, AzulPrimario, AzulClaro, Modifier.weight(1f)) {
                        navController.navigate(Rutas.ESPECIALIDADES)
                    }
                    TarjetaAccion("Mis citas", Icons.AutoMirrored.Filled.List, VerdeDisponible, VerdeClaro, Modifier.weight(1f)) {
                        navController.navigate(Rutas.MIS_CITAS)
                    }
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    TarjetaAccion("Mis datos", Icons.Filled.Person, Morado, MoradoClaro, Modifier.weight(1f)) {
                        navController.navigate(Rutas.PERFIL)
                    }
                    TarjetaAccion("Resultados", Icons.Filled.Info, Naranja, NaranjaClaro, Modifier.weight(1f)) {
                        navController.navigate(Rutas.RESULTADOS)
                    }
                }
            }

            // titulo de la seccion + "Ver todas"
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "Especialidades destacadas",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = { navController.navigate(Rutas.ESPECIALIDADES) }) {
                        Text("Ver todas", color = AzulPrimario)
                    }
                }
            }

            // LazyRow con las 3 especialidades destacadas (take del repositorio)
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(Repositorio.especialidadesDestacadas()) { especialidad ->
                        Card(
                            modifier = Modifier
                                .width(110.dp)
                                .clickable { navController.navigate(Rutas.medicos(especialidad.id)) },
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(AzulClaro, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(especialidad.icono, fontSize = 22.sp)
                                }
                                Spacer(Modifier.height(8.dp))
                                Text(especialidad.nombre, fontSize = 13.sp, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
            }
        }
    }
}

// tarjeta de color del inicio; recibe sus colores para reutilizarla 4 veces
@Composable
fun TarjetaAccion(
    titulo: String,
    icono: ImageVector,
    color: Color,
    fondo: Color,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = fondo)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icono, contentDescription = null, tint = color, modifier = Modifier.size(36.dp))
            Spacer(Modifier.height(8.dp))
            Text(titulo, color = color, fontWeight = FontWeight.SemiBold)
        }
    }
}