package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.TarjetaMedico
import com.saludplus.citas.ui.theme.FondoApp
import com.saludplus.citas.ui.theme.TextoGris

@Composable
fun MedicosScreen(navController: NavController, especialidadId: Int) {
    // con el id que llego por la ruta se busca la especialidad y sus medicos
    val especialidad = Repositorio.obtenerEspecialidad(especialidadId)
    // solo los medicos que atienden en el local elegido
    val medicos = Repositorio.medicosDelLocal(especialidadId)
    val local = Repositorio.localActual

    Scaffold(
        topBar = {
            BarraSuperior("Medicos de ${especialidad?.nombre ?: ""}") { navController.popBackStack() }
        },
        containerColor = FondoApp
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    "${medicos.size} medicos en ${local?.distrito ?: "todas las sedes"} · ordenados por calificacion",
                    color = TextoGris,
                    fontSize = 13.sp
                )
            }
            items(medicos) { medico ->
                // manda el id del medico elegido a la pantalla de fecha y hora
                TarjetaMedico(medico) {
                    navController.navigate(Rutas.fechaHora(medico.id))
                }
            }
        }
    }
}