package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * ENTREGADO. Boton principal de la app, del color del rol (MaterialTheme.colorScheme.primary).
 * contorno = true -> boton con borde (ej. "Cancelar"); peligro = true -> en rojo (ej. "Cerrar sesion").
 */
@Composable
fun VetButton(
    texto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true,
    contorno: Boolean = false,
    peligro: Boolean = false,
    icono: ImageVector? = null
) {
    val forma = RoundedCornerShape(12.dp)
    val color = if (peligro) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
    val contenido: @Composable RowScope.() -> Unit = {
        if (icono != null) {
            Icon(icono, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
        }
        Text(texto, fontWeight = FontWeight.SemiBold)
    }
    val mod = modifier
        .fillMaxWidth()
        .height(50.dp)

    if (contorno) {
        OutlinedButton(
            onClick = onClick,
            modifier = mod,
            enabled = habilitado,
            shape = forma,
            border = BorderStroke(1.dp, color),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = color),
            content = contenido
        )
    } else {
        Button(
            onClick = onClick,
            modifier = mod,
            enabled = habilitado,
            shape = forma,
            colors = ButtonDefaults.buttonColors(containerColor = color),
            content = contenido
        )
    }
}
