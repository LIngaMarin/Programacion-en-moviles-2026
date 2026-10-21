package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.EstadoCita
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Errores del registro clinico, uno por campo. */
data class ErroresAtencion(
    val diagnostico: String? = null,
    val peso: String? = null
)

/**
 * Atender una cita: registrar diagnostico, tratamiento y peso (en una transaccion)
 * o cancelarla. Solo se atiende una cita PENDIENTE o CONFIRMADA.
 */
class AtenderCitaViewModel(
    private val repo: VetRepository,
    private val citaId: Int
) : ViewModel() {

    val cita: StateFlow<CitaConDetalle?> = repo.observarCita(citaId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _errores = MutableStateFlow(ErroresAtencion())
    val errores: StateFlow<ErroresAtencion> = _errores.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    /** true si la cita todavia se puede atender o cancelar. */
    fun esAtendible(c: CitaConDetalle?): Boolean =
        c != null && (c.cita.estado == EstadoCita.PENDIENTE.name || c.cita.estado == EstadoCita.CONFIRMADA.name)

    /**
     * Valida: diagnostico de al menos 5 caracteres y peso mayor que 0.
     * repo.registrarAtencion deja la cita ATENDIDA y actualiza el peso de la mascota a la vez.
     */
    fun registrar(diagnostico: String, tratamiento: String, pesoTexto: String, onListo: () -> Unit) {
        if (!esAtendible(cita.value)) {
            _mensaje.value = "Solo se puede atender una cita pendiente o confirmada"
            return
        }
        val peso = pesoTexto.trim().replace(',', '.').toDoubleOrNull()
        val errores = ErroresAtencion(
            diagnostico = if (diagnostico.trim().length < 5) "El diagnóstico debe tener al menos 5 caracteres" else null,
            peso = if (peso == null || peso <= 0.0) "El peso debe ser mayor que 0" else null
        )
        _errores.value = errores
        if (errores.diagnostico != null || errores.peso != null || peso == null) return

        viewModelScope.launch {
            repo.registrarAtencion(citaId, diagnostico.trim(), tratamiento.trim(), peso)
            onListo()
        }
    }

    fun cancelar(onListo: () -> Unit) {
        if (!esAtendible(cita.value)) {
            _mensaje.value = "Esta cita ya no se puede cancelar"
            return
        }
        viewModelScope.launch {
            repo.cambiarEstadoCita(citaId, EstadoCita.CANCELADA)
            onListo()
        }
    }

    fun limpiarMensaje() { _mensaje.value = null }
}