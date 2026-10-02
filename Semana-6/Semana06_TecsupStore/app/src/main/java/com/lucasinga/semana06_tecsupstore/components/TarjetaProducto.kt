package com.lucasinga.semana06_tecsupstore.components

import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lucasinga.semana06_tecsupstore.model.Producto
import com.lucasinga.semana06_tecsupstore.ui.theme.LilaClaro
import com.lucasinga.semana06_tecsupstore.ui.theme.MoradoStore
import com.lucasinga.semana06_tecsupstore.ui.theme.TextoGris
import androidx.compose.material.icons.filled.FavoriteBorder

@Composable
fun TarjetaProducto(producto: Producto, esFavorito: Boolean, onFavorito: () -> Unit) {
    // cada tarjeta guarda si su propio menu esta abierto o cerrado
    var expanded by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(48.dp).background(LilaClaro, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.ShoppingCart, contentDescription = null, tint = MoradoStore)
            }
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text(producto.nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("S/ ${"%.2f".format(producto.precio)}", color = TextoGris)
            }
            // Box: el menu se abre pegado al icono que lo activa
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "Opciones")
                }
                // onDismissRequest: se cierra al tocar fuera del menu
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    // leadingIcon: icono a la izquierda de cada opcion
                    // la tarjeta no guarda la lista, solo avisa hacia arriba con onFavorito
                    DropdownMenuItem(
                        text = { Text(if (esFavorito) "Quitar de favoritos" else "Favoritos") },
                        onClick = {
                            onFavorito()
                            expanded = false
                        },
                        leadingIcon = {
                            Icon(
                                if (esFavorito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = null
                            )
                        }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text("Compartir") },
                        onClick = { expanded = false },
                        leadingIcon = { Icon(Icons.Filled.Share, contentDescription = null) }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text("Reportar") },
                        onClick = { expanded = false },
                        leadingIcon = { Icon(Icons.Filled.Warning, contentDescription = null) }
                    )
                }
            }
        }
    }
}