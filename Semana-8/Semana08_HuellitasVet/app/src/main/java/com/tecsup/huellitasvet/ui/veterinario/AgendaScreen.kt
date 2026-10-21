package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.ui.components.AccionTexto
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.CitaCard
import com.tecsup.huellitasvet.ui.components.FechaChip
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.viewmodel.AgendaViewModel

/**
 * Agenda del dia: chips de fechas y una linea de tiempo por hora.
 * PENDIENTE -> boton "Confirmar"; CONFIRMADA -> boton "Atender".
 */
@Composable
fun AgendaScreen(
    vm: AgendaViewModel,
    onAtender: (Int) -> Unit
) {
    val fecha by vm.fechaSeleccionada.collectAsState()
    val citas by vm.citas.collectAsState()
    val colores = MaterialTheme.colorScheme

    Scaffold(
        topBar = { AppTopBar("Agenda") },
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
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(vm.fechas) { f ->
                        FechaChip(fecha = f, seleccionado = f == fecha, onClick = { vm.elegirFecha(f) })
                    }
                }
                Spacer(Modifier.height(6.dp))
            }

            if (citas.isEmpty()) {
                item {
                    Text("No hay citas este día", color = colores.onSurfaceVariant, modifier = Modifier.padding(vertical = 24.dp))
                }
            }

            items(citas, key = { it.cita.id }) { c ->
                FilaAgenda(
                    c = c,
                    onConfirmar = { vm.confirmar(c) },
                    onAtender = { onAtender(c.cita.id) },
                    onCancelar = { vm.cancelar(c) }
                )
            }
        }
    }
}

/** Una fila de la linea de tiempo: la hora a la izquierda y la tarjeta de la cita. */
@Composable
private fun FilaAgenda(
    c: CitaConDetalle,
    onConfirmar: () -> Unit,
    onAtender: () -> Unit,
    onCancelar: () -> Unit
) {
    val estado = EstadoCita.de(c.cita.estado)
    Row(verticalAlignment = Alignment.Top) {
        Text(
            c.cita.hora,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .width(52.dp)
                .padding(top = 16.dp)
        )
        CitaCard(
            titulo = "${c.mascota.mascota.nombre} · ${c.servicio.nombre}",
            subtitulo = "Dueño: ${c.mascota.dueno.nombre}",
            estado = estado,
            icono = { MascotaAvatar(especie = Especie.de(c.mascota.mascota.especie), tamano = 44.dp) },
            modifier = Modifier.weight(1f),
            accion = when (estado) {
                EstadoCita.PENDIENTE -> { { AccionesCita("Confirmar", onConfirmar, onCancelar) } }
                EstadoCita.CONFIRMADA -> { { AccionesCita("Atender", onAtender, onCancelar) } }
                else -> null
            }
        )
    }
}

/** Boton principal (Confirmar o Atender) y debajo "Cancelar". */
@Composable
private fun AccionesCita(texto: String, onClick: () -> Unit, onCancelar: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(texto, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
        }
        AccionTexto("Cancelar", onClick = onCancelar, peligro = true)
    }
}