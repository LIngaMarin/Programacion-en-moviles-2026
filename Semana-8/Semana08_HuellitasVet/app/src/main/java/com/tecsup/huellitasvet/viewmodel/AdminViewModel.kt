package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.data.local.relation.MascotaConDueno
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.model.validarUsuarioNuevo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Administrador: gestiona usuarios (dueños y veterinarios) y mascotas.
 * Reglas: no se elimina al ultimo veterinario; al eliminar un dueño o una mascota
 * tambien se borran sus datos (citas y vacunas) en una transaccion del Repository.
 */
class AdminViewModel(private val repo: VetRepository) : ViewModel() {

    // Filtro de la lista de usuarios por rol (null = Todos)
    private val _rolFiltro = MutableStateFlow<Rol?>(null)
    val rolFiltro: StateFlow<Rol?> = _rolFiltro.asStateFlow()

    val usuarios: StateFlow<List<Usuario>> =
        combine(repo.observarUsuariosGestionables(), _rolFiltro) { lista, rol ->
            if (rol == null) lista else lista.filter { it.rol == rol.name }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val mascotas: StateFlow<List<MascotaConDueno>> = repo.observarTodasLasMascotas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Aviso cuando algo no se puede eliminar (se muestra en un dialogo)
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    // Error del formulario de nuevo veterinario
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun elegirRol(r: Rol?) { _rolFiltro.value = r }

    /** Un dueño se elimina con sus mascotas, citas y vacunas. El ultimo veterinario no se elimina. */
    fun eliminarUsuario(u: Usuario) {
        viewModelScope.launch {
            if (u.rol == Rol.ADMIN.name && repo.contarUsuariosPorRol(Rol.ADMIN) <= 1) {
                _mensaje.value = "No se puede eliminar a ${u.nombre}: es el único veterinario de la clínica."
                return@launch
            }
            repo.eliminarUsuarioConDatos(u)
        }
    }

    /** La mascota se elimina junto con sus citas y vacunas. */
    fun eliminarMascota(m: Mascota) {
        viewModelScope.launch { repo.eliminarMascotaConDatos(m) }
    }

    /** Alta de un veterinario (rol ADMIN) con la misma validacion del registro del dueño. */
    fun crearVeterinario(
        nombre: String,
        correo: String,
        telefono: String,
        distrito: String,
        clave: String,
        confirmar: String,
        onListo: () -> Unit
    ) {
        val nombreLimpio = nombre.trim()
        val correoLimpio = correo.trim()
        val telefonoLimpio = telefono.trim()
        val distritoLimpio = distrito.trim()
        _error.value = validarUsuarioNuevo(nombreLimpio, correoLimpio, clave, confirmar, telefonoLimpio, distritoLimpio)
        if (_error.value != null) return
        viewModelScope.launch {
            if (repo.correoExiste(correoLimpio)) {
                _error.value = "Ese correo ya está registrado"
                return@launch
            }
            repo.registrarUsuario(
                Usuario(
                    nombre = nombreLimpio,
                    correo = correoLimpio,
                    clave = clave,
                    rol = Rol.ADMIN.name,
                    telefono = telefonoLimpio,
                    distrito = distritoLimpio
                )
            )
            onListo()
        }
    }

    fun limpiarMensaje() { _mensaje.value = null }

    fun limpiarError() { _error.value = null }
}
