package com.lucasinga.semana05_tecsupfit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.lucasinga.semana05_tecsupfit.model.Reserva
import com.lucasinga.semana05_tecsupfit.model.clases
import com.lucasinga.semana05_tecsupfit.navigation.Screen
import com.lucasinga.semana05_tecsupfit.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleClaseScreen(navController: NavController, claseId: Int, reservas: MutableList<Reserva>) {
    // con el id que llegó por la ruta se busca la clase en la lista
    val clase = clases.first { it.id == claseId }

    // se guarda UNA sola posición (-1 = nada elegido); elegir otro horario reemplaza al anterior
    var horarioSeleccionado by remember { mutableIntStateOf(-1) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de clase", fontWeight = FontWeight.Bold) },
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .background(VerdeClaroFit, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = VerdeFit,
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(clase.nombre, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(
                "${clase.sala} · ${clase.duracion} min · ${clase.instructor}",
                fontSize = 13.sp,
                color = TextoGris
            )
            Spacer(Modifier.height(12.dp))
            Text(clase.descripcion, fontSize = 14.sp)

            Spacer(Modifier.height(20.dp))
            Text("Selecciona horario (${clase.dia})", fontSize = 13.sp, color = TextoGris)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                clase.horarios.forEachIndexed { index, horario ->
                    OpcionHorario(
                        hora = horario.hora,
                        cupos = horario.cuposDisponibles,
                        seleccionado = index == horarioSeleccionado,
                        onClick = { horarioSeleccionado = index },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
            if (horarioSeleccionado != -1) {
                val horario = clase.horarios[horarioSeleccionado]
                Text(
                    "${horario.cuposDisponibles} de ${horario.cuposTotales} cupos disponibles",
                    fontSize = 13.sp,
                    color = VerdeFit
                )
            }

            Spacer(Modifier.weight(1f))

            Button(
                onClick = {
                    val horario = clase.horarios[horarioSeleccionado]
                    // se guarda la reserva en la lista compartida (vive en AppNavigation)
                    reservas.add(
                        0,
                        Reserva(
                            clase = clase.nombre,
                            dia = clase.dia,
                            hora = horario.hora,
                            sala = clase.sala,
                            estado = "Confirmada"
                        )
                    )
                    // popUpTo(Inicio): desde la confirmación, "atrás" vuelve al Inicio y no se reserva dos veces
                    navController.navigate(
                        Screen.Confirmacion.createRoute(clase.id, horarioSeleccionado)
                    ) {
                        popUpTo(Screen.Inicio.route)
                    }
                },
                // solo se activa cuando ya se eligió un horario
                enabled = horarioSeleccionado != -1,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VerdeFit)
            ) {
                Text("Reservar cupo", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// horario seleccionable con forma de chip; verde si está elegido, gris si no
@Composable
fun OpcionHorario(
    hora: String,
    cupos: Int,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = if (seleccionado) VerdeFit else GrisTarjeta,
        contentColor = if (seleccionado) Color.White else Color.Black
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(hora, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text("$cupos cupos", fontSize = 11.sp)
        }
    }
}