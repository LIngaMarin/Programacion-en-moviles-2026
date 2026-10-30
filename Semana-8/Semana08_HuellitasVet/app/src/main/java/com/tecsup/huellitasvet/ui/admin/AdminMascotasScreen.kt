package com.tecsup.huellitasvet.ui.admin

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.data.local.relation.MascotaConDueno
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.edad
import com.tecsup.huellitasvet.viewmodel.AdminViewModel

/** Mascotas del administrador: todas las mascotas con su dueño y eliminar con confirmacion. */
@Composable
fun AdminMascotasScreen(vm: AdminViewModel) {
    val mascotas by vm.mascotas.collectAsState()
    var porEliminar by remember { mutableStateOf<MascotaConDueno?>(null) }
    val colores = MaterialTheme.colorScheme

    Scaffold(
        topBar = { AppTopBar("Mascotas") },
        containerColor = colores.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (mascotas.isEmpty()) {
                item {
                    Text("No hay mascotas registradas", color = colores.onSurfaceVariant, modifier = Modifier.padding(vertical = 24.dp))
                }
            }
            items(mascotas, key = { it.mascota.id }) { p ->
                TarjetaMascotaAdmin(p, onEliminar = { porEliminar = p })
            }
        }
    }

    // Confirmacion antes de eliminar
    porEliminar?.let { p ->
        AlertDialog(
            onDismissRequest = { porEliminar = null },
            confirmButton = {
                TextButton(onClick = {
                    vm.eliminarMascota(p.mascota)
                    porEliminar = null
                }) { Text("Eliminar", color = colores.error) }
            },
            dismissButton = { TextButton(onClick = { porEliminar = null }) { Text("Cancelar") } },
            title = { Text("¿Eliminar a ${p.mascota.nombre}?") },
            text = { Text("También se eliminarán sus citas y vacunas.") },
            containerColor = colores.surface
        )
    }
}

@Composable
private fun TarjetaMascotaAdmin(p: MascotaConDueno, onEliminar: () -> Unit) {
    val m = p.mascota
    val colores = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colores.surface),
        border = BorderStroke(1.dp, colores.outlineVariant)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            MascotaAvatar(especie = Especie.de(m.especie), tamano = 52.dp)
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(m.nombre, style = MaterialTheme.typography.titleMedium)
                Text("${m.raza} · ${edad(m.anioNacimiento)}", style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
                Text("Dueño: ${p.dueno.nombre}", style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
            }
            IconButton(onClick = onEliminar) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = colores.error)
            }
        }
    }
}
