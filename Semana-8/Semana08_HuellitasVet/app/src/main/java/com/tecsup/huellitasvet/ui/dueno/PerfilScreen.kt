package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.theme.AzulPrimario
import com.tecsup.huellitasvet.ui.theme.EstadoPendiente

/**
 * Perfil del dueño: datos, contadores (mascotas, citas proximas y atenciones),
 * preferencias guardadas en DataStore, cambiar de perfil y cerrar sesion.
 */
@Composable
fun PerfilScreen(
    nombre: String,
    correo: String,
    mascotas: Int,
    proximas: Int,
    atenciones: Int,
    modoOscuro: Boolean,
    recordatorios: Boolean,
    onModoOscuro: (Boolean) -> Unit,
    onRecordatorios: (Boolean) -> Unit,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val colores = MaterialTheme.colorScheme
    Scaffold(
        topBar = { AppTopBar("Perfil") },
        containerColor = colores.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            TarjetaUsuario(nombre, correo, etiquetaRol = "Dueño")
            Spacer(Modifier.height(14.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ContadorPerfil(mascotas, "Mascotas", colores.primary, Modifier.weight(1f))
                ContadorPerfil(proximas, "Próximas", EstadoPendiente, Modifier.weight(1f))
                ContadorPerfil(atenciones, "Atenciones", AzulPrimario, Modifier.weight(1f))
            }
            Spacer(Modifier.height(14.dp))

            TarjetaCambiarPerfil(texto = "Modo práctica: ir a vista Veterinario", onClick = onCambiarPerfil)
            Spacer(Modifier.height(18.dp))

            Text("Preferencias", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            TarjetaPreferencias {
                FilaPreferencia("Modo oscuro", "Cambia el tema de la app", modoOscuro, onModoOscuro)
                HorizontalDivider(color = colores.outlineVariant)
                FilaPreferencia("Recordatorios", "Avisos de citas y vacunas", recordatorios, onRecordatorios)
            }
            NotaDataStore()
            Spacer(Modifier.height(18.dp))

            VetButton(texto = "Cerrar sesión", onClick = onCerrarSesion, contorno = true, peligro = true)
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ContadorPerfil(valor: Int, texto: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(14.dp))
            .padding(vertical = 14.dp)
    ) {
        Text(valor.toString(), color = color, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(texto, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

// ---------- Piezas compartidas con AjustesScreen (veterinario) ----------

/** Tarjeta con el icono, nombre, correo y rol del usuario. */
@Composable
fun TarjetaUsuario(nombre: String, correo: String, etiquetaRol: String) {
    val colores = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colores.surface),
        border = BorderStroke(1.dp, colores.outlineVariant)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(colores.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Person, contentDescription = null, tint = colores.primary, modifier = Modifier.size(32.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column {
                Text(nombre, style = MaterialTheme.typography.titleMedium)
                Text(correo, style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
                Spacer(Modifier.height(4.dp))
                Text(
                    etiquetaRol,
                    color = colores.primary,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .background(colores.primaryContainer, RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 2.dp)
                )
            }
        }
    }
}

/** Tarjeta de color del rol: "Cambiar de perfil". */
@Composable
fun TarjetaCambiarPerfil(texto: String, onClick: () -> Unit) {
    val colores = MaterialTheme.colorScheme
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(colores.primary, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Icon(Icons.Filled.SwapHoriz, contentDescription = null, tint = colores.onPrimary)
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text("Cambiar de perfil", color = colores.onPrimary, fontWeight = FontWeight.SemiBold)
            Text(texto, color = colores.onPrimary.copy(alpha = 0.85f), style = MaterialTheme.typography.bodySmall)
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = colores.onPrimary)
    }
}

/** Tarjeta blanca que agrupa las filas de preferencias. */
@Composable
fun TarjetaPreferencias(contenido: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) { contenido() }
    }
}

/** Fila con titulo, subtitulo y un switch. */
@Composable
fun FilaPreferencia(titulo: String, subtitulo: String, activo: Boolean, onCambiar: (Boolean) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(titulo, fontWeight = FontWeight.SemiBold)
            Text(subtitulo, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = activo, onCheckedChange = onCambiar)
    }
}

/** Nota del mockup: las preferencias se guardan con DataStore. */
@Composable
fun NotaDataStore() {
    Text(
        "Se guardan en el dispositivo con DataStore",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 8.dp, start = 4.dp)
    )
}