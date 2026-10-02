package com.lucasinga.semana06_tecsupstore.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lucasinga.semana06_tecsupstore.components.BarraSuperior
import com.lucasinga.semana06_tecsupstore.model.productos
import com.lucasinga.semana06_tecsupstore.ui.theme.LilaClaro
import com.lucasinga.semana06_tecsupstore.ui.theme.MoradoStore
import com.lucasinga.semana06_tecsupstore.ui.theme.TextoGris

@Composable
fun FavoritosScreen(onMenuClick: () -> Unit, favoritos: List<Int>) {
    // de todos los productos, solo los que tienen su id en la lista de favoritos
    val productosFavoritos = productos.filter { it.id in favoritos }

    Scaffold(
        topBar = { BarraSuperior("Favoritos", onMenuClick) }
    ) { padding ->
        if (productosFavoritos.isEmpty()) {
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
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(productosFavoritos) { producto ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(producto.nombre, fontWeight = FontWeight.Bold)
                                Text("S/ ${"%.2f".format(producto.precio)}", color = TextoGris)
                            }
                            Icon(Icons.Filled.Favorite, contentDescription = null, tint = MoradoStore)
                        }
                    }
                }
            }
        }
    }
}