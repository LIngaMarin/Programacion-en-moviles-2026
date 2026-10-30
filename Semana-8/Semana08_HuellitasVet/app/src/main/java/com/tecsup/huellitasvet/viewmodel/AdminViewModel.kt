package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.local.entity.TipoServicio
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

/** Un tipo de servicio con los servicios que pertenecen a el (para la pantalla Tipos). */
data class TipoConServicios(
    val tipo: TipoServicio,
    val servicios: List<Servicio>
)

/**
 * Administrador: gestiona usuarios (dueños y veterinarios), mascotas y tipos de servicio.
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

    // ---------- Tipos de servicio ----------
    val tipos: StateFlow<List<TipoServicio>> = repo.observarTiposServicio()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Todos los servicios (para elegir cuales pertenecen a un tipo). */
    val servicios: StateFlow<List<Servicio>> = repo.observarServicios()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Cada tipo con sus servicios, y aparte los servicios que no tienen tipo. */
    val tiposConServicios: StateFlow<List<TipoConServicios>> = combine(tipos, servicios) { ts, ss ->
        ts.map { t -> TipoConServicios(t, ss.filter { it.tipoId == t.id }) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val serviciosSinTipo: StateFlow<List<Servicio>> = combine(tipos, servicios) { _, ss ->
        ss.filter { it.tipoId == null }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Error del dialogo de crear/editar tipo
    private val _errorTipo = MutableStateFlow<String?>(null)
    val errorTipo: StateFlow<String?> = _errorTipo.asStateFlow()

    /** Nuevo (id = 0) o editado (id > 0). Valida: al menos 3 letras y que el nombre no se repita. */
    fun guardarTipo(id: Int, nombre: String, onListo: () -> Unit) {
        val limpio = nombre.trim()
        if (limpio.length < 3) {
            _errorTipo.value = "El nombre debe tener al menos 3 letras"
            return
        }
        viewModelScope.launch {
            if (repo.nombreTipoExiste(limpio, id)) {
                _errorTipo.value = "Ya existe un tipo con ese nombre"
                return@launch
            }
            repo.guardarTipoServicio(TipoServicio(id = id, nombre = limpio))
            _errorTipo.value = null
            onListo()
        }
    }

    /** Elimina el tipo: sus servicios no se borran, quedan "Sin tipo" (ForeignKey SET_NULL). */
    fun eliminarTipo(t: TipoServicio) {
        viewModelScope.launch { repo.eliminarTipoServicio(t) }
    }

    /** Pone el servicio en un tipo, o lo quita del tipo con tipoId = null. */
    fun asignarServicio(servicioId: Int, tipoId: Int?) {
        viewModelScope.launch { repo.asignarTipo(servicioId, tipoId) }
    }

    fun limpiarErrorTipo() { _errorTipo.value = null }

    fun limpiarMensaje() { _mensaje.value = null }

    fun limpiarError() { _error.value = null }
}
