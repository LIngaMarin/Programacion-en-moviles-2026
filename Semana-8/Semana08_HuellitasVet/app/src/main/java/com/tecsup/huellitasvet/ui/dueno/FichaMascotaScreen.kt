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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Vacuna
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.PuntoPeso
import com.tecsup.huellitasvet.model.Sexo
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.HistorialItem
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.ui.components.edad
import com.tecsup.huellitasvet.ui.components.fechaCorta
import com.tecsup.huellitasvet.ui.components.graficos.GraficoLineas
import com.tecsup.huellitasvet.ui.components.kilos
import com.tecsup.huellitasvet.ui.theme.EstadoCancelada
import com.tecsup.huellitasvet.ui.theme.EstadoConfirmada
import com.tecsup.huellitasvet.viewmodel.MascotaViewModel
import java.time.LocalDate

/**
 * Ficha de la mascota: cabecera, botones y pestañas Datos / Historial clínico / Vacunas.
 * onPedirCita y onEditar son opcionales: el veterinario ve la ficha sin esos botones.
 * puedeAgregarVacuna = true habilita el botón de agregar vacunas (solo para veterinario).
 */
@Composable
fun FichaMascotaScreen(
    vm: MascotaViewModel,
    onVolver: () -> Unit,
    onPedirCita: (() -> Unit)? = null,
    onEditar: (() -> Unit)? = null,
    puedeAgregarVacuna: Boolean = false
) {
    val mascota by vm.mascota.collectAsState()
    val historial by vm.historial.collectAsState()
    val pesos by vm.pesos.collectAsState()
    val vacunas by vm.vacunas.collectAsState()
    var pestana by rememberSaveable { mutableIntStateOf(1) }   // 0 = Datos, 1 = Historial clínico, 2 = Vacunas

    Scaffold(
        topBar = { AppTopBar(mascota?.nombre ?: "Mascota", onAtras = onVolver) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        val m = mascota
        if (m == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Cargando…", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Cabecera(m)
                Spacer(Modifier.height(14.dp))

                if (onPedirCita != null || onEditar != null) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        if (onPedirCita != null) {
                            VetButton(texto = "Pedir cita", onClick = onPedirCita, icono = Icons.Filled.DateRange, modifier = Modifier.weight(1f))
                        }
                        if (onEditar != null) {
                            VetButton(texto = "Editar", onClick = onEditar, icono = Icons.Filled.Edit, contorno = true, modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }

                Pestanas(seleccionada = pestana, onCambiar = { pestana = it })
                Spacer(Modifier.height(14.dp))

                when (pestana) {
                    0 -> Datos(m)
                    1 -> Historial(historial = historial, pesos = pesos)
                    2 -> Vacunas(vacunas = vacunas, puedeAgregarVacuna = puedeAgregarVacuna, vm = vm)
                }
            }
        }
    }
}

@Composable
private fun Cabecera(m: Mascota) {
    val colores = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = colores.surface),
        border = BorderStroke(1.dp, colores.outlineVariant)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            MascotaAvatar(especie = Especie.de(m.especie), tamano = 72.dp)
            Spacer(Modifier.width(14.dp))
            Column {
                Text(m.nombre, style = MaterialTheme.typography.titleLarge)
                Text(
                    "${Especie.de(m.especie).etiqueta} · ${m.raza} · ${edad(m.anioNacimiento)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = colores.onSurfaceVariant
                )
                Text(
                    "${Sexo.de(m.sexo).etiqueta} · ${kilos(m.peso)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = colores.onSurfaceVariant
                )
            }
        }
    }
}

/** Tres pestañas: Datos / Historial clínico / Vacunas. */
@Composable
private fun Pestanas(seleccionada: Int, onCambiar: (Int) -> Unit) {
    val colores = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colores.surfaceContainerHighest, RoundedCornerShape(12.dp))
            .padding(4.dp)
    ) {
        listOf("Datos", "Historial clínico", "Vacunas").forEachIndexed { i, texto ->
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
                Text(
                    texto,
                    color = if (elegida) colores.primary else colores.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
private fun Datos(m: Mascota) {
    val colores = MaterialTheme.colorScheme
    val filas = listOf(
        "Especie" to Especie.de(m.especie).etiqueta,
        "Raza" to m.raza.ifBlank { "—" },
        "Sexo" to Sexo.de(m.sexo).etiqueta,
        "Año de nacimiento" to m.anioNacimiento.toString(),
        "Edad" to edad(m.anioNacimiento),
        "Peso actual" to kilos(m.peso)
    )
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colores.surface),
        border = BorderStroke(1.dp, colores.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
            filas.forEachIndexed { i, (titulo, valor) ->
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
                    Text(titulo, color = colores.onSurfaceVariant, modifier = Modifier.weight(1f))
                    Text(valor, fontWeight = FontWeight.SemiBold)
                }
                if (i < filas.lastIndex) HorizontalDivider(color = colores.outlineVariant)
            }
        }
    }
}

