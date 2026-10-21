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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.Sexo
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.HistorialItem
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.edad
import com.tecsup.huellitasvet.ui.components.kilos
import com.tecsup.huellitasvet.viewmodel.MascotaViewModel

/**
 * Ficha de la mascota: cabecera, botones y pestañas Datos / Historial clinico.
 * onPedirCita y onEditar son opcionales: el veterinario ve la ficha sin esos botones.
 */
@Composable
fun FichaMascotaScreen(
    vm: MascotaViewModel,
    onVolver: () -> Unit,
    onPedirCita: (() -> Unit)? = null,
    onEditar: (() -> Unit)? = null
) {
    val mascota by vm.mascota.collectAsState()
    val historial by vm.historial.collectAsState()
    var pestana by rememberSaveable { mutableIntStateOf(1) }   // 0 = Datos, 1 = Historial clinico

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

                if (pestana == 0) Datos(m) else Historial(historial)
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

/** Dos pestañas: Datos / Historial clinico. */
@Composable
private fun Pestanas(seleccionada: Int, onCambiar: (Int) -> Unit) {
    val colores = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colores.surfaceContainerHighest, RoundedCornerShape(12.dp))
            .padding(4.dp)
    ) {
        listOf("Datos", "Historial clínico").forEachIndexed { i, texto ->
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
                    fontWeight = FontWeight.SemiBold
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
private fun Historial(historial: List<CitaConDetalle>) {
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