package com.lucasinga.semana06_tecsupstore.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppDrawer(onCerrarMenu: () -> Unit) {
    // ModalDrawerSheet: la hoja blanca que sale desde la izquierda
    ModalDrawerSheet {
        Spacer(Modifier.height(16.dp))
        NavigationDrawerItem(
            label = { Text("Inicio") },
            icon = { Icon(Icons.Filled.Home, contentDescription = null) },
            selected = false,
            onClick = onCerrarMenu,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        NavigationDrawerItem(
            label = { Text("Mis pedidos") },
            icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = null) },
            selected = false,
            onClick = onCerrarMenu,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        NavigationDrawerItem(
            label = { Text("Favoritos") },
            icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
            selected = false,
            onClick = onCerrarMenu,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        NavigationDrawerItem(
            label = { Text("Perfil") },
            icon = { Icon(Icons.Filled.Person, contentDescription = null) },
            selected = false,
            onClick = onCerrarMenu,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        NavigationDrawerItem(
            label = { Text("Cerrar sesion") },
            icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null) },
            selected = false,
            onClick = onCerrarMenu,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
    }
}