package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Vaccines
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** ENTREGADO. Convierte el texto del icono guardado en la tabla servicios en un icono de Material. */
fun iconoDeServicio(icono: String): ImageVector = when (icono) {
    "heart" -> Icons.Filled.Favorite
    "syringe" -> Icons.Filled.Vaccines
    "scissors" -> Icons.Filled.ContentCut
    else -> Icons.Filled.MedicalServices
}

/** ENTREGADO. Icono del servicio dentro de un cuadrado redondeado del color del rol. */
@Composable
fun IconoServicio(icono: String, modifier: Modifier = Modifier, tamano: Dp = 44.dp) {
    Box(
        modifier = modifier
            .size(tamano)
            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            iconoDeServicio(icono),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(tamano / 2)
        )
    }
}
