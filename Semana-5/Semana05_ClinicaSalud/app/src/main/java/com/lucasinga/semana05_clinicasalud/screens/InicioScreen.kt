package com.lucasinga.semana05_clinicasalud.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lucasinga.semana05_clinicasalud.model.Medico
import com.lucasinga.semana05_clinicasalud.model.especialidades
import com.lucasinga.semana05_clinicasalud.model.medicos
import com.lucasinga.semana05_clinicasalud.navigation.Screen
import com.lucasinga.semana05_clinicasalud.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(navController: NavController, onMenuClick: () -> Unit) {
    // chip elegido; al cambiar, Compose vuelve a dibujar la lista ya filtrada
    var especialidadSeleccionada by remember { mutableStateOf("Todos") }

    val medicosFiltrados = if (especialidadSeleccionada == "Todos") {
        medicos
    } else {
        medicos.filter { it.especialidad == especialidadSeleccionada }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Clínica Salud+", fontWeight = FontWeight.Bold)
                        Text("Hola, Lucas", fontSize = 13.sp)
                    }
                },
                navigationIcon = {
                    // ícono ☰: abre el drawer (la función llega desde AppNavigation)
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menú")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MoradoClinica,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        // lista anidada: la LazyRow de chips es el primer item de la LazyColumn
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(especialidades) { especialidad ->
                        FilterChip(
                            selected = especialidad == especialidadSeleccionada,
                            onClick = { especialidadSeleccionada = especialidad },
                            label = { Text(especialidad) },
                            shape = RoundedCornerShape(50),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MoradoClinica,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
            item {
                Text("Médicos disponibles", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            items(medicosFiltrados) { medico ->
                // al tocar, se navega mandando solo el id del médico
                TarjetaMedico(medico) {
                    navController.navigate(Screen.PerfilMedico.createRoute(medico.id))
                }
            }
        }
    }
}

@Composable
fun TarjetaMedico(medico: Medico, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = GrisTarjeta)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(LilaClaro, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Add, contentDescription = null, tint = MoradoClinica)
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(medico.nombre, fontWeight = FontWeight.Bold)
                Text(medico.cargo, fontSize = 13.sp, color = TextoGris)
            }
            Icon(
                Icons.Filled.Star,
                contentDescription = null,
                tint = Dorado,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text("${medico.calificacion}", fontSize = 13.sp, color = TextoGris)
        }
    }
}