package com.tecsup.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.huellitasvet.ui.components.fechaCorta
import com.tecsup.huellitasvet.ui.components.graficos.GraficoBarras
import com.tecsup.huellitasvet.ui.components.graficos.GraficoDona
import com.tecsup.huellitasvet.ui.components.soles
import com.tecsup.huellitasvet.ui.theme.EstadoAtendida
import com.tecsup.huellitasvet.ui.theme.EstadoPendiente
import com.tecsup.huellitasvet.viewmodel.PanelViewModel
import java.time.LocalDate

/** Panel de la clinica: 3 contadores, ingresos del mes, graficos y alerta de citas por confirmar. */
@Composable
fun PanelScreen(
    vm: PanelViewModel,
    onVerPendientes: () -> Unit
) {
    val citasHoy by vm.citasHoy.collectAsState()
    val pendientes by vm.pendientes.collectAsState()
    val atendidas by vm.atendidas.collectAsState()
    val ingresos by vm.ingresosMes.collectAsState()
    val porEstado by vm.porEstado.collectAsState()
    val porServicio by vm.porServicio.collectAsState()
    val colores = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Panel de la clínica", style = MaterialTheme.typography.headlineMedium)
                Text("Hoy · ${fechaCorta(LocalDate.now().toString())}", color = colores.onSurfaceVariant)
            }
            Icon(Icons.Filled.Notifications, contentDescription = null, tint = colores.onSurface)
        }
        Spacer(Modifier.height(16.dp))

        // 3 contadores de color
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Contador(citasHoy, "Citas hoy", colores.primary, Modifier.weight(1f))
            Contador(pendientes, "Pendientes", EstadoPendiente, Modifier.weight(1f))
            Contador(atendidas, "Atendidas", EstadoAtendida, Modifier.weight(1f))
        }
        Spacer(Modifier.height(14.dp))

        // Ingresos del mes
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colores.primary, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Text("Ingresos del mes", color = colores.onPrimary.copy(alpha = 0.85f), style = MaterialTheme.typography.bodySmall)
            Row(verticalAlignment = Alignment.Bottom) {
                Text(soles(ingresos), color = colores.onPrimary, fontSize = 28.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text("$atendidas atenciones", color = colores.onPrimary.copy(alpha = 0.85f), style = MaterialTheme.typography.bodySmall)
            }
        }
        Spacer(Modifier.height(14.dp))

        Seccion("Citas por estado") { GraficoDona(datos = porEstado) }
        Spacer(Modifier.height(14.dp))
        Seccion("Citas por servicio") { GraficoBarras(datos = porServicio) }

        // Alerta si hay citas por confirmar
        if (pendientes > 0) {
            Spacer(Modifier.height(14.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(EstadoPendiente.copy(alpha = 0.12f), RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Icon(Icons.Filled.Warning, contentDescription = null, tint = EstadoPendiente)
                Spacer(Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("$pendientes citas por confirmar", fontWeight = FontWeight.SemiBold)
                    Text("Revísalas en la agenda", style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
                }
                Button(
                    onClick = onVerPendientes,
                    colors = ButtonDefaults.buttonColors(containerColor = EstadoPendiente, contentColor = Color.White),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Ver")
                }
            }
        }
    }
}

/** Contador de color: numero grande y texto. */
@Composable
private fun Contador(valor: Int, texto: String, color: Color, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(14.dp))
            .padding(vertical = 14.dp)
    ) {
        Text(valor.toString(), color = color, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text(texto, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

/** Tarjeta blanca con titulo para cada grafico. */
@Composable
private fun Seccion(titulo: String, contenido: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))
            contenido()
        }
    }
}