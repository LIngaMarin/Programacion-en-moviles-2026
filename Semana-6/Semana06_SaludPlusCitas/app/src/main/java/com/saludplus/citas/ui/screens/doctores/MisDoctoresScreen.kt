package com.saludplus.citas.ui.screens.doctores

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.TarjetaMedico
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.FondoApp
import com.saludplus.citas.ui.theme.TextoGris

@Composable
fun MisDoctoresScreen(navController: NavController) {
    Scaffold(
        topBar = { BarraSuperior("Mis doctores") { navController.popBackStack() } },
        containerColor = FondoApp
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // por cada especialidad (categoria): un titulo y debajo sus medicos
            Repositorio.especialidades.forEach { especialidad ->
                val medicos = Repositorio.medicosPorEspecialidad(especialidad.id)
                item {
                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "${especialidad.icono} ${especialidad.nombre}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = AzulPrimario,
                            modifier = Modifier.weight(1f)
                        )
                        Text("${medicos.size} medicos", color = TextoGris, fontSize = 13.sp)
                    }
                }
                items(medicos) { medico ->
                    TarjetaMedico(medico, sedes = Repositorio.sedesDelMedico(medico))
                }
            }
        }
    }
}
