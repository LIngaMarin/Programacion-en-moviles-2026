package com.tecsup.huellitasvet.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * ENTREGADO (del laboratorio de graficos). Grafico de dona dibujado con Canvas,
 * con el total en el centro y la leyenda a la derecha.
 */
@Composable
fun GraficoDona(
    datos: List<DatoGrafico>,
    modifier: Modifier = Modifier,
    textoCentro: String = datos.sumOf { it.valor.toDouble() }.toInt().toString(),
    subtituloCentro: String = "citas",
    tamano: Dp = 120.dp
) {
    val total = datos.sumOf { it.valor.toDouble() }.toFloat()
    val vacio = MaterialTheme.colorScheme.surfaceContainerHighest
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(tamano), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val grosor = size.minDimension * 0.18f
                val lado = size.minDimension - grosor
                val esquina = Offset(grosor / 2f, grosor / 2f)
                val area = Size(lado, lado)
                if (total <= 0f) {
                    drawArc(vacio, 0f, 360f, useCenter = false, topLeft = esquina, size = area, style = Stroke(grosor))
                } else {
                    var inicio = -90f
                    datos.forEach { dato ->
                        val barrido = 360f * dato.valor / total
                        drawArc(dato.color, inicio, barrido, useCenter = false, topLeft = esquina, size = area, style = Stroke(grosor))
                        inicio += barrido
                    }
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(textoCentro, style = MaterialTheme.typography.titleLarge)
                Text(subtituloCentro, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(Modifier.width(16.dp))
        // leyenda
        Column(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.weight(1f)) {
            datos.forEach { dato ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(10.dp).background(dato.color, CircleShape))
                    Spacer(Modifier.width(8.dp))
                    Text(dato.etiqueta, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                    Text(dato.valor.toInt().toString(), style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
