package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.ui.components.AccionTexto
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.CitaCard
import com.tecsup.huellitasvet.ui.components.IconoServicio
import com.tecsup.huellitasvet.ui.components.fechaCorta
import com.tecsup.huellitasvet.ui.components.soles
import com.tecsup.huellitasvet.ui.theme.EstadoPendiente
import com.tecsup.huellitasvet.viewmodel.CitaItem
import com.tecsup.huellitasvet.viewmodel.MisCitasViewModel

/** Mis citas: pestañas Proximas / Historial, cancelar con confirmacion y FAB para pedir otra. */
@Composable
fun MisCitasScreen(
    vm: MisCitasViewModel,
    onPedirCita: () -> Unit
) {
    val proximas by vm.proximas.collectAsState()
    val historial by vm.historial.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    var pestana by rememberSaveable { mutableIntStateOf(0) }       // 0 = Proximas, 1 = Historial
    var porCancelar by remember { mutableStateOf<CitaConDetalle?>(null) }
    val colores = MaterialTheme.colorScheme

    Scaffold(
        topBar = { AppTopBar("Mis citas") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onPedirCita,
                containerColor = colores.primary,
                contentColor = colores.onPrimary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Pedir una cita")
            }
        },
        containerColor = colores.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 88.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item { PestanasCitas(pestana, onCambiar = { pestana = it }) }

            val lista = if (pestana == 0) proximas else historial
            if (lista.isEmpty()) {
                item {
                    Text(
                        if (pestana == 0) "No tienes citas próximas" else "Aún no hay historial",
                        color = colores.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 24.dp)
                    )
                }
            }
            items(lista, key = { it.detalle.cita.id }) { item ->
                TarjetaCita(item, onCancelar = { porCancelar = item.detalle })
            }

            if (pestana == 0) {
                item {
                    // Aviso de la regla de las 2 horas
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(EstadoPendiente.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Icon(Icons.Filled.Info, contentDescription = null, tint = EstadoPendiente)
                        Spacer(Modifier.width(8.dp))
                        Text("Puedes cancelar hasta 2 horas antes", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }

    // Confirmacion antes de cancelar
    porCancelar?.let { c ->
        AlertDialog(
            onDismissRequest = { porCancelar = null },
            title = { Text("¿Cancelar la cita?") },
            text = { Text("${c.mascota.mascota.nombre} · ${c.servicio.nombre}\n${fechaCorta(c.cita.fecha)} a las ${c.cita.hora}") },
            confirmButton = {
                TextButton(onClick = {
                    vm.cancelar(c)
                    porCancelar = null
                }) { Text("Sí, cancelar", color = colores.error) }
            },
            dismissButton = { TextButton(onClick = { porCancelar = null }) { Text("No") } },
            containerColor = colores.surface
        )
    }

    // Mensaje cuando ya no se puede cancelar
    mensaje?.let {
        AlertDialog(
            onDismissRequest = { vm.limpiarMensaje() },
            confirmButton = { TextButton(onClick = { vm.limpiarMensaje() }) { Text("Entendido") } },
            text = { Text(it) },
            containerColor = colores.surface
        )
    }
}

@Composable
private fun TarjetaCita(item: CitaItem, onCancelar: () -> Unit) {
    val c = item.detalle
    val estado = EstadoCita.de(c.cita.estado)
    val activa = estado == EstadoCita.PENDIENTE || estado == EstadoCita.CONFIRMADA
    CitaCard(
        titulo = c.servicio.nombre,
        subtitulo = "${c.mascota.mascota.nombre} · ${fechaCorta(c.cita.fecha)} · ${c.cita.hora}",
        estado = estado,
        icono = { IconoServicio(c.servicio.icono) },
        detalle = when {
            activa && !item.puedeCancelar -> "Ya no puedes cancelar esta cita"
            else -> soles(c.cita.precio)
        },
        accion = if (item.puedeCancelar) {
            { AccionTexto("Cancelar", onClick = onCancelar, peligro = true) }
        } else null
    )
}

/** Dos pestañas: Proximas / Historial. */
@Composable
private fun PestanasCitas(seleccionada: Int, onCambiar: (Int) -> Unit) {
    val colores = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colores.surfaceContainerHighest, RoundedCornerShape(12.dp))
            .padding(4.dp)
    ) {
        listOf("Próximas", "Historial").forEachIndexed { i, texto ->
            val elegida = i == seleccionada
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (elegida) colores.surface else Color.Transparent)
                    .clickable { onCambiar(i) },
                contentAlignment = Alignment.Center
            ) {
                Text(texto, color = if (elegida) colores.primary else colores.onSurfaceVariant, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}