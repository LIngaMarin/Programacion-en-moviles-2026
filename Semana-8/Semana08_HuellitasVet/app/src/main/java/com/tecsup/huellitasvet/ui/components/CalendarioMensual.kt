package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.YearMonth

/**
 * Calendario mensual para la agenda del veterinario.
 * Cabecera con flechas y mes, iniciales de L a D, grilla con Column/Row,
 * día seleccionado en círculo primario, hoy con borde, y puntos en días con citas.
 */
@Composable
fun CalendarioMensual(
    mesVisible: YearMonth,
    fechaSeleccionada: LocalDate,
    diasConCitas: Set<LocalDate>,
    onMesAnterior: () -> Unit,
    onMesSiguiente: () -> Unit,
    onSeleccionarFecha: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val colores = MaterialTheme.colorScheme

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = colores.surface,
        border = BorderStroke(1.dp, colores.outlineVariant),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Cabecera con flechas y nombre del mes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onMesAnterior) {
                    Icon(Icons.Filled.ChevronLeft, contentDescription = "Mes anterior", tint = colores.primary)
                }
                Text(
                    text = nombreMesAnio(mesVisible),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colores.onSurface
                )
                IconButton(onClick = onMesSiguiente) {
                    Icon(Icons.Filled.ChevronRight, contentDescription = "Mes siguiente", tint = colores.primary)
                }
            }

            Spacer(Modifier.height(8.dp))

            // Fila de iniciales de los días (L M M J V S D)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                val iniciales = listOf("L", "M", "M", "J", "V", "S", "D")
                for (inicia in iniciales) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = inicia,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = colores.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(Modifier.height(6.dp))

            // Grilla de días con Column y Row (sin LazyVerticalGrid)
            val primerDia = mesVisible.atDay(1)
            val diasEnMes = mesVisible.lengthOfMonth()
            // Lunes = 1, Domingo = 7
            val diaSemanaInicio = primerDia.dayOfWeek.value
            val desplazamiento = diaSemanaInicio - 1 // 0 para Lunes, 6 para Domingo

            val totalCeldas = desplazamiento + diasEnMes
            val totalFilas = (totalCeldas + 6) / 7

            val hoy = LocalDate.now()

            for (fila in 0 until totalFilas) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    for (col in 0 until 7) {
                        val index = fila * 7 + col
                        val diaNumero = index - desplazamiento + 1

                        if (diaNumero in 1..diasEnMes) {
                            val fecha = mesVisible.atDay(diaNumero)
                            val seleccionado = fecha == fechaSeleccionada
                            val esHoy = fecha == hoy
                            val tieneCita = diasConCitas.contains(fecha)

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(2.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            seleccionado -> colores.primary
                                            else -> colores.surface
                                        }
                                    )
                                    .then(
                                        if (esHoy && !seleccionado) {
                                            Modifier.border(1.5.dp, colores.primary, CircleShape)
                                        } else {
                                            Modifier
                                        }
                                    )
                                    .clickable { onSeleccionarFecha(fecha) },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = diaNumero.toString(),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (seleccionado || esHoy) FontWeight.Bold else FontWeight.Normal,
                                        color = when {
                                            seleccionado -> colores.onPrimary
                                            esHoy -> colores.primary
                                            else -> colores.onSurface
                                        }
                                    )
                                    // Punto pequeño debajo si tiene citas
                                    if (tieneCita) {
                                        Spacer(Modifier.height(2.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(4.dp)
                                                .clip(CircleShape)
                                                .background(if (seleccionado) colores.onPrimary else colores.primary)
                                        )
                                    } else {
                                        Spacer(Modifier.height(6.dp))
                                    }
                                }
                            }
                        } else {
                            // Espacio vacío para rellenar la grilla
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
