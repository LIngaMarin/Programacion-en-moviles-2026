package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.EstadoCita

/**
 * ENTREGADO. Tarjeta de una cita.
 * icono: lo que va a la izquierda (IconoServicio o MascotaAvatar).
 * accion: lo que va a la derecha (boton Cancelar, Confirmar, Atender...), opcional.
 */
@Composable
fun CitaCard(
    titulo: String,
    subtitulo: String,
    estado: EstadoCita,
    icono: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    detalle: String? = null,
    accion: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    val mod = if (onClick != null) modifier.clickable { onClick() } else modifier
    Card(
        modifier = mod.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icono()
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(titulo, style = MaterialTheme.typography.titleMedium)
                Text(subtitulo, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (detalle != null) {
                    Text(detalle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Spacer(Modifier.height(6.dp))
                EstadoCitaChip(estado)
            }
            if (accion != null) {
                Spacer(Modifier.width(8.dp))
                accion()
            }
        }
    }
}

/** Texto de accion en la tarjeta (ej. "Cancelar" en rojo). */
@Composable
fun AccionTexto(texto: String, onClick: () -> Unit, peligro: Boolean = false) {
    Text(
        text = texto,
        color = if (peligro) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.SemiBold,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier
            .clickable { onClick() }
            .padding(6.dp)
    )
}
