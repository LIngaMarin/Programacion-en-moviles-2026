package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Usuario
import com.tecsup.huellitasvet.data.prefs.PreferenciasStore
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.model.validarUsuarioNuevo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Sesion de la app: quien inicio sesion, con que perfil, y las preferencias guardadas en DataStore.
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
    // Mensaje de exito para el login (por ejemplo, despues de registrarse)
    private val _aviso = MutableStateFlow<String?>(null)
    val aviso: StateFlow<String?> = _aviso.asStateFlow()

    // ---------- Preferencias (DataStore) ----------
    val modoOscuro: StateFlow<Boolean> = prefs.modoOscuro
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val notificaciones: StateFlow<Boolean> = prefs.notificaciones
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val aceptarCitas: StateFlow<Boolean> = prefs.aceptarCitas
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

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
                usuario.rol != rolSeleccionado.name -> {
                    // Con 3 perfiles, el mensaje dice cual es la pestaña correcta
                    val suRol = Rol.de(usuario.rol)?.etiqueta ?: "otro perfil"
                    _error.value = "Esta cuenta es de ${suRol.lowercase()}: elige la pestaña $suRol"
                }
                else -> {
                    _error.value = null
                    _usuarioActual.value = usuario
                    prefs.guardarSesion(rolSeleccionado, usuario.id)
                    onExito(rolSeleccionado)
                }
            }
        }
    }

    /**
     * Registra a un dueño nuevo (siempre rol CLIENTE: el veterinario no se registra solo).
     * Valida aqui; si todo esta bien lo guarda en Room, inicia su sesion en DataStore
     * y llama a onExito para que la navegacion vaya a su Inicio.
     */
    fun registrar(
        nombre: String,
        correo: String,
        telefono: String,
        distrito: String,
        clave: String,
        confirmar: String,
        onExito: () -> Unit
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
            val nuevo = Usuario(
                nombre = nombreLimpio,
                correo = correoLimpio,
                clave = clave,
                rol = Rol.CLIENTE.name,
                telefono = telefonoLimpio,
                distrito = distritoLimpio
            )
            repo.registrarUsuario(nuevo)
            _aviso.value = "Cuenta creada. Inicia sesión con tu correo y contraseña"
            onExito()
        }
    }

    fun limpiarError() {
        _error.value = null
        _aviso.value = null
    }

    // ---------- Ajustes ----------
    fun cambiarModoOscuro(activo: Boolean) {
        viewModelScope.launch { prefs.guardarModoOscuro(activo) }
    }

    fun cambiarNotificaciones(activo: Boolean) {
        viewModelScope.launch { prefs.guardarNotificaciones(activo) }
    }

    fun cambiarAceptarCitas(activo: Boolean) {
        viewModelScope.launch { prefs.guardarAceptarCitas(activo) }
    }

    /** Cambia al otro perfil (modo practica): guarda el otro rol en DataStore y avisa a cual ir. */
    fun cambiarPerfil(onCambiado: (Rol) -> Unit) {
        val actual = Rol.de(_usuarioActual.value?.rol) ?: return
        val otro = if (actual == Rol.CLIENTE) Rol.ADMIN else Rol.CLIENTE
        viewModelScope.launch {
            val usuario = repo.obtenerUsuarioPorRol(otro) ?: return@launch
            _usuarioActual.value = usuario
            prefs.guardarSesion(otro, usuario.id)
            onCambiado(otro)
        }
    }

    /** Borra la sesion de DataStore (el modo oscuro se mantiene). */
    fun cerrarSesion(onCerrada: () -> Unit) {
        viewModelScope.launch {
            prefs.cerrarSesion()
            _usuarioActual.value = null
            onCerrada()
        }
    }
}