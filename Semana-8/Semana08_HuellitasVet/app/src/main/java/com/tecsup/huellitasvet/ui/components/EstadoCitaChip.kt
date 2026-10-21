package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.ui.theme.colorEstado

/** ENTREGADO. Etiqueta de color con el estado de la cita (Pendiente, Confirmada, Atendida, Cancelada). */
@Composable
fun EstadoCitaChip(estado: EstadoCita, modifier: Modifier = Modifier) {
    val color = colorEstado(estado)
    Text(
        text = estado.etiqueta,
        color = color,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = modifier
            .background(color.copy(alpha = 0.14f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 3.dp)
    )
}
