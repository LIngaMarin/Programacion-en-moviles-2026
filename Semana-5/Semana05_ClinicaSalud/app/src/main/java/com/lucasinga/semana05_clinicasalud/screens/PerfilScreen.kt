package com.lucasinga.semana05_clinicasalud.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasinga.semana05_clinicasalud.model.Cita
import com.lucasinga.semana05_clinicasalud.model.nombrePaciente
import com.lucasinga.semana05_clinicasalud.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(citas: List<Cita>, onMenuClick: () -> Unit) {
    // Switch: control on/off, guarda true o false
    var recordatorios by remember { mutableStateOf(true) }

    // estadísticas calculadas con la misma lista de citas
    val confirmadas = citas.count { it.estado == "Confirmada" }
    val completadas = citas.count { it.estado == "Completada" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi perfil", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menú")
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
                    .size(80.dp)
                    .background(LilaClaro, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("LI", fontWeight = FontWeight.Bold, fontSize = 26.sp, color = MoradoClinica)
            }
            Spacer(Modifier.height(12.dp))
            Text(nombrePaciente, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Paciente", fontSize = 13.sp, color = TextoGris)

            Spacer(Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CajaDato("$confirmadas", "Confirmadas", Modifier.weight(1f))
                CajaDato("$completadas", "Completadas", Modifier.weight(1f))
            }

            Spacer(Modifier.height(20.dp))

            FilaInfo("Correo", "lucas.inga@tecsup.edu.pe")
            FilaInfo("Teléfono", "+51 987 654 321")
            FilaInfo("Tipo de sangre", "O+")

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Recordatorios de citas", fontWeight = FontWeight.Bold)
                    Text(
                        if (recordatorios) "Activados" else "Desactivados",
                        fontSize = 13.sp,
                        color = TextoGris
                    )
                }
                Switch(
                    checked = recordatorios,
                    onCheckedChange = { recordatorios = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = MoradoClinica
                    )
                )
            }
        }
    }
}

@Composable
fun CajaDato(valor: String, etiqueta: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(GrisTarjeta, RoundedCornerShape(12.dp))
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(valor, fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Text(etiqueta, fontSize = 12.sp, color = TextoGris)
    }
}

@Composable
fun FilaInfo(etiqueta: String, valor: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(etiqueta, fontSize = 12.sp, color = TextoGris)
        Text(valor, fontWeight = FontWeight.Bold)
    }
}