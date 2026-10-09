package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrimario
import com.saludplus.citas.ui.components.TarjetaMedico
import com.saludplus.citas.ui.theme.AzulClaro
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.FondoApp
import com.saludplus.citas.ui.theme.TextoGris
import com.saludplus.citas.util.diaCorto
import com.saludplus.citas.util.diasHabiles
import com.saludplus.citas.util.textoMes

@Composable
fun FechaHoraScreen(navController: NavController, medicoId: Int) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val dias = diasHabiles(0)
    var diaSeleccionado by remember { mutableStateOf("") }
    var horaSeleccionada by remember { mutableStateOf("") }

    // se recalcula sola cada vez que cambia el dia elegido
    val horarios = if (diaSeleccionado.isEmpty()) {
        emptyList()
    } else {
        Repositorio.horariosDisponibles(medicoId, diaSeleccionado)
    }

    Scaffold(
        topBar = { BarraSuperior("Seleccionar fecha y hora") { navController.popBackStack() } },
        containerColor = FondoApp
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (medico != null) {
                TarjetaMedico(medico)
            }

            Text(
                textoMes(dias.first()),
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // fila de dias (lista fija en la Fase 1)
            // fila con los 5 dias habiles reales
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                dias.forEach { fecha ->
                    // la fecha se guarda como "2026-10-12" para compararla con las citas
                    val valor = fecha.toString()
                    val elegido = valor == diaSeleccionado
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (elegido) AzulPrimario else Color.White)
                            .clickable {
                                diaSeleccionado = valor
                                // al cambiar de dia, la hora elegida se reinicia
                                horaSeleccionada = ""
                            }
                            .padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(diaCorto(fecha), fontSize = 12.sp, color = if (elegido) Color.White else TextoGris)
                        Text(
                            "${fecha.dayOfMonth}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (elegido) Color.White else Color.Black
                        )
                    }
                }
            }

            Text("Horarios disponibles", fontWeight = FontWeight.Bold)
            if (diaSeleccionado.isEmpty()) {
                Text("Elige un dia para ver los horarios", color = TextoGris)
            } else if (horarios.isEmpty()) {
                Text("No hay horarios disponibles este dia", color = TextoGris)
            }

            // cuadricula de 3 columnas con las horas libres
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(horarios) { hora ->
                    val elegida = hora == horaSeleccionada
                    Text(
                        hora,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        color = if (elegida) Color.White else AzulPrimario,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (elegida) AzulPrimario else AzulClaro)
                            .clickable { horaSeleccionada = hora }
                            .padding(vertical = 14.dp)
                    )
                }
            }

            // solo se habilita cuando hay dia y hora elegidos
            BotonPrimario(
                "Continuar",
                habilitado = diaSeleccionado.isNotEmpty() && horaSeleccionada.isNotEmpty()
            ) {
                navController.navigate(Rutas.confirmar(medicoId, diaSeleccionado, horaSeleccionada))
            }
        }
    }
}