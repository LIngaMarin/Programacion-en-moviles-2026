package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.repository.VetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Errores del formulario de servicio, uno por campo. */
data class ErroresServicio(
    val nombre: String? = null,
    val duracion: String? = null,
    val precio: String? = null
)

/**
 * Ejemplo resuelto + Paso 13: lista de servicios, activar/desactivar, guardar y eliminar.
 * Flow del repository -> StateFlow con stateIn.
 */
class ServiciosViewModel(private val repo: VetRepository) : ViewModel() {

    val servicios: StateFlow<List<Servicio>> = repo.observarServicios()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Duraciones permitidas en el formulario. */
    val duraciones = listOf(15, 30, 45, 60)

    private val _errores = MutableStateFlow(ErroresServicio())
    val errores: StateFlow<ErroresServicio> = _errores.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun alternar(s: Servicio) {
        viewModelScope.launch { repo.cambiarActivo(s.id, !s.activo) }
    }

    /**
     * Nuevo (id = 0) o editado (id > 0).
     * Valida: nombre obligatorio, duracion 15/30/45/60 y precio mayor que 0.
     */
    fun guardar(
        id: Int,
        nombre: String,
        duracionMin: Int?,
        precioTexto: String,
        descripcion: String,
        icono: String,
        activo: Boolean,
        onListo: () -> Unit
    ) {
        val precio = precioTexto.trim().replace(',', '.').toDoubleOrNull()
        val errores = ErroresServicio(
            nombre = if (nombre.isBlank()) "El nombre es obligatorio" else null,
            duracion = if (duracionMin == null || duracionMin !in duraciones) "Elige la duración" else null,
            precio = if (precio == null || precio <= 0.0) "El precio debe ser mayor que 0" else null
        )
        _errores.value = errores
        if (errores.nombre != null || errores.duracion != null || errores.precio != null ||
            duracionMin == null || precio == null
        ) return

        viewModelScope.launch {
            repo.guardarServicio(
                Servicio(
                    id = id,
                    nombre = nombre.trim(),
                    descripcion = descripcion.trim(),
                    duracionMin = duracionMin,
                    precio = precio,
                    activo = activo,
                    icono = icono
                )
            )
            onListo()
        }
    }

    /** Un servicio con citas no se elimina: se muestra un mensaje (solo se puede desactivar). */
    fun eliminar(s: Servicio, onEliminado: () -> Unit) {
        viewModelScope.launch {
            if (repo.servicioTieneCitas(s.id)) {
                _mensaje.value = "No se puede eliminar \"${s.nombre}\" porque tiene citas. Puedes desactivarlo."
            } else {
                repo.eliminarServicio(s)
                onEliminado()
            }
        }
    }

    fun limpiarMensaje() { _mensaje.value = null }
}