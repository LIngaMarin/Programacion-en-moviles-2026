package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BotonPrimario
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.RojoError
import com.saludplus.citas.ui.theme.TextoGris

@Composable
fun RegistroScreen(navController: NavController) {
    var nombres by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Scaffold(containerColor = Color.White) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("Crear cuenta", fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Text("Registrate para agendar tus citas", color = TextoGris)
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = nombres,
                onValueChange = { nombres = it },
                label = { Text("Nombre completo") },
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null, tint = AzulPrimario) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = telefono,
                onValueChange = { telefono = it },
                label = { Text("Telefono") },
                leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = null, tint = AzulPrimario) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                label = { Text("Correo (opcional)") },
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null, tint = AzulPrimario) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = AzulPrimario) },
                // muestra puntitos en vez de la contraseña
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (error.isNotEmpty()) {
                Text(error, color = RojoError, fontSize = 14.sp)
            }

            BotonPrimario("Registrarme") {
                // se revisa en orden; el primer problema que encuentre es el mensaje que sale
                error = when {
                    nombres.isBlank() -> "Ingresa tu nombre completo"
                    telefono.length != 9 -> "El telefono debe tener 9 digitos"
                    correo.isNotBlank() && !correo.contains("@") -> "El correo no es valido"
                    contrasena.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
                    !Repositorio.registrarUsuario(nombres, telefono, correo, contrasena) -> "Ese telefono ya esta registrado"
                    else -> ""
                }
                if (error.isEmpty()) {
                    // popUpTo: al presionar Atras desde Inicio ya no se vuelve al registro
                    navController.navigate(Rutas.HOME) {
                        popUpTo(Rutas.SPLASH) { inclusive = true }
                    }
                }
            }

            Text("Al registrarte aceptas nuestros", color = TextoGris, fontSize = 13.sp)
            TextButton(onClick = { navController.navigate(Rutas.TERMINOS) }) {
                Text("Terminos y Condiciones", color = AzulPrimario, fontSize = 13.sp)
            }
            TextButton(onClick = { navController.navigate(Rutas.LOGIN) }) {
                Text("¿Ya tienes cuenta? Iniciar sesion", color = AzulPrimario)
            }
        }
    }
}