package com.lucasinga.semana05_navegacion.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lucasinga.semana05_navegacion.components.*
import com.lucasinga.semana05_navegacion.model.estudiantes
import com.lucasinga.semana05_navegacion.navigation.Screen

@Composable
fun ProfileScreen(navController: NavController) {
    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = "Configuración de Perfil",
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF6750A4),
                                Color(0xFF7D5260)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FotoPerfil(
                        url = estudiantes.first().fotoUrl,
                        tamano = 96.dp,
                        bordeAncho = 3.dp,
                        bordeColor = Color.White,
                        contentDescription = "Foto de perfil"
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Lucas Inga Marín",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Spacer(modifier = Modifier.height(20.dp))
                TituloSeccion(texto = "INFORMACIÓN PERSONAL")
                Spacer(modifier = Modifier.height(12.dp))

                FilaInformacion(
                    icono = Icons.Filled.Person,
                    etiqueta = "Nombre Completo",
                    valor = "Lucas Inga Marín",
                    conFondoIcono = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                FilaInformacion(
                    icono = Icons.Filled.Email,
                    etiqueta = "Correo",
                    valor = "lucas.inga@tecsup.edu.pe",
                    conFondoIcono = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                FilaInformacion(
                    icono = Icons.Filled.Phone,
                    etiqueta = "Teléfono",
                    valor = "+51 987 654 321",
                    conFondoIcono = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                TituloSeccion(texto = "ACADÉMICO")
                Spacer(modifier = Modifier.height(12.dp))

                FilaInformacion(
                    icono = Icons.Filled.School,
                    etiqueta = "Carrera",
                    valor = "Diseño y Desarrollo de Software",
                    conFondoIcono = true
                )

                Spacer(modifier = Modifier.height(14.dp))

                FilaInformacion(
                    icono = Icons.Filled.CalendarMonth,
                    etiqueta = "Ciclo Actual",
                    valor = "VI Ciclo",
                    conFondoIcono = true
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            BotonCerrarSesion(
                onClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }
    }
}
