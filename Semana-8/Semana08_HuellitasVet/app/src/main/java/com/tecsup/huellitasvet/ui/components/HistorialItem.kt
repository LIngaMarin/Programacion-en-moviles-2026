package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.ui.theme.EstadoCancelada

/**
 * ENTREGADO. Un punto de la linea de tiempo del historial clinico:
 * fecha, servicio, diagnostico, tratamiento y peso registrado en la atencion.
 * esUltimo = true -> no dibuja la linea hacia abajo.
 */
@Composable
fun HistorialItem(
    fecha: String,
    servicio: String,
    diagnostico: String?,
    tratamiento: String?,
    peso: Double?,
    esUltimo: Boolean = false
) {
    val colores = MaterialTheme.colorScheme
    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
        // linea de tiempo
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(28.dp).fillMaxHeight()
        ) {
            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = colores.primary, modifier = Modifier.size(20.dp))
            if (!esUltimo) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .weight(1f)
                        .background(colores.primaryContainer)
                )
            }
        }
        Spacer(Modifier.width(10.dp))
        // detalle de la atencion
        Column(modifier = Modifier.weight(1f).padding(bottom = 18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(servicio, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                Text(diaMes(fecha), style = MaterialTheme.typography.bodySmall, color = colores.primary, fontWeight = FontWeight.SemiBold)
            }
            if (!diagnostico.isNullOrBlank()) {
                Text(diagnostico, style = MaterialTheme.typography.bodyMedium, color = colores.onSurfaceVariant)
            }
            if (!tratamiento.isNullOrBlank()) {
                Text("Tratamiento: $tratamiento", style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
            }
            if (peso != null) {
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Favorite, contentDescription = null, tint = EstadoCancelada, modifier = Modifier.size(14.dp))
                    Text(" Peso ${kilos(peso)}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
