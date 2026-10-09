package com.saludplus.citas.ui.screens.citas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.saludplus.citas.ui.components.BotonPrimario
import com.saludplus.citas.ui.theme.AzulClaro
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.FondoApp
import com.saludplus.citas.ui.theme.TextoGris

@Composable
fun MisCitasScreen(navController: NavController) {
    val citas = Repositorio.citasDelUsuario()

    Scaffold(
        topBar = { BarraSuperior("Mis citas") },
        bottomBar = { BarraInferior(navController, Rutas.MIS_CITAS) },
        containerColor = FondoApp
    ) { padding ->
        if (citas.isEmpty()) {
            // lista vacia: mensaje + boton para agendar la primera
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(AzulClaro, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Filled.DateRange, contentDescription = null, tint = AzulPrimario, modifier = Modifier.size(52.dp))
                }
                Spacer(Modifier.height(16.dp))
                Text("Aun no tienes citas", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Agenda tu primera cita con un especialista", color = TextoGris)
                Spacer(Modifier.height(24.dp))
                BotonPrimario("Agendar cita") { navController.navigate(Rutas.ESPECIALIDADES) }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(citas) { cita ->
                    val medico = Repositorio.obtenerMedico(cita.medicoId)
                    val especialidad = Repositorio.obtenerEspecialidad(medico?.especialidadId ?: 0)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    medico?.nombre ?: "",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    "Programada",
                                    color = AzulPrimario,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier
                                        .background(AzulClaro, RoundedCornerShape(8.dp))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                            Text(especialidad?.nombre ?: "", color = TextoGris, fontSize = 13.sp)
                            Spacer(Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.DateRange, contentDescription = null, tint = TextoGris, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("${cita.fecha} de setiembre · ${cita.hora}", fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}