package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.IconoServicio
import com.tecsup.huellitasvet.ui.components.soles
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel

/**
 * ENTREGADO (ejemplo resuelto). La View solo observa el StateFlow del ViewModel y le avisa los clics:
 * no conoce el DAO ni el Repository.
 */
@Composable
fun ServiciosScreen(
    vm: ServiciosViewModel,
    onEditar: (Int) -> Unit,
    onNuevo: () -> Unit,
    onAtras: (() -> Unit)? = null
) {
    val servicios by vm.serviciosFiltrados.collectAsState()
    val tipoFiltro by vm.tipoFiltro.collectAsState()
    val tipos by vm.tipos.collectAsState()
    val colores = MaterialTheme.colorScheme
    // Chips: Todos + un chip por cada tipo de la BD + Sin tipo (el valor es el id del tipo)
    val filtros = listOf<Pair<String, Int?>>("Todos" to null) +
        tipos.map { it.nombre to it.id } +
        listOf("Sin tipo" to ServiciosViewModel.SIN_TIPO)

    Scaffold(
        topBar = { AppTopBar("Servicios", onAtras = onAtras) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNuevo,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Nuevo servicio")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filtros) { (texto, valor) ->
                        val elegido = valor == tipoFiltro
                        Surface(
                            onClick = { vm.elegirTipo(valor) },
                            shape = RoundedCornerShape(50),
                            color = if (elegido) colores.primary else colores.surface,
                            contentColor = if (elegido) colores.onPrimary else colores.onSurface,
                            border = if (elegido) null else BorderStroke(1.dp, colores.outlineVariant)
                        ) {
                            Text(texto, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                        }
                    }
                }
            }

            if (servicios.isEmpty()) {
                item {
                    Text("No hay servicios de este tipo", color = colores.onSurfaceVariant, modifier = Modifier.padding(vertical = 24.dp))
                }
            }

            items(servicios, key = { it.id }) { s ->
                ServicioItem(
                    s = s,
                    nombreTipo = tipos.find { it.id == s.tipoId }?.nombre ?: "Sin tipo",
                    onToggle = { vm.alternar(s) },
                    onEditar = { onEditar(s.id) }
                )
            }
        }
    }
}

@Composable
private fun ServicioItem(s: Servicio, nombreTipo: String, onToggle: () -> Unit, onEditar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconoServicio(s.icono)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(s.nombre, style = MaterialTheme.typography.titleMedium)
                Text(
                    "$nombreTipo · ${s.duracionMin} min · ${soles(s.precio)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(checked = s.activo, onCheckedChange = { onToggle() })
            IconButton(onClick = onEditar) {
                Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
