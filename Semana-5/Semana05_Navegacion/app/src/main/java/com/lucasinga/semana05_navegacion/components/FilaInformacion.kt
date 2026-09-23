package com.lucasinga.semana05_navegacion.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun FilaInformacion(
    icono: ImageVector,
    etiqueta: String,
    valor: String,
    conFondoIcono: Boolean = false
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (conFondoIcono) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFE7E0EC), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color(0xFF49454F)
                )
            }
        } else {
            Icon(
                imageVector = icono,
                contentDescription = null,
                modifier = Modifier.size(22.dp),
                tint = Color(0xFF6750A4)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = etiqueta,
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF79747E)
            )
            Text(
                text = valor,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D1B20)
            )
        }
    }
}
