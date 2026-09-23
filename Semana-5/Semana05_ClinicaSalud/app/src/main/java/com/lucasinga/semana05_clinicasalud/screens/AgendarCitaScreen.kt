package com.lucasinga.semana05_clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lucasinga.semana05_clinicasalud.model.Cita
import com.lucasinga.semana05_clinicasalud.model.fechas
import com.lucasinga.semana05_clinicasalud.model.horas
import com.lucasinga.semana05_clinicasalud.model.medicos
import com.lucasinga.semana05_clinicasalud.navigation.Screen
import com.lucasinga.semana05_clinicasalud.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendarCitaScreen(navController: NavController, medicoId: Int, citas: MutableList<Cita>) {
    val medico = medicos.first { it.id == medicoId }

    // se guarda UNA sola posición por grupo (-1 = nada elegido todavía)
    // por eso funciona como RadioButton: elegir otra opción reemplaza a la anterior
    var fechaSeleccionada by remember { mutableIntStateOf(-1) }
    var horaSeleccionada by remember { mutableIntStateOf(-1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar cita", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text("Cita con ${medico.nombre}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.height(20.dp))

            Text("Selecciona fecha", fontSize = 13.sp, color = TextoGris)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                fechas.forEachIndexed { index, fecha ->
                    OpcionSeleccion(
                        seleccionado = index == fechaSeleccionada,
                        onClick = { fechaSeleccionada = index },
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(fecha.dia, fontSize = 12.sp)
                            Text(fecha.numero, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Text("Selecciona hora", fontSize = 13.sp, color = TextoGris)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                horas.forEachIndexed { index, hora ->
                    OpcionSeleccion(
                        seleccionado = index == horaSeleccionada,
                        onClick = { horaSeleccionada = index },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(hora, fontSize = 14.sp)
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    // se guarda la cita en la lista compartida (vive en AppNavigation)
                    citas.add(
                        0,
                        Cita(
                            medico = medico.nombre,
                            especialidad = medico.especialidad,
                            fecha = fechas[fechaSeleccionada].textoCompleto,
                            hora = horas[horaSeleccionada],
                            estado = "Confirmada"
                        )
                    )
                    // popUpTo(Inicio): al volver desde la confirmación se regresa al Inicio,
                    // así no se puede volver a esta pantalla y agendar la misma cita dos veces
                    navController.navigate(
                        Screen.Confirmacion.createRoute(medico.id, fechaSeleccionada, horaSeleccionada)
                    ) {
                        popUpTo(Screen.Inicio.route)
                    }
                },
                // solo se activa cuando ya se eligió fecha y hora
                enabled = fechaSeleccionada != -1 && horaSeleccionada != -1,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MoradoClinica)
            ) {
                Text("Confirmar cita", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// opción seleccionable con forma de chip; morada si está elegida, gris si no
@Composable
fun OpcionSeleccion(
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contenido: @Composable () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = if (seleccionado) MoradoClinica else GrisTarjeta,
        contentColor = if (seleccionado) Color.White else Color.Black
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            contenido()
        }
    }
}