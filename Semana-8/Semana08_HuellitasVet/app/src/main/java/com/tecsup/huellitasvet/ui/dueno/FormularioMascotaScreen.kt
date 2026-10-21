package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.tecsup.huellitasvet.model.Sexo
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.MascotaAvatar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.viewmodel.MascotaViewModel

/**
 * Formulario para registrar o editar una mascota (esEdicion = true cuando llega un id).
 * La pantalla solo guarda lo que se escribe; las validaciones las hace el ViewModel.
 */
@Composable
fun FormularioMascotaScreen(
    vm: MascotaViewModel,
    esEdicion: Boolean,
    onVolver: () -> Unit,
    onEliminada: () -> Unit
) {
    val mascota by vm.mascota.collectAsState()
    val errores by vm.errores.collectAsState()
    val mensaje by vm.mensaje.collectAsState()

    var nombre by rememberSaveable { mutableStateOf("") }
    var especie by rememberSaveable { mutableStateOf<Especie?>(null) }
    var raza by rememberSaveable { mutableStateOf("") }
    var anio by rememberSaveable { mutableStateOf("") }
    var peso by rememberSaveable { mutableStateOf("") }
    var sexo by rememberSaveable { mutableStateOf<Sexo?>(null) }
    var cargado by rememberSaveable { mutableStateOf(false) }

    // Al editar: llena los campos una sola vez cuando llega la mascota desde Room
    LaunchedEffect(mascota) {
        val m = mascota
        if (m != null && !cargado) {
            nombre = m.nombre
            especie = Especie.de(m.especie)
            raza = m.raza
            anio = m.anioNacimiento.toString()
            peso = m.peso.toString()
            sexo = Sexo.de(m.sexo)
            cargado = true
        }
    }

    Scaffold(
        topBar = { AppTopBar(if (esEdicion) "Editar mascota" else "Nueva mascota", onAtras = onVolver) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Avatar segun la especie elegida
            Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                MascotaAvatar(especie = especie ?: Especie.PERRO, tamano = 96.dp)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(28.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.Add, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(18.dp))
                }
            }
            Spacer(Modifier.height(16.dp))

            VetTextField(valor = nombre, onValorCambia = { nombre = it }, etiqueta = "Nombre", error = errores.nombre)
            Spacer(Modifier.height(10.dp))

            Etiqueta("Especie")
            SelectorChips(Especie.entries, especie, { it.etiqueta }, { especie = it })
            ErrorCampo(errores.especie)
            Spacer(Modifier.height(10.dp))

            VetTextField(valor = raza, onValorCambia = { raza = it }, etiqueta = "Raza")
            Spacer(Modifier.height(10.dp))

            Row {
                VetTextField(
                    valor = anio,
                    onValorCambia = { anio = it },
                    etiqueta = "Año de nacimiento",
                    error = errores.anio,
                    teclado = KeyboardType.Number,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(10.dp))
                VetTextField(
                    valor = peso,
                    onValorCambia = { peso = it },
                    etiqueta = "Peso (kg)",
                    error = errores.peso,
                    teclado = KeyboardType.Decimal,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(10.dp))

            Etiqueta("Sexo")
            SelectorChips(Sexo.entries, sexo, { it.etiqueta }, { sexo = it })
            ErrorCampo(errores.sexo)
            Spacer(Modifier.height(20.dp))

            VetButton(
                texto = "Guardar mascota",
                onClick = { vm.guardar(nombre, especie, raza, anio, peso, sexo, onListo = onVolver) }
            )
            Spacer(Modifier.height(10.dp))
            VetButton(texto = "Cancelar", onClick = onVolver, contorno = true)

            if (esEdicion) {
                Spacer(Modifier.height(10.dp))
                VetButton(
                    texto = "Eliminar mascota",
                    onClick = { mascota?.let { vm.eliminar(it, onEliminada) } },
                    contorno = true,
                    peligro = true
                )
            }
        }
    }

    // Aviso cuando no se puede eliminar (la mascota tiene citas)
    mensaje?.let {
        AlertDialog(
            onDismissRequest = { vm.limpiarMensaje() },
            confirmButton = { TextButton(onClick = { vm.limpiarMensaje() }) { Text("Entendido") } },
            title = { Text("No se puede eliminar") },
            text = { Text(it) },
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
private fun Etiqueta(texto: String) {
    Text(texto, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 8.dp))
}

@Composable
private fun ErrorCampo(error: String?) {
    if (error != null) {
        Text(error, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp, start = 4.dp))
    }
}

/** Fila de opciones (Perro/Gato/Otro o Macho/Hembra): la elegida se pinta del color del rol. */
@Composable
private fun <T> SelectorChips(
    opciones: List<T>,
    seleccionado: T?,
    etiqueta: (T) -> String,
    onElegir: (T) -> Unit
) {
    val colores = MaterialTheme.colorScheme
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        opciones.forEach { opcion ->
            val elegido = opcion == seleccionado
            Surface(
                onClick = { onElegir(opcion) },
                shape = RoundedCornerShape(12.dp),
                color = if (elegido) colores.primary else colores.surface,
                contentColor = if (elegido) colores.onPrimary else colores.onSurface,
                border = if (elegido) null else BorderStroke(1.dp, colores.outlineVariant),
                modifier = Modifier
                    .weight(1f)
                    .height(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(etiqueta(opcion), fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}