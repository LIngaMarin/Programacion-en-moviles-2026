package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.ui.components.AccionTexto
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.CalendarioMensual
import com.tecsup.huellitasvet.ui.components.CitaCard
import com.tecsup.huellitasvet.ui.components.FechaChip
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.viewmodel.AgendaViewModel

private enum class ModoAgenda { SEMANA, MES }

/**
 * Agenda del dia: selector Semana/Mes, chips de fechas o calendario mensual,
 * y una linea de tiempo por hora.
 * PENDIENTE -> boton "Confirmar"; CONFIRMADA -> boton "Atender".
 */
@Composable
fun AgendaScreen(
    vm: AgendaViewModel,
    onAtender: (Int) -> Unit
) {
    val fecha by vm.fechaSeleccionada.collectAsState()
    val mesVisible by vm.mesVisible.collectAsState()
    val diasConCitas by vm.diasConCitas.collectAsState()
    val citas by vm.citas.collectAsState()
    val colores = MaterialTheme.colorScheme

    var modoAgenda by rememberSaveable { mutableStateOf(ModoAgenda.SEMANA) }

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
                SelectorVistaAgenda(
                    modo = modoAgenda,
                    onCambiar = { modoAgenda = it }
                )
                Spacer(Modifier.height(10.dp))
            }

            item {
                when (modoAgenda) {
                    ModoAgenda.SEMANA -> {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(vm.fechas) { f ->
                                FechaChip(fecha = f, seleccionado = f == fecha, onClick = { vm.elegirFecha(f) })
                            }
                        }
                    }
                    ModoAgenda.MES -> {
                        CalendarioMensual(
                            mesVisible = mesVisible,
                            fechaSeleccionada = fecha,
                            diasConCitas = diasConCitas,
                            onMesAnterior = { vm.mesAnterior() },
                            onMesSiguiente = { vm.mesSiguiente() },
                            onSeleccionarFecha = { f -> vm.elegirFecha(f) }
                        )
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

/** Dos opciones: Semana / Mes. */
@Composable
private fun SelectorVistaAgenda(modo: ModoAgenda, onCambiar: (ModoAgenda) -> Unit) {
    val colores = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colores.surfaceContainerHighest, RoundedCornerShape(12.dp))
            .padding(4.dp)
    ) {
        listOf(ModoAgenda.SEMANA to "Semana", ModoAgenda.MES to "Mes").forEach { (m, texto) ->
            val elegida = m == modo
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (elegida) colores.surface else Color.Transparent)
                    .clickable { onCambiar(m) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    texto,
                    color = if (elegida) colores.primary else colores.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
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
