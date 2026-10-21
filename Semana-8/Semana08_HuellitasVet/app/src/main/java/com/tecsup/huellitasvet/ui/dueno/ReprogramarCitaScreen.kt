package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
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
import com.tecsup.huellitasvet.ui.components.FechaChip
import com.tecsup.huellitasvet.ui.components.HoraChip
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.fechaCorta
import com.tecsup.huellitasvet.viewmodel.ReprogramarCitaViewModel

/**
 * Pantalla para reprogramar una cita del dueño.
 * Muestra la cita actual, selector de fecha (de hoy a 7 dias) y grilla de horas de 4 columnas.
 */
@Composable
fun ReprogramarCitaScreen(
    vm: ReprogramarCitaViewModel,
    onVolver: () -> Unit,
    onListo: () -> Unit
) {
    val citaActual by vm.citaActual.collectAsState()
    val fecha by vm.fecha.collectAsState()
    val hora by vm.hora.collectAsState()
    val noDisponibles by vm.horasNoDisponibles.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val colores = MaterialTheme.colorScheme

    Scaffold(
        topBar = { AppTopBar("Reprogramar cita", onAtras = onVolver) },
        containerColor = colores.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {

            // Cita actual
            citaActual?.let { c ->
                Titulo("Cita actual")
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = colores.surface,
                    border = BorderStroke(1.dp, colores.outlineVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        MascotaAvatar(especie = Especie.de(c.mascota.mascota.especie), tamano = 40.dp)
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("${c.mascota.mascota.nombre} · ${c.servicio.nombre}", fontWeight = FontWeight.SemiBold)
                            Text(
                                "${fechaCorta(c.cita.fecha)} a las ${c.cita.hora}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = colores.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Nueva Fecha: de hoy a 7 dias
            Titulo("Nueva fecha")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(vm.fechas) { f ->
                    FechaChip(fecha = f, seleccionado = f == fecha, onClick = { vm.elegirFecha(f) })
                }
            }

            // Nueva Hora: 18 horarios en filas de 4
            Row(verticalAlignment = Alignment.CenterVertically) {
                Titulo("Nueva hora", modifier = Modifier.weight(1f))
                Text("Ocupado = gris", style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
            }
            vm.horarios.chunked(4).forEach { fila ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    fila.forEach { h ->
                        HoraChip(
                            hora = h,
                            seleccionado = h == hora,
                            ocupado = h in noDisponibles,
                            onClick = { vm.elegirHora(h) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    repeat(4 - fila.size) { Spacer(Modifier.weight(1f)) }
                }
            }

            // Resumen de la reprogramacion
            if (hora != null && citaActual != null) {
                Spacer(Modifier.height(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colores.primaryContainer, RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Text("Nuevo horario seleccionado", fontWeight = FontWeight.SemiBold, color = colores.onPrimaryContainer)
                    Text(
                        "${fechaCorta(fecha.toString())} · $hora",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colores.onPrimaryContainer
                    )
                }
            }

            mensaje?.let {
                Spacer(Modifier.height(10.dp))
                Text(it, color = colores.error, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(Modifier.height(16.dp))
            VetButton(
                texto = "Guardar nuevo horario",
                onClick = { vm.confirmar(onListo) },
                habilitado = hora != null
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun Titulo(texto: String, modifier: Modifier = Modifier) {
    Text(
        texto,
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier.padding(top = 16.dp, bottom = 10.dp)
    )
}
