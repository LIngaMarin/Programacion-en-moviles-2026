package com.lucasinga.semana05_navegacion.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.lucasinga.semana05_navegacion.components.BarraSuperior
import com.lucasinga.semana05_navegacion.components.TarjetaEstudiante
import com.lucasinga.semana05_navegacion.model.estudiantes
import com.lucasinga.semana05_navegacion.navigation.Screen

@Composable
fun ListScreen(navController: NavController) {
    Scaffold(
        topBar = {
            BarraSuperior(
                titulo = "Directorio de Alumnos",
                colorFondo = Color(0xFFEADDFF),
                colorTitulo = Color(0xFF21005D),
                onBack = { navController.popBackStack() }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = padding.calculateTopPadding() + 16.dp,
                bottom = padding.calculateBottomPadding() + 16.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(estudiantes.size) { index ->
                val alumno = estudiantes[index]
                TarjetaEstudiante(
                    estudiante = alumno,
                    onClick = {
                        navController.navigate(
                            Screen.Detail.createRoute(alumno.id)
                        )
                    }
                )
            }
        }
    }
}
