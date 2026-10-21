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
import com.tecsup.huellitasvet.ui.components.IconoServicio
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.fechaCorta
import com.tecsup.huellitasvet.ui.components.soles
import com.tecsup.huellitasvet.viewmodel.PedirCitaViewModel

/**
 * Pedir cita: mascota, servicio, fecha y hora, con un resumen antes de confirmar.
 * La pantalla solo muestra lo que expone el ViewModel y le avisa cada eleccion.
 */
@Composable
fun PedirCitaScreen(
    vm: PedirCitaViewModel,
    onVolver: () -> Unit,
    onCreada: () -> Unit
) {
    val mascotas by vm.mascotas.collectAsState()
    val servicios by vm.servicios.collectAsState()
    val mascotaId by vm.mascotaId.collectAsState()
    val servicioId by vm.servicioId.collectAsState()
    val fecha by vm.fecha.collectAsState()
    val hora by vm.hora.collectAsState()
    val noDisponibles by vm.horasNoDisponibles.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val aceptaCitas by vm.aceptaCitas.collectAsState()
    val colores = MaterialTheme.colorScheme

    val mascota = mascotas.find { it.id == mascotaId }
    val servicio = servicios.find { it.id == servicioId }
    val completo = mascota != null && servicio != null && hora != null

    Scaffold(
        topBar = { AppTopBar("Pedir cita", onAtras = onVolver) },
        containerColor = colores.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {

            // Aviso si el veterinario apago "Aceptar citas"
            if (!aceptaCitas) {
                Text(
                    "La clínica no está recibiendo citas",
                    color = colores.error,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colores.error.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                )
            }

            // ¿Quien viene?
            Titulo("¿Quién viene?")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(mascotas, key = { it.id }) { m ->
                    val elegida = m.id == mascotaId
                    Surface(
                        onClick = { vm.elegirMascota(m.id) },
                        shape = RoundedCornerShape(50),
                        color = if (elegida) colores.primaryContainer else colores.surface,
                        border = BorderStroke(if (elegida) 1.5.dp else 1.dp, if (elegida) colores.primary else colores.outlineVariant)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 6.dp, end = 14.dp, top = 6.dp, bottom = 6.dp)
                        ) {
                            MascotaAvatar(especie = Especie.de(m.especie), tamano = 30.dp)
                            Spacer(Modifier.width(8.dp))
                            Text(m.nombre, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // Servicio (solo activos)
            Titulo("Servicio")
            servicios.forEach { s ->
                val elegido = s.id == servicioId
                Surface(
                    onClick = { vm.elegirServicio(s.id) },
                    shape = RoundedCornerShape(14.dp),
                    color = if (elegido) colores.primaryContainer else colores.surface,
                    border = BorderStroke(if (elegido) 1.5.dp else 1.dp, if (elegido) colores.primary else colores.outlineVariant),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                        IconoServicio(s.icono, tamano = 36.dp)
                        Spacer(Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(s.nombre, fontWeight = FontWeight.SemiBold)
                            Text("${s.duracionMin} min", style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
                        }
                        Text(soles(s.precio), color = colores.primary, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Fecha: de hoy a 7 dias
            Titulo("Fecha")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(vm.fechas) { f ->
                    FechaChip(fecha = f, seleccionado = f == fecha, onClick = { vm.elegirFecha(f) })
                }
            }

            // Hora: 18 horarios en filas de 4
            Row(verticalAlignment = Alignment.CenterVertically) {
                Titulo("Hora", modifier = Modifier.weight(1f))
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
                    // Completa la ultima fila para que los chips tengan el mismo ancho
                    repeat(4 - fila.size) { Spacer(Modifier.weight(1f)) }
                }
            }

            // Resumen
            if (completo) {
                Spacer(Modifier.height(8.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colores.primaryContainer, RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Text("${mascota?.nombre} · ${servicio?.nombre}", fontWeight = FontWeight.SemiBold, color = colores.onPrimaryContainer)
                    Text(
                        "${fechaCorta(fecha.toString())} · $hora · ${soles(servicio?.precio ?: 0.0)}",
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
                texto = "Confirmar cita",
                onClick = { vm.confirmar(onCreada) },
                habilitado = completo && aceptaCitas
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