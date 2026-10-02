package com.lucasinga.semana06_tecsupstore.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasinga.semana06_tecsupstore.components.TarjetaProducto
import com.lucasinga.semana06_tecsupstore.model.categorias
import com.lucasinga.semana06_tecsupstore.model.productos
import com.lucasinga.semana06_tecsupstore.ui.theme.MoradoStore
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(onMenuClick: () -> Unit) {
    // chip elegido; al cambiar se vuelve a dibujar la lista filtrada
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }

    val productosFiltrados = if (categoriaSeleccionada == "Todos") {
        productos
    } else {
        productos.filter { it.categoria == categoriaSeleccionada }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("TECSUP Store", fontWeight = FontWeight.Bold)
                        Text("Mas vendidos", fontSize = 13.sp)
                    }
                },
                navigationIcon = {
                    // icono ☰: abre el drawer (la funcion llega desde AppNavegacion)
                    IconButton(onClick = onMenuClick) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MoradoStore,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categorias) { categoria ->
                        FilterChip(
                            selected = categoria == categoriaSeleccionada,
                            onClick = { categoriaSeleccionada = categoria },
                            label = { Text(categoria) },
                            shape = RoundedCornerShape(50),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MoradoStore,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
            items(productosFiltrados) { producto ->
                TarjetaProducto(producto)
            }
        }
    }
}