package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.ui.components.AppTopBar

/**
 * Perfil del dueño. Por ahora solo muestra sus datos.
 * TODO (Paso 13): contadores, modo oscuro, recordatorios, cambiar de perfil y cerrar sesion.
 */
@Composable
fun PerfilScreen(nombre: String, correo: String) {
    Scaffold(
        topBar = { AppTopBar("Perfil") },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            Text(nombre, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(4.dp))
            Text(correo, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}