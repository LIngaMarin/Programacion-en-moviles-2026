package com.tecsup.huellitasvet.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Especie

/**
 * ENTREGADO. Carita de la mascota dibujada con Canvas (sin imagenes).
 * Dibuja un perro, un gato u otro animal (conejo) segun la especie.
 */
@Composable
fun MascotaAvatar(
    especie: Especie,
    modifier: Modifier = Modifier,
    tamano: Dp = 56.dp,
    conFondo: Boolean = true
) {
    Canvas(modifier = modifier.size(tamano)) {
        val s = size.minDimension
        if (conFondo) {
            val fondo = when (especie) {
                Especie.PERRO -> Color(0xFFFDEBD3)
                Especie.GATO -> Color(0xFFE2E8F0)
                Especie.OTRO -> Color(0xFFFCE7F3)
            }
            drawCircle(color = fondo, radius = s / 2f, center = Offset(s / 2f, s / 2f))
        }
        when (especie) {
            Especie.PERRO -> dibujarPerro(s)
            Especie.GATO -> dibujarGato(s)
            Especie.OTRO -> dibujarConejo(s)
        }
    }
}

// Ayudas para trabajar con proporciones del tamaño (0f..1f)
private fun p(s: Float, x: Float, y: Float) = Offset(s * x, s * y)
private fun t(s: Float, ancho: Float, alto: Float) = Size(s * ancho, s * alto)

private val Ojo = Color(0xFF1F2937)

private fun DrawScope.dibujarPerro(s: Float) {
    val pelo = Color(0xFFE9A55B)
    val oreja = Color(0xFFA8642A)
    val hocico = Color(0xFFFCE3C4)
    // orejas caidas
    drawOval(oreja, topLeft = p(s, 0.15f, 0.26f), size = t(s, 0.20f, 0.38f))
    drawOval(oreja, topLeft = p(s, 0.65f, 0.26f), size = t(s, 0.20f, 0.38f))
    // cabeza
    drawCircle(pelo, radius = s * 0.28f, center = p(s, 0.5f, 0.52f))
    // hocico
    drawOval(hocico, topLeft = p(s, 0.34f, 0.55f), size = t(s, 0.32f, 0.22f))
    // ojos
    drawCircle(Ojo, radius = s * 0.035f, center = p(s, 0.40f, 0.46f))
    drawCircle(Ojo, radius = s * 0.035f, center = p(s, 0.60f, 0.46f))
    drawCircle(Color.White, radius = s * 0.012f, center = p(s, 0.41f, 0.45f))
    drawCircle(Color.White, radius = s * 0.012f, center = p(s, 0.61f, 0.45f))
    // nariz y boca
    drawOval(Color(0xFF3B2A20), topLeft = p(s, 0.445f, 0.56f), size = t(s, 0.11f, 0.075f))
    drawLine(Color(0xFF3B2A20), p(s, 0.5f, 0.63f), p(s, 0.5f, 0.68f), strokeWidth = s * 0.02f, cap = StrokeCap.Round)
    drawLine(Color(0xFF3B2A20), p(s, 0.5f, 0.68f), p(s, 0.44f, 0.71f), strokeWidth = s * 0.02f, cap = StrokeCap.Round)
    drawLine(Color(0xFF3B2A20), p(s, 0.5f, 0.68f), p(s, 0.56f, 0.71f), strokeWidth = s * 0.02f, cap = StrokeCap.Round)
}

private fun DrawScope.dibujarGato(s: Float) {
    val pelo = Color(0xFF94A3B8)
    val rosa = Color(0xFFF9A8D4)
    // orejas puntiagudas (triangulos)
    val orejaIzq = Path().apply {
        moveTo(s * 0.24f, s * 0.44f); lineTo(s * 0.27f, s * 0.15f); lineTo(s * 0.47f, s * 0.30f); close()
    }
    val orejaDer = Path().apply {
        moveTo(s * 0.76f, s * 0.44f); lineTo(s * 0.73f, s * 0.15f); lineTo(s * 0.53f, s * 0.30f); close()
    }
    drawPath(orejaIzq, pelo)
    drawPath(orejaDer, pelo)
    val internaIzq = Path().apply {
        moveTo(s * 0.29f, s * 0.36f); lineTo(s * 0.30f, s * 0.22f); lineTo(s * 0.41f, s * 0.31f); close()
    }
    val internaDer = Path().apply {
        moveTo(s * 0.71f, s * 0.36f); lineTo(s * 0.70f, s * 0.22f); lineTo(s * 0.59f, s * 0.31f); close()
    }
    drawPath(internaIzq, rosa)
    drawPath(internaDer, rosa)
    // cabeza
    drawCircle(pelo, radius = s * 0.28f, center = p(s, 0.5f, 0.55f))
    // ojos (rasgados)
    drawOval(Color(0xFF84CC16), topLeft = p(s, 0.35f, 0.47f), size = t(s, 0.09f, 0.08f))
    drawOval(Color(0xFF84CC16), topLeft = p(s, 0.56f, 0.47f), size = t(s, 0.09f, 0.08f))
    drawOval(Ojo, topLeft = p(s, 0.38f, 0.47f), size = t(s, 0.03f, 0.08f))
    drawOval(Ojo, topLeft = p(s, 0.59f, 0.47f), size = t(s, 0.03f, 0.08f))
    // nariz
    val nariz = Path().apply {
        moveTo(s * 0.46f, s * 0.60f); lineTo(s * 0.54f, s * 0.60f); lineTo(s * 0.50f, s * 0.645f); close()
    }
    drawPath(nariz, rosa)
    // bigotes
    val bigote = Color(0xFF475569)
    val grosor = s * 0.012f
    drawLine(bigote, p(s, 0.40f, 0.64f), p(s, 0.20f, 0.60f), strokeWidth = grosor, cap = StrokeCap.Round)
    drawLine(bigote, p(s, 0.40f, 0.67f), p(s, 0.20f, 0.69f), strokeWidth = grosor, cap = StrokeCap.Round)
    drawLine(bigote, p(s, 0.60f, 0.64f), p(s, 0.80f, 0.60f), strokeWidth = grosor, cap = StrokeCap.Round)
    drawLine(bigote, p(s, 0.60f, 0.67f), p(s, 0.80f, 0.69f), strokeWidth = grosor, cap = StrokeCap.Round)
}

private fun DrawScope.dibujarConejo(s: Float) {
    val pelo = Color(0xFFD6BCA8)
    val rosa = Color(0xFFF9A8D4)
    // orejas largas
    drawOval(pelo, topLeft = p(s, 0.31f, 0.08f), size = t(s, 0.14f, 0.40f))
    drawOval(pelo, topLeft = p(s, 0.55f, 0.08f), size = t(s, 0.14f, 0.40f))
    drawOval(rosa, topLeft = p(s, 0.345f, 0.13f), size = t(s, 0.07f, 0.28f))
    drawOval(rosa, topLeft = p(s, 0.585f, 0.13f), size = t(s, 0.07f, 0.28f))
    // cabeza
    drawCircle(pelo, radius = s * 0.26f, center = p(s, 0.5f, 0.60f))
    // ojos
    drawCircle(Ojo, radius = s * 0.032f, center = p(s, 0.41f, 0.56f))
    drawCircle(Ojo, radius = s * 0.032f, center = p(s, 0.59f, 0.56f))
    // nariz y dientes
    drawCircle(rosa, radius = s * 0.028f, center = p(s, 0.5f, 0.65f))
    drawRect(Color.White, topLeft = p(s, 0.475f, 0.68f), size = t(s, 0.05f, 0.06f))
}
