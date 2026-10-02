package com.lucasinga.semana06_tecsupstore.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lucasinga.semana06_tecsupstore.model.correoUsuario
import com.lucasinga.semana06_tecsupstore.model.nombreUsuario
import com.lucasinga.semana06_tecsupstore.navigation.Screen
import com.lucasinga.semana06_tecsupstore.ui.theme.LilaClaro
import com.lucasinga.semana06_tecsupstore.ui.theme.MoradoStore
import com.lucasinga.semana06_tecsupstore.ui.theme.TextoGris

@Composable
fun AppDrawer(
    rutaActual: String?,
    totalFavoritos: Int,
    onNavegar: (String) -> Unit,
    onCerrarMenu: () -> Unit
) {
    // ModalDrawerSheet: la hoja blanca que sale desde la izquierda
    ModalDrawerSheet {
        // encabezado: iniciales + datos del usuario
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(LilaClaro, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("LI", fontWeight = FontWeight.Bold, color = MoradoStore)
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(nombreUsuario, fontWeight = FontWeight.Bold)
                Text(correoUsuario, fontSize = 12.sp, color = TextoGris)
            }
        }
        HorizontalDivider()
        Spacer(Modifier.height(8.dp))

        // selected es true solo en el item de la pantalla actual
        ItemMenu("Inicio", Icons.Filled.Home, rutaActual == Screen.Inicio.route) {
            onNavegar(Screen.Inicio.route)
        }
        ItemMenu("Mis pedidos", Icons.Filled.ShoppingCart, rutaActual == Screen.MisPedidos.route) {
            onNavegar(Screen.MisPedidos.route)
        }
        // solo Favoritos lleva contador
        ItemMenu("Favoritos", Icons.Filled.Favorite, rutaActual == Screen.Favoritos.route, contador = totalFavoritos) {
            onNavegar(Screen.Favoritos.route)
        }
        ItemMenu("Perfil", Icons.Filled.Person, rutaActual == Screen.Perfil.route) {
            onNavegar(Screen.Perfil.route)
        }
        ItemMenu("Cerrar sesion", Icons.AutoMirrored.Filled.ExitToApp, false) {
            onCerrarMenu()
        }
    }
}

@Composable
fun ItemMenu(
    texto: String,
    icono: ImageVector,
    seleccionado: Boolean,
    contador: Int = 0,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text(texto) },
        icon = { Icon(icono, contentDescription = null) },
        selected = seleccionado,
        onClick = onClick,
        // badge: solo aparece si hay al menos 1
        badge = if (contador > 0) {
            { Badge(containerColor = MoradoStore, contentColor = Color.White) { Text("$contador") } }
        } else null,
        modifier = Modifier.padding(horizontal = 12.dp),
        // fondo lila y texto morado solo para el item activo
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = LilaClaro,
            selectedTextColor = MoradoStore,
            selectedIconColor = MoradoStore
        )
    )
}