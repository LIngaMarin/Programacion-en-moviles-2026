package com.saludplus.citas.ui.screens.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraInferior
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.FilaDato
import com.saludplus.citas.ui.theme.AzulClaro
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.FondoApp
import com.saludplus.citas.ui.theme.RojoError
import com.saludplus.citas.ui.theme.TextoGris

@Composable
fun PerfilScreen(navController: NavController) {
    // datos de la sesion iniciada
    val usuario = Repositorio.usuarioActual
    val totalCitas = Repositorio.citasDelUsuario().size

    Scaffold(
        topBar = { BarraSuperior("Mis datos") },
        bottomBar = { BarraInferior(navController, Rutas.PERFIL) },
        containerColor = FondoApp
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(AzulClaro, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    usuario?.nombres?.take(1) ?: "",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulPrimario
                )
            }
            Text(usuario?.nombres ?: "", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text("Paciente", color = TextoGris)

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    FilaDato(Icons.Filled.Phone, "Telefono", usuario?.telefono ?: "")
                    // el correo es opcional en el registro
                    FilaDato(
                        Icons.Filled.Email,
                        "Correo",
                        if (usuario?.correo.isNullOrBlank()) "No registrado" else usuario?.correo ?: ""
                    )
                    FilaDato(Icons.Filled.DateRange, "Citas agendadas", "$totalCitas")
                }
            }

            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    Repositorio.cerrarSesion()
                    // vuelve al Splash y borra todo el historial: con Atras ya no se regresa al perfil
                    navController.navigate(Rutas.SPLASH) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RojoError)
            ) {
                Text("Cerrar sesion", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}