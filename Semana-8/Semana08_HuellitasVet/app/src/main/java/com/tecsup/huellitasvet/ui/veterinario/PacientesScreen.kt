package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.diaMes
import com.tecsup.huellitasvet.ui.components.edad
import com.tecsup.huellitasvet.viewmodel.Paciente
import com.tecsup.huellitasvet.viewmodel.PacientesViewModel

/** Pacientes: buscador, chips por especie y tarjetas. Tocar una tarjeta abre la ficha con el historial. */
@Composable
fun PacientesScreen(
    vm: PacientesViewModel,
    onVerPaciente: (Int) -> Unit
) {
    val pacientes by vm.pacientes.collectAsState()
    val texto by vm.texto.collectAsState()
    val especie by vm.especie.collectAsState()
    val colores = MaterialTheme.colorScheme

    // Chips: Todos / Perros / Gatos / Otros (null = Todos)
    val filtros = listOf<Pair<String, Especie?>>(
        "Todos" to null,
        "Perros" to Especie.PERRO,
        "Gatos" to Especie.GATO,
        "Otros" to Especie.OTRO
    )

    Scaffold(
        topBar = { AppTopBar("Pacientes") },
        containerColor = colores.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                OutlinedTextField(
                    value = texto,
                    onValueChange = { vm.cambiarTexto(it) },
                    placeholder = { Text("Buscar mascota o dueño…") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colores.surface,
                        unfocusedContainerColor = colores.surface
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filtros) { (texto, valor) ->
                        val elegido = valor == especie
                        Surface(
                            onClick = { vm.elegirEspecie(valor) },
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

            if (pacientes.isEmpty()) {
                item {
                    Text("No se encontraron pacientes", color = colores.onSurfaceVariant, modifier = Modifier.padding(vertical = 24.dp))
                }
            }

            items(pacientes, key = { it.datos.mascota.id }) { p ->
                TarjetaPaciente(p, onClick = { onVerPaciente(p.datos.mascota.id) })
            }
        }
    }
}

@Composable
private fun TarjetaPaciente(p: Paciente, onClick: () -> Unit) {
    val m = p.datos.mascota
    val colores = MaterialTheme.colorScheme
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
                Text("Dueño: ${p.datos.dueno.nombre}", style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
                Text(
                    p.ultimaVisita?.let { "Última visita: ${diaMes(it)}" } ?: "Sin visitas",
                    style = MaterialTheme.typography.bodySmall,
                    color = colores.primary
                )
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = colores.onSurfaceVariant)
        }
    }
}