package com.tecsup.huellitasvet.ui.dueno

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.ui.theme.VetTheme
import com.tecsup.huellitasvet.viewmodel.SesionViewModel

/**
 * Registro de un dueño nuevo (tema verde del dueño).
 * La pantalla no valida nada: le pasa los datos a SesionViewModel.registrar
 * y muestra el error que este devuelva.
 */
@Composable
fun RegistroScreen(
    vm: SesionViewModel,
    oscuro: Boolean,
    onRegistrado: () -> Unit,
    onVolver: () -> Unit
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var distrito by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    var confirmar by rememberSaveable { mutableStateOf("") }
    val error by vm.error.collectAsState()

    VetTheme(rol = Rol.CLIENTE, oscuro = oscuro) {
        val colores = MaterialTheme.colorScheme
        Scaffold(containerColor = colores.background) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                Text("Crear cuenta", style = MaterialTheme.typography.headlineMedium)
                Text("Regístrate para cuidar a tus mascotas", color = colores.onSurfaceVariant)
                Spacer(Modifier.height(24.dp))

                VetTextField(
                    valor = nombre,
                    onValorCambia = { nombre = it },
                    etiqueta = "Nombre completo",
                    icono = Icons.Filled.Person
                )
                Spacer(Modifier.height(12.dp))
                VetTextField(
                    valor = correo,
                    onValorCambia = { correo = it },
                    etiqueta = "Correo",
                    icono = Icons.Filled.Email,
                    teclado = KeyboardType.Email
                )
                Spacer(Modifier.height(12.dp))
                VetTextField(
                    valor = telefono,
                    // Solo deja escribir numeros, hasta 9
                    onValorCambia = { nuevo -> telefono = nuevo.filter { it.isDigit() }.take(9) },
                    etiqueta = "Teléfono",
                    icono = Icons.Filled.Phone,
                    teclado = KeyboardType.Phone,
                    placeholder = "9XXXXXXXX"
                )
                Spacer(Modifier.height(12.dp))
                VetTextField(
                    valor = distrito,
                    onValorCambia = { distrito = it },
                    etiqueta = "Distrito",
                    icono = Icons.Filled.LocationOn
                )
                Spacer(Modifier.height(12.dp))
                VetTextField(
                    valor = clave,
                    onValorCambia = { clave = it },
                    etiqueta = "Contraseña",
                    icono = Icons.Filled.Lock,
                    esClave = true
                )
                Spacer(Modifier.height(12.dp))
                VetTextField(
                    valor = confirmar,
                    onValorCambia = { confirmar = it },
                    etiqueta = "Confirmar contraseña",
                    icono = Icons.Filled.Lock,
                    esClave = true
                )

                // Mensaje de error que viene del ViewModel
                error?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(it, color = colores.error, style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(Modifier.height(20.dp))
                VetButton(
                    texto = "Crear cuenta",
                    onClick = { vm.registrar(nombre, correo, telefono, distrito, clave, confirmar, onRegistrado) }
                )
                Spacer(Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text("¿Ya tienes cuenta? ", color = colores.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                    Text(
                        "Inicia sesión",
                        color = colores.primary,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onVolver() }
                    )
                }
            }
        }
    }
}
