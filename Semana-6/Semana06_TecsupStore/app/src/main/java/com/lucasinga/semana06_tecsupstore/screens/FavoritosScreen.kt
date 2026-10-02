package com.lucasinga.semana06_tecsupstore.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lucasinga.semana06_tecsupstore.components.BarraSuperior
import com.lucasinga.semana06_tecsupstore.ui.theme.LilaClaro
import com.lucasinga.semana06_tecsupstore.ui.theme.TextoGris

@Composable
fun FavoritosScreen(onMenuClick: () -> Unit) {
    Scaffold(
        topBar = { BarraSuperior("Favoritos", onMenuClick) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = LilaClaro,
                    modifier = Modifier.size(64.dp)
                )
                Text("Aun no tienes favoritos", fontWeight = FontWeight.Bold)
                Text("Marcalos desde el menu de cada producto", color = TextoGris)
            }
        }
    }
}