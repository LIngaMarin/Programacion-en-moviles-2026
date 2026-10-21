package com.tecsup.huellitasvet.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.model.PuntoPeso
import com.tecsup.huellitasvet.ui.components.diaMes
import com.tecsup.huellitasvet.ui.components.kilos

/**
 * Gráfico de líneas dibujado con Canvas para mostrar la evolución del peso de una mascota.
 * Muestra la línea que une los pesos, un círculo en cada punto, el peso encima y la fecha corta debajo.
 * Utiliza los colores del tema actual (MaterialTheme.colorScheme).
 */
@Composable
fun GraficoLineas(
    puntos: List<PuntoPeso>,
    modifier: Modifier = Modifier,
    alto: Dp = 150.dp
) {
    if (puntos.isEmpty()) return

    val textMeasurer = rememberTextMeasurer()
    val primaryColor = MaterialTheme.colorScheme.primary
    val textColor = MaterialTheme.colorScheme.onSurface
    val dateTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    val surfaceColor = MaterialTheme.colorScheme.surface

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(alto)
    ) {
        val width = size.width
        val height = size.height

        val horizontalPadding = 36.dp.toPx()
        val topPadding = 28.dp.toPx()
        val bottomPadding = 28.dp.toPx()

        val usableWidth = width - (horizontalPadding * 2)
        val usableHeight = height - topPadding - bottomPadding

        val minPeso = puntos.minOf { it.peso }
        val maxPeso = puntos.maxOf { it.peso }
        val rangoPeso = maxPeso - minPeso

        val count = puntos.size

        val xList = FloatArray(count)
        val yList = FloatArray(count)

        for (i in 0 until count) {
            val x = if (count == 1) {
                horizontalPadding + usableWidth / 2f
            } else {
                horizontalPadding + (i.toFloat() * usableWidth / (count - 1))
            }

            val y = if (rangoPeso == 0.0) {
                topPadding + usableHeight / 2f
            } else {
                val fraccion = ((puntos[i].peso - minPeso) / rangoPeso).toFloat()
                topPadding + usableHeight * (1f - fraccion)
            }

            xList[i] = x
            yList[i] = y
        }

        // 1. Línea que une los puntos
        if (count > 1) {
            val path = Path().apply {
                moveTo(xList[0], yList[0])
                for (i in 1 until count) {
                    lineTo(xList[i], yList[i])
                }
            }
            drawPath(
                path = path,
                color = primaryColor,
                style = Stroke(
                    width = 3.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }

        // 2. Círculo en cada punto, peso encima y fecha debajo
        for (i in 0 until count) {
            val center = Offset(xList[i], yList[i])

            drawCircle(
                color = surfaceColor,
                radius = 7.dp.toPx(),
                center = center
            )
            drawCircle(
                color = primaryColor,
                radius = 5.dp.toPx(),
                center = center
            )

            val pesoTexto = kilos(puntos[i].peso)
            val pesoResult = textMeasurer.measure(
                text = pesoTexto,
                style = TextStyle(
                    color = textColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            )
            val pesoX = xList[i] - pesoResult.size.width / 2f
            val pesoY = yList[i] - 8.dp.toPx() - pesoResult.size.height
            drawText(pesoResult, topLeft = Offset(pesoX, pesoY))

            val fechaTexto = try {
                diaMes(puntos[i].fecha)
            } catch (_: Exception) {
                puntos[i].fecha
            }
            val fechaResult = textMeasurer.measure(
                text = fechaTexto,
                style = TextStyle(
                    color = dateTextColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            )
            val fechaX = xList[i] - fechaResult.size.width / 2f
            val fechaY = yList[i] + 8.dp.toPx()
            drawText(fechaResult, topLeft = Offset(fechaX, fechaY))
        }
    }
}
