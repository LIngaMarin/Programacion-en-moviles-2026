package com.tecsup.huellitasvet.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.components.AppTopBar
import com.tecsup.huellitasvet.viewmodel.AdminViewModel

/**
 * Usuarios del administrador: chips por rol, tarjetas con telefono y distrito,
 * y eliminar con confirmacion. El boton + crea un veterinario nuevo.
 */
@Composable
fun AdminUsuariosScreen(
    vm: AdminViewModel,
    onNuevoVeterinario: () -> Unit
) {
    val usuarios by vm.usuarios.collectAsState()
    val rolFiltro by vm.rolFiltro.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    var porEliminar by remember { mutableStateOf<Usuario?>(null) }
    val colores = MaterialTheme.colorScheme

    // Chips: Todos / Dueños / Veterinarios (null = Todos)
    val filtros = listOf<Pair<String, Rol?>>(
        "Todos" to null,
        "Dueños" to Rol.CLIENTE,
        "Veterinarios" to Rol.ADMIN
    )

    Scaffold(
        topBar = { AppTopBar("Usuarios") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNuevoVeterinario,
                containerColor = colores.primary,
                contentColor = colores.onPrimary
            ) {
                Icon(Icons.Filled.PersonAdd, contentDescription = "Nuevo veterinario")
            }
        },
        containerColor = colores.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filtros) { (texto, valor) ->
                        val elegido = valor == rolFiltro
                        Surface(
                            onClick = { vm.elegirRol(valor) },
                            shape = RoundedCornerShape(50),
                            color = if (elegido) colores.primary else colores.surface,
                            contentColor = if (elegido) colores.onPrimary else colores.onSurface,
                            border = if (elegido) null else BorderStroke(1.dp, colores.outlineVariant)
                        ) {
                            Text(texto, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                        }
                    }
                }
            }

            if (usuarios.isEmpty()) {
                item {
                    Text("No hay usuarios", color = colores.onSurfaceVariant, modifier = Modifier.padding(vertical = 24.dp))
                }
            }

            items(usuarios, key = { it.id }) { u ->
                TarjetaUsuarioAdmin(u, onEliminar = { porEliminar = u })
            }
        }
    }

    // Confirmacion antes de eliminar
    porEliminar?.let { u ->
        val detalle = if (u.rol == Rol.CLIENTE.name)
            "También se eliminarán sus mascotas, con sus citas y vacunas."
        else
            "Ya no podrá ingresar a la app."
        AlertDialog(
            onDismissRequest = { porEliminar = null },
            confirmButton = {
                TextButton(onClick = {
                    vm.eliminarUsuario(u)
                    porEliminar = null
                }) { Text("Eliminar", color = colores.error) }
            },
            dismissButton = { TextButton(onClick = { porEliminar = null }) { Text("Cancelar") } },
            title = { Text("¿Eliminar a ${u.nombre}?") },
            text = { Text(detalle) },
            containerColor = colores.surface
        )
    }

    // Aviso del ViewModel cuando no se puede eliminar
    mensaje?.let {
        AlertDialog(
            onDismissRequest = { vm.limpiarMensaje() },
            confirmButton = { TextButton(onClick = { vm.limpiarMensaje() }) { Text("Entendido") } },
            title = { Text("No se puede eliminar") },
            text = { Text(it) },
            containerColor = colores.surface
        )
    }
}

@Composable
private fun TarjetaUsuarioAdmin(u: Usuario, onEliminar: () -> Unit) {
    val colores = MaterialTheme.colorScheme
    val esVeterinario = u.rol == Rol.ADMIN.name
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colores.surface),
        border = BorderStroke(1.dp, colores.outlineVariant)
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(colores.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (esVeterinario) Icons.Filled.MedicalServices else Icons.Filled.Person,
                    contentDescription = null,
                    tint = colores.primary
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(u.nombre, style = MaterialTheme.typography.titleMedium)
                Text(u.correo, style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
                Text(
                    "Tel. ${u.telefono.ifBlank { "—" }} · ${u.distrito.ifBlank { "Sin distrito" }}",
                    style = MaterialTheme.typography.bodySmall,
                    color = colores.onSurfaceVariant
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    Rol.de(u.rol)?.etiqueta ?: u.rol,
                    color = colores.primary,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .background(colores.primaryContainer, RoundedCornerShape(50))
                        .padding(horizontal = 10.dp, vertical = 2.dp)
                )
            }
            IconButton(onClick = onEliminar) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = colores.error)
            }
        }
    }
}
