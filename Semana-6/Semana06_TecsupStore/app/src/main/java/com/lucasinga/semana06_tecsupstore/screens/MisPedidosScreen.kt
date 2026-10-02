package com.lucasinga.semana06_tecsupstore.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lucasinga.semana06_tecsupstore.components.BarraSuperior
import com.lucasinga.semana06_tecsupstore.model.productos
import com.lucasinga.semana06_tecsupstore.ui.theme.MoradoStore
import com.lucasinga.semana06_tecsupstore.ui.theme.TextoGris

@Composable
fun MisPedidosScreen(onMenuClick: () -> Unit) {
    // pedidos de ejemplo tomados de la lista de productos
    val pedidos = listOf(productos[0], productos[2])

    Scaffold(
        topBar = { BarraSuperior("Mis pedidos", onMenuClick) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(pedidos) { producto ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(producto.nombre, fontWeight = FontWeight.Bold)
                            Text("S/ ${"%.2f".format(producto.precio)}", color = TextoGris)
                        }
                        Text("Entregado", color = MoradoStore, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}