@Composable
private fun Historial(historial: List<CitaConDetalle>, pesos: List<PuntoPeso>) {
    Column {
        if (pesos.size >= 2) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Evolución del peso",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))
                    GraficoLineas(puntos = pesos)
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        if (historial.isEmpty()) {
            Text(
                "Aún no tiene atenciones",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
            )
        } else {
            historial.forEachIndexed { i, c ->
                HistorialItem(
                    fecha = c.cita.fecha,
                    servicio = c.servicio.nombre,
                    diagnostico = c.cita.diagnostico,
                    tratamiento = c.cita.tratamiento,
                    peso = c.cita.pesoRegistrado,
                    esUltimo = i == historial.lastIndex
                )
            }
        }
    }
}

/** Carnet de vacunas de la mascota. */
@Composable
private fun Vacunas(
    vacunas: List<Vacuna>,
    puedeAgregarVacuna: Boolean,
    vm: MascotaViewModel
) {
    var mostrarDialogo by remember { mutableStateOf(false) }

    Column {
        if (puedeAgregarVacuna) {
            VetButton(
                texto = "Agregar vacuna",
                onClick = {
                    vm.limpiarErroresVacuna()
                    mostrarDialogo = true
                },
                icono = Icons.Filled.Add,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
        }

        if (vacunas.isEmpty()) {
            Text(
                "Aún no tiene vacunas registradas",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                vacunas.forEach { vacuna ->
                    VacunaCard(
                        vacuna = vacuna,
                        esVencida = vm.esVacunaVencida(vacuna),
                        puedeEliminar = puedeAgregarVacuna,
                        onEliminar = { vm.eliminarVacuna(vacuna) }
                    )
                }
            }
        }
    }

    if (mostrarDialogo) {
        AgregarVacunaDialog(
            vm = vm,
            onDismiss = {
                mostrarDialogo = false
                vm.limpiarErroresVacuna()
            },
            onGuardar = { nombre, fecha, proximaDosis ->
                vm.agregarVacuna(nombre, fecha, proximaDosis) {
                    mostrarDialogo = false
                }
            }
        )
    }
}

@Composable
private fun VacunaCard(
    vacuna: Vacuna,
    esVencida: Boolean,
    puedeEliminar: Boolean,
    onEliminar: () -> Unit
) {
    val colores = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colores.surface),
        border = BorderStroke(1.dp, colores.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = vacuna.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val colorEtiqueta = if (esVencida) EstadoCancelada else EstadoConfirmada
                    Box(
                        modifier = Modifier
                            .background(colorEtiqueta.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (esVencida) "Vencida" else "Al día",
                            color = colorEtiqueta,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (puedeEliminar) {
                        Spacer(Modifier.width(4.dp))
                        IconButton(onClick = onEliminar) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Eliminar vacuna",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            val fechaAplicacionTexto = try { fechaCorta(vacuna.fecha) } catch (e: Exception) { vacuna.fecha }
            Text(
                text = "Fecha de aplicación: $fechaAplicacionTexto",
                style = MaterialTheme.typography.bodyMedium,
                color = colores.onSurface
            )

            val proxTexto = vacuna.proximaDosis?.let { prox ->
                try { fechaCorta(prox) } catch (e: Exception) { prox }
            } ?: "—"

            Text(
                text = "Próxima dosis: $proxTexto",
                style = MaterialTheme.typography.bodyMedium,
                color = colores.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AgregarVacunaDialog(
    vm: MascotaViewModel,
    onDismiss: () -> Unit,
    onGuardar: (nombre: String, fecha: String, proximaDosis: String?) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf(LocalDate.now().toString()) }
    var proximaDosis by remember { mutableStateOf("") }

    val errores by vm.erroresVacuna.collectAsState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Agregar vacuna",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                VetTextField(
                    valor = nombre,
                    onValorCambia = { nombre = it },
                    etiqueta = "Nombre de la vacuna",
                    placeholder = "Ej. Triple Felina",
                    error = errores.nombre
                )
                VetTextField(
                    valor = fecha,
                    onValorCambia = { fecha = it },
                    etiqueta = "Fecha de aplicación (aaaa-MM-dd)",
                    placeholder = "aaaa-MM-dd",
                    error = errores.fecha
                )
                VetTextField(
                    valor = proximaDosis,
                    onValorCambia = { proximaDosis = it },
                    etiqueta = "Próxima dosis (opcional)",
                    placeholder = "aaaa-MM-dd",
                    error = errores.proximaDosis
                )
            }
        },
        confirmButton = {
            VetButton(
                texto = "Guardar",
                onClick = { onGuardar(nombre, fecha, proximaDosis) },
                modifier = Modifier.fillMaxWidth()
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
