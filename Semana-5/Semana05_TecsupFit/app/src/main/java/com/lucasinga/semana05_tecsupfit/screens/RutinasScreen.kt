package com.lucasinga.semana05_tecsupfit.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lucasinga.semana05_tecsupfit.model.Rutina
import com.lucasinga.semana05_tecsupfit.model.rutinas
import com.lucasinga.semana05_tecsupfit.navigation.BarraInferior
import com.lucasinga.semana05_tecsupfit.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RutinasScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Rutinas", fontWeight = FontWeight.Bold) })
        },
        bottomBar = { BarraInferior(navController) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Marca las rutinas que hiciste hoy", fontSize = 13.sp, color = TextoGris)
            }
            items(rutinas) { rutina ->
                TarjetaRutina(rutina)
            }
        }
    }
}

@Composable
fun TarjetaRutina(rutina: Rutina) {
    // Checkbox: selección múltiple, cada rutina se marca por separado
    var hecha by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (hecha) VerdeClaroFit else GrisTarjeta
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(rutina.nombre, fontWeight = FontWeight.Bold)
                Text(
                    "${rutina.nivel} · ${rutina.ejercicios} ejercicios · ${rutina.duracion} min",
                    fontSize = 13.sp,
                    color = TextoGris
                )
            }
            Checkbox(
                checked = hecha,
                onCheckedChange = { hecha = it },
                colors = CheckboxDefaults.colors(checkedColor = VerdeFit)
            )
        }
    }
}