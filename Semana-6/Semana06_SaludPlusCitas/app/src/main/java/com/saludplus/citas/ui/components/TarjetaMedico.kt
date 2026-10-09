package com.saludplus.citas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.ui.theme.AzulClaro
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.TextoGris
import com.saludplus.citas.ui.theme.VerdeClaro
import com.saludplus.citas.ui.theme.VerdeDisponible

// tarjeta del medico: en Medicos se puede tocar; en Fecha y hora y Confirmar solo se muestra
@Composable
fun TarjetaMedico(medico: Medico, conCmp: Boolean = false, onClick: (() -> Unit)? = null) {
    // iniciales en vez de foto: "Dra. Ana Torres" -> "AT"
    val iniciales = medico.nombre.split(" ").drop(1).take(2).joinToString("") { it.take(1) }
    val modificador = if (onClick != null) Modifier.clickable { onClick() } else Modifier

    Card(
        modifier = modificador.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(AzulClaro, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(iniciales, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = AzulPrimario)
            }
            Spacer(Modifier.width(14.dp))
            Column {
                Text(medico.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(medico.titulo, color = TextoGris, fontSize = 13.sp)
                if (conCmp) {
                    Text("CMP: ${medico.cmp}", color = TextoGris, fontSize = 13.sp)
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = null,
                            tint = Color(0xFFF5B301),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(" ${medico.calificacion} (${medico.resenas})", fontSize = 13.sp, color = TextoGris)
                    }
                    Spacer(Modifier.height(6.dp))
                    // etiqueta verde de disponibilidad
                    Text(
                        medico.disponibilidad,
                        color = VerdeDisponible,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .background(VerdeClaro, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }
    }
}