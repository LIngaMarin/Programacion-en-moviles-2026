package com.lucasinga.semana05_clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasinga.semana05_clinicasalud.model.historial
import com.lucasinga.semana05_clinicasalud.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen(onMenuClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial médico", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menú")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(historial) { registro ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = GrisTarjeta)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(registro.fecha, fontSize = 12.sp, color = TextoGris)
                        Text(registro.motivo, fontWeight = FontWeight.Bold)
                        Text(registro.medico, fontSize = 13.sp, color = MoradoClinica)
                    }
                }
            }
        }
    }
}