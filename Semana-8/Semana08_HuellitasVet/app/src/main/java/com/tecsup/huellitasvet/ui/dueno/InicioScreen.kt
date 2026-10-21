package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.fechaCorta
import com.tecsup.huellitasvet.ui.components.iconoDeServicio
import com.tecsup.huellitasvet.ui.theme.EstadoPendiente
import com.tecsup.huellitasvet.viewmodel.InicioViewModel

/**
 * Inicio del dueño: saludo, proxima cita, carrusel de mascotas, accesos a servicios,
 * recordatorio y boton para pedir una cita. Solo observa al ViewModel.
 */
@Composable
fun InicioScreen(
    vm: InicioViewModel,
    nombre: String,
    onVerMascota: (Int) -> Unit,
    onAgregarMascota: () -> Unit,
    onVerTodas: () -> Unit,
    onPedirCita: () -> Unit
) {
    val mascotas by vm.mascotas.collectAsState()
    val proximaCita by vm.proximaCita.collectAsState()
    val recordatorio by vm.recordatorio.collectAsState()
    val colores = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Saludo
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Hola, ${nombre.substringBefore(" ")}", style = MaterialTheme.typography.headlineMedium)
                Text("¿Cómo están tus mascotas hoy?", color = colores.onSurfaceVariant)
            }
            Icon(Icons.Filled.Notifications, contentDescription = "Notificaciones", tint = colores.onSurface)
        }
        Spacer(Modifier.height(18.dp))

        TarjetaProximaCita(proximaCita)
        Spacer(Modifier.height(22.dp))

        // Mis mascotas
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Mis mascotas", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
            Text(
                "Ver todas",
                color = colores.primary,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onVerTodas() }
            )
        }
        Spacer(Modifier.height(10.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(mascotas, key = { it.id }) { m ->
                TarjetaMascotaMini(m, onClick = { onVerMascota(m.id) })
            }
            item { TarjetaAgregar(onClick = onAgregarMascota) }
        }
        Spacer(Modifier.height(22.dp))

        // Servicios: accesos rapidos para pedir una cita
        Text("Servicios", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AccesoServicio("Consulta", "heart", onPedirCita, Modifier.weight(1f))
            AccesoServicio("Vacunas", "syringe", onPedirCita, Modifier.weight(1f))
            AccesoServicio("Baño", "scissors", onPedirCita, Modifier.weight(1f))
        }

        // Recordatorio: la siguiente cita despues de la proxima
        recordatorio?.let { r ->
            Spacer(Modifier.height(18.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(EstadoPendiente.copy(alpha = 0.12f), RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Icon(Icons.Filled.Notifications, contentDescription = null, tint = EstadoPendiente)
                Spacer(Modifier.width(10.dp))
                Column {
                    Text("Recordatorio", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "${r.mascota.mascota.nombre}: ${r.servicio.nombre} el ${fechaCorta(r.cita.fecha)} a las ${r.cita.hora}",
                        style = MaterialTheme.typography.bodySmall,
                        color = colores.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(Modifier.height(22.dp))
        VetButton(texto = "Pedir una cita", onClick = onPedirCita)
    }
}

/** Tarjeta verde con la proxima cita, o el mensaje si no tiene citas. */
@Composable
private fun TarjetaProximaCita(cita: CitaConDetalle?) {
    val colores = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(colores.primary, RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        if (cita == null) {
            Column {
                Text("Próxima cita", color = colores.onPrimary.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
                Text("Aún no tienes citas", color = colores.onPrimary, style = MaterialTheme.typography.titleMedium)
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Próxima cita", color = colores.onPrimary.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
                    Text(
                        "${cita.mascota.mascota.nombre} · ${cita.servicio.nombre}",
                        color = colores.onPrimary,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Schedule, contentDescription = null, tint = colores.onPrimary, modifier = Modifier.size(16.dp))
                        Text(
                            " ${fechaCorta(cita.cita.fecha)} · ${cita.cita.hora}",
                            color = colores.onPrimary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        EstadoCita.de(cita.cita.estado).etiqueta,
                        color = colores.primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .background(colores.onPrimary, RoundedCornerShape(50))
                            .padding(horizontal = 10.dp, vertical = 3.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(colores.onPrimary, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    MascotaAvatar(especie = Especie.de(cita.mascota.mascota.especie), tamano = 48.dp)
                }
            }
        }
    }
}

/** Tarjeta pequeña del carrusel: avatar, nombre y "Ver ficha". */
@Composable
private fun TarjetaMascotaMini(m: Mascota, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            MascotaAvatar(especie = Especie.de(m.especie), tamano = 52.dp)
            Spacer(Modifier.height(6.dp))
            Text(m.nombre, style = MaterialTheme.typography.titleMedium)
            Text("Ver ficha", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/** Ultima tarjeta del carrusel: "+ Agregar". */
@Composable
private fun TarjetaAgregar(onClick: () -> Unit) {
    val colores = MaterialTheme.colorScheme
    Card(
        modifier = Modifier
            .width(100.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colores.surface),
        border = BorderStroke(1.dp, colores.primary)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 22.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = null, tint = colores.primary, modifier = Modifier.size(30.dp))
            Spacer(Modifier.height(8.dp))
            Text("Agregar", color = colores.primary, fontWeight = FontWeight.SemiBold)
        }
    }
}

/** Acceso rapido a un servicio (Consulta, Vacunas, Baño). */
@Composable
private fun AccesoServicio(texto: String, icono: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colores = MaterialTheme.colorScheme
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(colores.primaryContainer, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(vertical = 14.dp)
    ) {
        Icon(iconoDeServicio(icono), contentDescription = null, tint = colores.primary)
        Spacer(Modifier.height(6.dp))
        Text(texto, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
    }
}