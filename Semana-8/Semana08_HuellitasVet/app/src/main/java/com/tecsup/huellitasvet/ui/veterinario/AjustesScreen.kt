package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.dueno.FilaPreferencia
import com.tecsup.huellitasvet.ui.dueno.NotaDataStore
import com.tecsup.huellitasvet.ui.dueno.TarjetaCambiarPerfil
import com.tecsup.huellitasvet.ui.dueno.TarjetaPreferencias
import com.tecsup.huellitasvet.ui.dueno.TarjetaUsuario

/**
 * Ajustes del veterinario: datos, cambiar de perfil, modo oscuro, notificaciones,
 * "Aceptar citas" (si se apaga, el dueño no puede pedir citas) y cerrar sesion.
 */
@Composable
fun AjustesScreen(
    nombre: String,
    correo: String,
    modoOscuro: Boolean,
    notificaciones: Boolean,
    aceptarCitas: Boolean,
    onModoOscuro: (Boolean) -> Unit,
    onNotificaciones: (Boolean) -> Unit,
    onAceptarCitas: (Boolean) -> Unit,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val colores = MaterialTheme.colorScheme
    Scaffold(
        topBar = { AppTopBar("Ajustes") },
        containerColor = colores.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            TarjetaUsuario(nombre, correo, etiquetaRol = "Perfil activo")
            Spacer(Modifier.height(14.dp))

            TarjetaCambiarPerfil(texto = "Modo práctica: ir a vista Dueño", onClick = onCambiarPerfil)
            Spacer(Modifier.height(18.dp))

            Text("Preferencias", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            TarjetaPreferencias {
                FilaPreferencia("Modo oscuro", "Cambia el tema de la app", modoOscuro, onModoOscuro)
                HorizontalDivider(color = colores.outlineVariant)
                FilaPreferencia("Notificaciones", "Avisar nuevas citas", notificaciones, onNotificaciones)
                HorizontalDivider(color = colores.outlineVariant)
                FilaPreferencia("Aceptar citas", "Abrir o cerrar la agenda", aceptarCitas, onAceptarCitas)
            }
            NotaDataStore()
            Spacer(Modifier.height(18.dp))

            VetButton(texto = "Cerrar sesión", onClick = onCerrarSesion, contorno = true, peligro = true)
            Spacer(Modifier.height(16.dp))
        }
    }
}