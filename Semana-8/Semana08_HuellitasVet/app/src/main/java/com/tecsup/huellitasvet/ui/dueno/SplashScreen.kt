package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.ui.components.MascotaAvatar

/** ENTREGADO. Pantalla de inicio (Splash) con el logo y los botones de entrada. */
@Composable
fun SplashScreen(
    onComenzar: () -> Unit,
    onYaTengoCuenta: () -> Unit
) {
    val colores = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.primary)
            .systemBarsPadding()
            .padding(24.dp)
    ) {
        // logo, nombre y mascotas
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(colores.onPrimary.copy(alpha = 0.18f), RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Pets, contentDescription = "Logo", tint = colores.onPrimary, modifier = Modifier.size(56.dp))
            }
            Spacer(Modifier.height(18.dp))
            Text("HuellitasVet", style = MaterialTheme.typography.headlineMedium, color = colores.onPrimary)
            Text("Cuidamos a tu mejor amigo", style = MaterialTheme.typography.bodyMedium, color = colores.onPrimary.copy(alpha = 0.85f))
            Spacer(Modifier.height(32.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                listOf(Especie.PERRO, Especie.GATO, Especie.PERRO).forEach { especie ->
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(colores.surface, RoundedCornerShape(18.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        MascotaAvatar(especie = especie, tamano = 54.dp)
                    }
                }
            }
        }

        // botones de entrada
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onComenzar,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colores.surface, contentColor = colores.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Comenzar", fontWeight = FontWeight.SemiBold)
            }
            TextButton(onClick = onYaTengoCuenta) {
                Text("Ya tengo una cuenta", color = colores.onPrimary)
            }
        }
    }
}
