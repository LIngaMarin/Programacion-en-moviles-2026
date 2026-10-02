package com.lucasinga.semana06_tecsupstore.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasinga.semana06_tecsupstore.components.BarraSuperior
import com.lucasinga.semana06_tecsupstore.model.correoUsuario
import com.lucasinga.semana06_tecsupstore.model.nombreUsuario
import com.lucasinga.semana06_tecsupstore.ui.theme.LilaClaro
import com.lucasinga.semana06_tecsupstore.ui.theme.MoradoStore
import com.lucasinga.semana06_tecsupstore.ui.theme.TextoGris

@Composable
fun PerfilScreen(onMenuClick: () -> Unit) {
    Scaffold(
        topBar = { BarraSuperior("Perfil", onMenuClick) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(LilaClaro, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("LI", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = MoradoStore)
            }
            Text(nombreUsuario, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(correoUsuario, color = TextoGris)
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Carrera", color = TextoGris)
                    Text("Diseño y Desarrollo de Software", fontWeight = FontWeight.Bold)
                    Text("Ciclo", color = TextoGris, modifier = Modifier.padding(top = 8.dp))
                    Text("4to ciclo", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}