package com.tecsup.huellitasvet.ui.admin

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.ui.components.VetButton
import com.tecsup.huellitasvet.ui.dueno.FilaPreferencia
import com.tecsup.huellitasvet.ui.dueno.TarjetaPreferencias
import com.tecsup.huellitasvet.ui.dueno.TarjetaUsuario

/** Cuenta del administrador: sus datos, modo oscuro y cerrar sesion (reusa las tarjetas de Perfil). */
@Composable
fun AdminCuentaScreen(
    nombre: String,
    correo: String,
    modoOscuro: Boolean,
    onModoOscuro: (Boolean) -> Unit,
    onCerrarSesion: () -> Unit
) {
    val colores = MaterialTheme.colorScheme
    Scaffold(
        topBar = { AppTopBar("Cuenta") },
        containerColor = colores.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            TarjetaUsuario(nombre, correo, etiquetaRol = "Administrador")
            Spacer(Modifier.height(18.dp))

            Text("Preferencias", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            TarjetaPreferencias {
                FilaPreferencia("Modo oscuro", "Cambia el tema de la app", modoOscuro, onModoOscuro)
            }
            Spacer(Modifier.height(18.dp))

            VetButton(texto = "Cerrar sesión", onClick = onCerrarSesion, contorno = true, peligro = true)
            Spacer(Modifier.height(16.dp))
        }
    }
}
