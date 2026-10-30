package com.tecsup.huellitasvet.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.local.entity.TipoServicio
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.viewmodel.AdminViewModel
import com.tecsup.huellitasvet.viewmodel.TipoConServicios

/**
 * Tipos de servicio del administrador: crear (+), editar el nombre, eliminar,
 * y elegir que servicios pertenecen a cada tipo (marcar o desmarcar).
 */
@Composable
fun AdminTiposScreen(vm: AdminViewModel) {
    val tipos by vm.tiposConServicios.collectAsState()
    val sinTipo by vm.serviciosSinTipo.collectAsState()
    val todosLosServicios by vm.servicios.collectAsState()
    val todosLosTipos by vm.tipos.collectAsState()
    val errorTipo by vm.errorTipo.collectAsState()
    val colores = MaterialTheme.colorScheme

    // Dialogos: crear/editar nombre, eliminar y elegir servicios
    var dialogoTipo by remember { mutableStateOf<TipoServicio?>(null) }   // id = 0 -> nuevo
    var porEliminar by remember { mutableStateOf<TipoConServicios?>(null) }
    var eligiendo by remember { mutableStateOf<TipoServicio?>(null) }

    Scaffold(
        topBar = { AppTopBar("Tipos de servicio") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { dialogoTipo = TipoServicio(nombre = "") },
                containerColor = colores.primary,
                contentColor = colores.onPrimary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Nuevo tipo")
            }
        },
        containerColor = colores.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (tipos.isEmpty()) {
                item {
                    Text("Aún no hay tipos. Crea uno con el botón +", color = colores.onSurfaceVariant, modifier = Modifier.padding(vertical = 24.dp))
                }
            }
            items(tipos, key = { it.tipo.id }) { t ->
                TarjetaTipo(
                    nombre = t.tipo.nombre,
                    servicios = t.servicios,
                    onEditar = { dialogoTipo = t.tipo },
                    onEliminar = { porEliminar = t },
                    onElegirServicios = { eligiendo = t.tipo }
                )
            }
            if (sinTipo.isNotEmpty()) {
                item {
                    TarjetaTipo(nombre = "Sin tipo", servicios = sinTipo)
                }
            }
        }
    }

    // Crear o editar el nombre del tipo
    dialogoTipo?.let { t ->
        var nombre by remember(t) { mutableStateOf(t.nombre) }
        val cerrar = {
            vm.limpiarErrorTipo()
            dialogoTipo = null
        }
        AlertDialog(
            onDismissRequest = cerrar,
            title = { Text(if (t.id == 0) "Nuevo tipo" else "Editar tipo") },
            text = {
                VetTextField(valor = nombre, onValorCambia = { nombre = it }, etiqueta = "Nombre del tipo", error = errorTipo)
            },
            confirmButton = {
                TextButton(onClick = { vm.guardarTipo(t.id, nombre, onListo = { dialogoTipo = null }) }) { Text("Guardar") }
            },
            dismissButton = { TextButton(onClick = cerrar) { Text("Cancelar") } },
            containerColor = colores.surface
        )
    }

    // Confirmacion antes de eliminar: los servicios no se borran, quedan "Sin tipo"
    porEliminar?.let { t ->
        val cantidad = t.servicios.size
        AlertDialog(
            onDismissRequest = { porEliminar = null },
            title = { Text("¿Eliminar el tipo ${t.tipo.nombre}?") },
            text = {
                Text(
                    if (cantidad == 0) "No tiene servicios."
                    else "Sus $cantidad servicio(s) no se borran: quedarán como \"Sin tipo\"."
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    vm.eliminarTipo(t.tipo)
                    porEliminar = null
                }) { Text("Eliminar", color = colores.error) }
            },
            dismissButton = { TextButton(onClick = { porEliminar = null }) { Text("Cancelar") } },
            containerColor = colores.surface
        )
    }

    // Elegir los servicios del tipo: marcar = agregar al tipo, desmarcar = quitarlo (queda Sin tipo)
    eligiendo?.let { t ->
        AlertDialog(
            onDismissRequest = { eligiendo = null },
            title = { Text("Servicios de ${t.nombre}") },
            text = {
                Column(
                    modifier = Modifier
                        .heightIn(max = 360.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (todosLosServicios.isEmpty()) {
                        Text("No hay servicios creados", color = colores.onSurfaceVariant)
                    }
                    todosLosServicios.forEach { s ->
                        val marcado = s.tipoId == t.id
                        val tipoActual = todosLosTipos.find { it.id == s.tipoId }?.nombre ?: "Sin tipo"
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { vm.asignarServicio(s.id, if (marcado) null else t.id) }
                        ) {
                            Checkbox(
                                checked = marcado,
                                onCheckedChange = { marcar -> vm.asignarServicio(s.id, if (marcar) t.id else null) }
                            )
                            Column {
                                Text(s.nombre, fontWeight = FontWeight.SemiBold)
                                if (!marcado) {
                                    Text("Ahora en: $tipoActual", style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { eligiendo = null }) { Text("Listo") } },
            containerColor = colores.surface
        )
    }
}

/** Tarjeta de un tipo con sus servicios como etiquetas. Sin acciones = tarjeta "Sin tipo". */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TarjetaTipo(
    nombre: String,
    servicios: List<Servicio>,
    onEditar: (() -> Unit)? = null,
    onEliminar: (() -> Unit)? = null,
    onElegirServicios: (() -> Unit)? = null
) {
    val colores = MaterialTheme.colorScheme
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colores.surface),
        border = BorderStroke(1.dp, colores.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(nombre, style = MaterialTheme.typography.titleMedium)
                    Text(
                        if (servicios.size == 1) "1 servicio" else "${servicios.size} servicios",
                        style = MaterialTheme.typography.bodySmall,
                        color = colores.onSurfaceVariant
                    )
                }
                if (onEditar != null) {
                    IconButton(onClick = onEditar) {
                        Icon(Icons.Filled.Edit, contentDescription = "Editar", tint = colores.primary)
                    }
                }
                if (onEliminar != null) {
                    IconButton(onClick = onEliminar) {
                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = colores.error)
                    }
                }
            }
            if (servicios.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    servicios.forEach { s ->
                        Text(
                            s.nombre,
                            color = colores.primary,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .background(colores.primaryContainer, RoundedCornerShape(50))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
            if (onElegirServicios != null) {
                TextButton(onClick = onElegirServicios, contentPadding = PaddingValues(horizontal = 0.dp)) {
                    Text("Elegir servicios", fontWeight = FontWeight.SemiBold)
                }
            } else {
                Spacer(Modifier.height(6.dp))
                Text(
                    "Para darles un tipo, usa \"Elegir servicios\" en el tipo que quieras.",
                    style = MaterialTheme.typography.bodySmall,
                    color = colores.onSurfaceVariant
                )
            }
        }
    }
}
