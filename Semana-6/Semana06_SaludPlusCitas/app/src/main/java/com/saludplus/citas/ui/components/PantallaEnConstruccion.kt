package com.saludplus.citas.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.ui.theme.TextoGris

// contenido temporal: se borra de cada pantalla cuando esa pantalla queda terminada
@Composable
fun PantallaEnConstruccion(nombre: String, onContinuar: () -> Unit) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("🚧", fontSize = 48.sp)
            Text(nombre, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text("Pantalla en construccion", color = TextoGris)
            Spacer(Modifier.height(24.dp))
            Button(onClick = onContinuar) {
                Text("Continuar")
            }
        }
    }
}