package com.saludplus.citas.ui.screens.resultados

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
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
import com.saludplus.citas.data.model.Resultado
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BarraInferior
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.theme.FondoApp
import com.saludplus.citas.ui.theme.Naranja
import com.saludplus.citas.ui.theme.NaranjaClaro
import com.saludplus.citas.ui.theme.TextoGris
import com.saludplus.citas.ui.theme.VerdeClaro
import com.saludplus.citas.ui.theme.VerdeDisponible

@Composable
fun ResultadosScreen(navController: NavController) {
    // lista fija de resultados con su propio modelo (Resultado)
    val resultados = listOf(
        Resultado(1, "Hemograma completo", "28 de setiembre 2026", "Dr. Carlos Ramos", "Listo"),
        Resultado(2, "Perfil lipidico", "30 de setiembre 2026", "Dr. Miguel Paredes", "Listo"),
        Resultado(3, "Glucosa en ayunas", "2 de octubre 2026", "Dra. Lucia Vega", "En proceso"),
        Resultado(4, "Ecografia abdominal", "5 de octubre 2026", "Dra. Ana Torres", "En proceso")
    )

    Scaffold(
        topBar = { BarraSuperior("Resultados") },
        bottomBar = { BarraInferior(navController, Rutas.RESULTADOS) },
        containerColor = FondoApp
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(resultados) { resultado ->
                val listo = resultado.estado == "Listo"
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(NaranjaClaro, RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Info, contentDescription = null, tint = Naranja)
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(resultado.examen, fontWeight = FontWeight.Bold)
                            Text(resultado.medico, color = TextoGris, fontSize = 13.sp)
                            Text(resultado.fecha, color = TextoGris, fontSize = 13.sp)
                        }
                        // etiqueta verde si esta listo, naranja si esta en proceso
                        Text(
                            resultado.estado,
                            color = if (listo) VerdeDisponible else Naranja,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .background(if (listo) VerdeClaro else NaranjaClaro, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }
    }
}
