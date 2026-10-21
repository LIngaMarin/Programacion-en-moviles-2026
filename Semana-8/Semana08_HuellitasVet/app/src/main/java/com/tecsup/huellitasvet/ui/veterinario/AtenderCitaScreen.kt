package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.model.Sexo
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.EstadoCitaChip
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.ui.components.edad
import com.tecsup.huellitasvet.ui.components.fechaCorta
import com.tecsup.huellitasvet.ui.components.kilos
import com.tecsup.huellitasvet.viewmodel.AtenderCitaViewModel

/**
 * Registro clinico de una cita: peso, diagnostico y tratamiento.
 * Al registrar, la cita queda ATENDIDA y el peso de la mascota se actualiza (transaccion en el Repository).
 */
@Composable
fun AtenderCitaScreen(
    vm: AtenderCitaViewModel,
    onVolver: () -> Unit
) {
    val cita by vm.cita.collectAsState()
    val errores by vm.errores.collectAsState()
    val mensaje by vm.mensaje.collectAsState()

    var peso by rememberSaveable { mutableStateOf("") }
    var diagnostico by rememberSaveable { mutableStateOf("") }
    var tratamiento by rememberSaveable { mutableStateOf("") }
    var cargado by rememberSaveable { mutableStateOf(false) }

    // Pone el peso anterior de la mascota como valor inicial (una sola vez)
    LaunchedEffect(cita) {
        val c = cita
        if (c != null && !cargado) {
            peso = c.mascota.mascota.peso.toString()
            cargado = true
        }
    }

    val colores = MaterialTheme.colorScheme

    Scaffold(
        topBar = { AppTopBar("Atender cita", onAtras = onVolver) },
        containerColor = colores.background
    ) { padding ->
        val c = cita
        if (c == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Cargando…", color = colores.onSurfaceVariant)
            }
        } else {
            val m = c.mascota.mascota
            val atendible = vm.esAtendible(c)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                // Cabecera: mascota, dueño, servicio y hora
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colores.surface),
                    border = BorderStroke(1.dp, colores.outlineVariant)
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        MascotaAvatar(especie = Especie.de(m.especie), tamano = 56.dp)
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("${m.nombre} · ${m.raza}", style = MaterialTheme.typography.titleMedium)
                            Text(
                                "${edad(m.anioNacimiento)} · ${Sexo.de(m.sexo).etiqueta} · Dueño: ${c.mascota.dueno.nombre}",
                                style = MaterialTheme.typography.bodySmall,
                                color = colores.onSurfaceVariant
                            )
                            Text(
                                "${c.servicio.nombre} · ${fechaCorta(c.cita.fecha)} · ${c.cita.hora}",
                                style = MaterialTheme.typography.bodySmall,
                                color = colores.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        EstadoCitaChip(EstadoCita.de(c.cita.estado))
                    }
                }
                Spacer(Modifier.height(18.dp))

                Text("Registro clínico", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    VetTextField(
                        valor = peso,
                        onValorCambia = { peso = it },
                        etiqueta = "Peso (kg)",
                        error = errores.peso,
                        teclado = KeyboardType.Decimal,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(10.dp))
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(colores.primaryContainer, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Text("Peso anterior", style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
                        Text(kilos(m.peso), fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.height(10.dp))

                VetTextField(
                    valor = diagnostico,
                    onValorCambia = { diagnostico = it },
                    etiqueta = "Diagnóstico",
                    error = errores.diagnostico,
                    lineas = 3
                )
                Spacer(Modifier.height(10.dp))
                VetTextField(
                    valor = tratamiento,
                    onValorCambia = { tratamiento = it },
                    etiqueta = "Tratamiento",
                    lineas = 3
                )
                Spacer(Modifier.height(10.dp))

                // Aviso: el peso de la mascota se actualizara
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colores.primaryContainer, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = colores.primary)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Se actualizará el peso de ${m.nombre} a ${peso.ifBlank { "—" }} kg",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(Modifier.height(18.dp))

                VetButton(
                    texto = "Registrar atención",
                    onClick = { vm.registrar(diagnostico, tratamiento, peso, onListo = onVolver) },
                    habilitado = atendible
                )
                Spacer(Modifier.height(10.dp))
                VetButton(
                    texto = "Cancelar cita",
                    onClick = { vm.cancelar(onListo = onVolver) },
                    habilitado = atendible,
                    contorno = true,
                    peligro = true
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }

    mensaje?.let {
        AlertDialog(
            onDismissRequest = { vm.limpiarMensaje() },
            confirmButton = { TextButton(onClick = { vm.limpiarMensaje() }) { Text("Entendido") } },
            text = { Text(it) },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}