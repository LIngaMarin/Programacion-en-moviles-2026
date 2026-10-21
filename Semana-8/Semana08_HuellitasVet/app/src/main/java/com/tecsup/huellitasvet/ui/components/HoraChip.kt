package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

/**
 * ENTREGADO. Selector de hora de la pantalla Pedir cita.
 * ocupado = true -> gris y tachado, y no se puede tocar.
 */
@Composable
fun HoraChip(
    hora: String,
    seleccionado: Boolean,
    ocupado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colores = MaterialTheme.colorScheme
    val fondo = when {
        ocupado -> colores.surfaceContainerHighest
        seleccionado -> colores.primary
        else -> colores.surface
    }
    val texto = when {
        ocupado -> colores.onSurfaceVariant.copy(alpha = 0.6f)
        seleccionado -> colores.onPrimary
        else -> colores.onSurface
    }
    Surface(
        onClick = onClick,
        enabled = !ocupado,
        shape = RoundedCornerShape(10.dp),
        color = fondo,
        contentColor = texto,
        border = if (seleccionado || ocupado) null else BorderStroke(1.dp, colores.outlineVariant),
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = hora,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
                textDecoration = if (ocupado) TextDecoration.LineThrough else TextDecoration.None
            )
        }
    }
}
