package com.tecsup.huellitasvet.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

/** Una opcion de la barra inferior: ruta a la que va, texto e icono. */
data class OpcionBarra(val ruta: String, val texto: String, val icono: ImageVector)

/** ENTREGADO. 4 opciones del Dueño. Las rutas deben coincidir con Rutas.kt */
val opcionesDueno = listOf(
    OpcionBarra("inicio", "Inicio", Icons.Filled.Home),
    OpcionBarra("mascotas", "Mascotas", Icons.Filled.Pets),
    OpcionBarra("mis_citas", "Citas", Icons.Filled.DateRange),
    OpcionBarra("perfil", "Perfil", Icons.Filled.Person)
)

/** ENTREGADO. 5 opciones del Veterinario. */
val opcionesVeterinario = listOf(
    OpcionBarra("panel", "Panel", Icons.Filled.BarChart),
    OpcionBarra("agenda", "Agenda", Icons.Filled.DateRange),
    OpcionBarra("pacientes", "Pacientes", Icons.Filled.Pets),
    OpcionBarra("servicios", "Servicios", Icons.Filled.MedicalServices),
    OpcionBarra("ajustes", "Ajustes", Icons.Filled.Settings)
)

/**
 * ENTREGADO. NavigationBar de Material 3 con los colores del rol.
 * rutaActual: la ruta de la pantalla visible, para marcar la opcion seleccionada.
 */
@Composable
fun BarraInferior(
    opciones: List<OpcionBarra>,
    rutaActual: String?,
    onNavegar: (String) -> Unit
) {
    val colores = MaterialTheme.colorScheme
    NavigationBar(containerColor = colores.surface) {
        opciones.forEach { opcion ->
            NavigationBarItem(
                selected = rutaActual == opcion.ruta,
                onClick = { if (rutaActual != opcion.ruta) onNavegar(opcion.ruta) },
                icon = { Icon(opcion.icono, contentDescription = opcion.texto) },
                label = { Text(opcion.texto) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colores.primary,
                    selectedTextColor = colores.primary,
                    indicatorColor = colores.primaryContainer,
                    unselectedIconColor = colores.onSurfaceVariant,
                    unselectedTextColor = colores.onSurfaceVariant
                )
            )
        }
    }
}
