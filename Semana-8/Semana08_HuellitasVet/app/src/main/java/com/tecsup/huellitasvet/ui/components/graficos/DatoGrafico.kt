package com.tecsup.huellitasvet.ui.components.graficos

import androidx.compose.ui.graphics.Color

/** ENTREGADO (del laboratorio de graficos). Un dato para GraficoDona o GraficoBarras. */
data class DatoGrafico(
    val etiqueta: String,
    val valor: Float,
    val color: Color
)
