package com.tecsup.huellitasvet.ui.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.viewmodel.AdminViewModel

/** Alta de un veterinario. La pantalla no valida: le pasa los datos a AdminViewModel.crearVeterinario. */
@Composable
fun FormularioVeterinarioScreen(
    vm: AdminViewModel,
    onVolver: () -> Unit
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var distrito by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    var confirmar by rememberSaveable { mutableStateOf("") }
    val error by vm.error.collectAsState()
    val colores = MaterialTheme.colorScheme

    val volver = {
        vm.limpiarError()
        onVolver()
    }

    Scaffold(
        topBar = { AppTopBar("Nuevo veterinario", onAtras = volver) },
        containerColor = colores.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            VetTextField(valor = nombre, onValorCambia = { nombre = it }, etiqueta = "Nombre completo", icono = Icons.Filled.Person)
            Spacer(Modifier.height(12.dp))
            VetTextField(valor = correo, onValorCambia = { correo = it }, etiqueta = "Correo", icono = Icons.Filled.Email, teclado = KeyboardType.Email)
            Spacer(Modifier.height(12.dp))
            VetTextField(
                valor = telefono,
                onValorCambia = { nuevo -> telefono = nuevo.filter { it.isDigit() }.take(9) },
                etiqueta = "Teléfono",
                icono = Icons.Filled.Phone,
                teclado = KeyboardType.Phone,
                placeholder = "9XXXXXXXX"
            )
            Spacer(Modifier.height(12.dp))
            VetTextField(valor = distrito, onValorCambia = { distrito = it }, etiqueta = "Distrito", icono = Icons.Filled.LocationOn)
            Spacer(Modifier.height(12.dp))
            VetTextField(valor = clave, onValorCambia = { clave = it }, etiqueta = "Contraseña", icono = Icons.Filled.Lock, esClave = true)
            Spacer(Modifier.height(12.dp))
            VetTextField(valor = confirmar, onValorCambia = { confirmar = it }, etiqueta = "Confirmar contraseña", icono = Icons.Filled.Lock, esClave = true)

            // Mensaje de error que viene del ViewModel
            error?.let {
                Spacer(Modifier.height(8.dp))
                Text(it, color = colores.error, style = MaterialTheme.typography.bodyMedium)
            }

            Spacer(Modifier.height(20.dp))
            VetButton(
                texto = "Guardar veterinario",
                onClick = { vm.crearVeterinario(nombre, correo, telefono, distrito, clave, confirmar, onListo = onVolver) }
            )
            Spacer(Modifier.height(10.dp))
            VetButton(texto = "Cancelar", onClick = volver, contorno = true)
            Spacer(Modifier.height(16.dp))
        }
    }
}
