package com.lucasinga.semana05_navegacion.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun FotoPerfil(
    url: String,
    tamano: Dp,
    bordeAncho: Dp = 0.dp,
    bordeColor: Color = Color.Transparent,
    contentDescription: String? = null
) {
    val modifierConBorde = if (bordeAncho > 0.dp) {
        Modifier
            .size(tamano)
            .border(bordeAncho, bordeColor, CircleShape)
            .clip(CircleShape)
            .background(Color(0xFFE7E0EC))
    } else {
        Modifier
            .size(tamano)
            .clip(CircleShape)
            .background(Color(0xFFE7E0EC))
    }

    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        contentScale = ContentScale.Crop,
        modifier = modifierConBorde
    )
}
