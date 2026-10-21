package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.data.prefs.PreferenciasStore
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Sesion de la app: quien inicio sesion, con que perfil, y el modo oscuro.
 * Las validaciones del login viven aqui (no en la pantalla).
 */
class SesionViewModel(
    private val repo: VetRepository,
    private val prefs: PreferenciasStore
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val modoOscuro: StateFlow<Boolean> = prefs.modoOscuro
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    // Perfil guardado en DataStore: null = todavia cargando, "" = sin sesion, CLIENTE o ADMIN
    val perfilGuardado: StateFlow<String?> = prefs.perfilActivo
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        // Si habia una sesion guardada, recupera al usuario por su id
        viewModelScope.launch {
            val id = prefs.usuarioId.first()
            if (id != 0) _usuarioActual.value = repo.obtenerUsuario(id)
        }
    }

    /**
     * Valida e inicia sesion. Si todo esta bien guarda el perfil en DataStore
     * y llama a onExito para que la navegacion vaya a Inicio o a Panel.
     */
    fun login(correo: String, clave: String, rolSeleccionado: Rol, onExito: (Rol) -> Unit) {
        val correoLimpio = correo.trim()
        if (correoLimpio.isBlank() || clave.isBlank()) {
            _error.value = "Ingresa tu correo y tu contraseña"
            return
        }
        viewModelScope.launch {
            val usuario = repo.login(correoLimpio, clave)
            when {
                usuario == null ->
                    _error.value = "Correo o contraseña incorrectos"
                usuario.rol != rolSeleccionado.name ->
                    _error.value = if (rolSeleccionado == Rol.CLIENTE)
                        "Esta cuenta es de veterinario: elige la pestaña Veterinario"
                    else
                        "Esta cuenta es de dueño: elige la pestaña Dueño"
                else -> {
                    _error.value = null
                    _usuarioActual.value = usuario
                    prefs.guardarSesion(rolSeleccionado, usuario.id)
                    onExito(rolSeleccionado)
                }
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }
}