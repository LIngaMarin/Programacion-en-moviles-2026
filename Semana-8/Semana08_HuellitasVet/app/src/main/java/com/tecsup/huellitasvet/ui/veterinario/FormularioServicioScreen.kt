package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.ui.components.iconoDeServicio
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel

/** Formulario para crear (servicioId = null) o editar un servicio. */
@Composable
fun FormularioServicioScreen(
    vm: ServiciosViewModel,
    servicioId: Int?,
    onVolver: () -> Unit
) {
    val servicios by vm.servicios.collectAsState()
    val errores by vm.errores.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val servicio = servicios.find { it.id == servicioId }

    var nombre by rememberSaveable { mutableStateOf("") }
    var duracion by rememberSaveable { mutableStateOf<Int?>(null) }
    var precio by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var icono by rememberSaveable { mutableStateOf("heart") }
    var activo by rememberSaveable { mutableStateOf(true) }
    var cargado by rememberSaveable { mutableStateOf(false) }

    // Al editar: llena los campos una sola vez
    LaunchedEffect(servicio) {
        val s = servicio
        if (s != null && !cargado) {
            nombre = s.nombre
            duracion = s.duracionMin
            precio = s.precio.toString()
            descripcion = s.descripcion
            icono = s.icono
            activo = s.activo
            cargado = true
        }
    }

    val colores = MaterialTheme.colorScheme

    Scaffold(
        topBar = { AppTopBar(if (servicioId == null) "Nuevo servicio" else "Editar servicio", onAtras = onVolver) },
        containerColor = colores.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            VetTextField(valor = nombre, onValorCambia = { nombre = it }, etiqueta = "Nombre del servicio", error = errores.nombre)
            Spacer(Modifier.height(12.dp))

            Text("Duración", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                vm.duraciones.forEach { min ->
                    val elegido = min == duracion
                    Surface(
                        onClick = { duracion = min },
                        shape = RoundedCornerShape(12.dp),
                        color = if (elegido) colores.primary else colores.surface,
                        contentColor = if (elegido) colores.onPrimary else colores.onSurface,
                        border = if (elegido) null else BorderStroke(1.dp, colores.outlineVariant),
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) { Text("$min min", fontWeight = FontWeight.SemiBold) }
                    }
                }
            }
            errores.duracion?.let { Text(it, color = colores.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp)) }
            Spacer(Modifier.height(12.dp))

            VetTextField(valor = precio, onValorCambia = { precio = it }, etiqueta = "Precio (S/)", error = errores.precio, teclado = KeyboardType.Decimal)
            Spacer(Modifier.height(12.dp))
            VetTextField(valor = descripcion, onValorCambia = { descripcion = it }, etiqueta = "Descripción", lineas = 2)
            Spacer(Modifier.height(12.dp))

            Text("Ícono", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                listOf("heart", "syringe", "scissors").forEach { opcion ->
                    val elegido = opcion == icono
                    Surface(
                        onClick = { icono = opcion },
                        shape = RoundedCornerShape(12.dp),
                        color = if (elegido) colores.primaryContainer else colores.surface,
                        border = BorderStroke(if (elegido) 1.5.dp else 1.dp, if (elegido) colores.primary else colores.outlineVariant),
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(iconoDeServicio(opcion), contentDescription = opcion, tint = colores.primary)
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Servicio activo", fontWeight = FontWeight.SemiBold)
                    Text("Los inactivos no se pueden pedir", style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
                }
                Switch(checked = activo, onCheckedChange = { activo = it })
            }
            Spacer(Modifier.height(20.dp))

            VetButton(
                texto = "Guardar servicio",
                onClick = { vm.guardar(servicioId ?: 0, nombre, duracion, precio, descripcion, icono, activo, onListo = onVolver) }
            )
            Spacer(Modifier.height(10.dp))
            VetButton(texto = "Cancelar", onClick = onVolver, contorno = true)

            if (servicio != null) {
                Spacer(Modifier.height(10.dp))
                VetButton(
                    texto = "Eliminar servicio",
                    onClick = { vm.eliminar(servicio, onEliminado = onVolver) },
                    contorno = true,
                    peligro = true
                )
            }
            Spacer(Modifier.height(16.dp))
        }
    }

    mensaje?.let {
        AlertDialog(
            onDismissRequest = { vm.limpiarMensaje() },
            confirmButton = { TextButton(onClick = { vm.limpiarMensaje() }) { Text("Entendido") } },
            title = { Text("No se puede eliminar") },
            text = { Text(it) },
            containerColor = colores.surface
        )
    }
}