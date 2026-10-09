package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.theme.AzulClaro
import com.saludplus.citas.ui.theme.FondoApp
import com.saludplus.citas.ui.theme.TextoGris

@Composable
fun EspecialidadesScreen(navController: NavController) {
    var busqueda by remember { mutableStateOf("") }
    // se vuelve a calcular cada vez que cambia "busqueda": la lista se actualiza sola
    val resultado = Repositorio.buscarEspecialidades(busqueda)

    Scaffold(
        topBar = { BarraSuperior("Especialidades") { navController.popBackStack() } },
        containerColor = FondoApp
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                placeholder = { Text("Buscar especialidad...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            if (resultado.isEmpty()) {
                Text(
                    "No se encontraron especialidades",
                    color = TextoGris,
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(resultado) { especialidad ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                // manda el id de la especialidad elegida a la pantalla de medicos
                                .clickable { navController.navigate(Rutas.medicos(especialidad.id)) },
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .background(AzulClaro, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(especialidad.icono, fontSize = 22.sp)
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(especialidad.nombre, fontWeight = FontWeight.Bold)
                                    Text(especialidad.descripcion, fontSize = 13.sp, color = TextoGris)
                                }
                                Icon(
                                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = TextoGris
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}