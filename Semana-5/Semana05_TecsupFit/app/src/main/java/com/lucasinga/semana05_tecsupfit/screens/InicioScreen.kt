package com.lucasinga.semana05_tecsupfit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lucasinga.semana05_tecsupfit.model.Clase
import com.lucasinga.semana05_tecsupfit.model.clases
import com.lucasinga.semana05_tecsupfit.model.filtros
import com.lucasinga.semana05_tecsupfit.navigation.BarraInferior
import com.lucasinga.semana05_tecsupfit.navigation.Screen
import com.lucasinga.semana05_tecsupfit.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(navController: NavController) {
    // chip elegido; al cambiar, Compose vuelve a dibujar la lista filtrada
    var filtroSeleccionado by remember { mutableStateOf("Hoy") }

    // "Esta semana" incluye las clases de hoy, por eso muestra todas
    val clasesFiltradas = if (filtroSeleccionado == "Hoy") {
        clases.filter { it.dia == "Hoy" }
    } else {
        clases
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("TECSUP Fit", fontWeight = FontWeight.Bold)
                        Text("Hola, Lucas", fontSize = 13.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdeFit,
                    titleContentColor = Color.White
                )
            )
        },
        // navegación secundaria: el bottomBar es parte del Scaffold
        bottomBar = { BarraInferior(navController) }
    ) { padding ->
        // lista anidada: la LazyRow de filtros es el primer item de la LazyColumn
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filtros) { filtro ->
                        FilterChip(
                            selected = filtro == filtroSeleccionado,
                            onClick = { filtroSeleccionado = filtro },
                            label = { Text(filtro) },
                            shape = RoundedCornerShape(50),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = VerdeFit,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
            item {
                Text("Clases disponibles", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            items(clasesFiltradas) { clase ->
                // al tocar, se navega mandando solo el id de la clase
                TarjetaClase(clase) {
                    navController.navigate(Screen.DetalleClase.createRoute(clase.id))
                }
            }
        }
    }
}

@Composable
fun TarjetaClase(clase: Clase, onClick: () -> Unit) {
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
                    .background(VerdeClaroFit, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Favorite, contentDescription = null, tint = VerdeFit)
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(clase.nombre, fontWeight = FontWeight.Bold)
                // se muestran todos los horarios de la clase separados por " / "
                Text(
                    "${clase.horarios.joinToString(" / ") { it.hora }} · ${clase.sala}",
                    fontSize = 13.sp,
                    color = TextoGris
                )
            }
            Text(clase.dia, fontSize = 12.sp, color = VerdeFit)
        }
    }
}