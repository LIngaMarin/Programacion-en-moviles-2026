package com.tecsup.huellitasvet.ui.dueno

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.components.VetTextField
import com.tecsup.huellitasvet.ui.theme.VetTheme
import com.tecsup.huellitasvet.viewmodel.SesionViewModel

/**
 * Un solo Composable para ambos roles: el selector Dueño / Veterinario
 * cambia el tema (verde o azul), asi el boton "Ingresar" toma el color del rol.
 * La pantalla no valida nada: solo le avisa al ViewModel.
 */
@Composable
fun LoginScreen(
    vm: SesionViewModel,
    oscuro: Boolean,
    onIngresar: (Rol) -> Unit
) {
    var rol by rememberSaveable { mutableStateOf(Rol.CLIENTE) }
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    val error by vm.error.collectAsState()

    VetTheme(rol = rol, oscuro = oscuro) {
        val colores = MaterialTheme.colorScheme
        Scaffold(containerColor = colores.background) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                Text("Bienvenido", style = MaterialTheme.typography.headlineMedium)
                Text("Ingresa para continuar", color = colores.onSurfaceVariant)
                Spacer(Modifier.height(24.dp))

                SelectorRol(rol = rol, onCambiar = { nuevo ->
                    rol = nuevo
                    vm.limpiarError()
                })
                Spacer(Modifier.height(20.dp))

                VetTextField(
                    valor = correo,
                    onValorCambia = { correo = it },
                    etiqueta = "Correo",
                    icono = Icons.Filled.Email,
                    teclado = KeyboardType.Email
                )
                Spacer(Modifier.height(12.dp))
                VetTextField(
                    valor = clave,
                    onValorCambia = { clave = it },
                    etiqueta = "Contraseña",
                    icono = Icons.Filled.Lock,
                    esClave = true
                )
                Text(
                    "¿Olvidaste tu contraseña?",
                    color = colores.primary,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 8.dp)
                )

                // Mensaje de error que viene del ViewModel
                error?.let {
                    Spacer(Modifier.height(8.dp))
                    Text(it, color = colores.error, style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(Modifier.height(20.dp))
                VetButton(texto = "Ingresar", onClick = { vm.login(correo, clave, rol, onIngresar) })
                Spacer(Modifier.height(16.dp))

                if (rol == Rol.CLIENTE) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Text("¿No tienes cuenta? ", color = colores.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
                        Text("Regístrate", color = colores.primary, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                    }
                } else {
                    // Aviso del mockup para el veterinario
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(colores.primaryContainer, RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Text("Modo práctica", color = colores.primary, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        Text(
                            "Puedes cambiar de perfil desde Ajustes para probar ambos roles.",
                            color = colores.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

/** Selector de dos pestañas: Dueño / Veterinario. */
@Composable
private fun SelectorRol(rol: Rol, onCambiar: (Rol) -> Unit) {
    val colores = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colores.surfaceContainerHighest, RoundedCornerShape(12.dp))
            .padding(4.dp)
    ) {
        Rol.entries.forEach { opcion ->
            val elegido = opcion == rol
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (elegido) colores.primary else Color.Transparent)
                    .clickable { onCambiar(opcion) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    opcion.etiqueta,
                    color = if (elegido) colores.onPrimary else colores.onSurfaceVariant,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}