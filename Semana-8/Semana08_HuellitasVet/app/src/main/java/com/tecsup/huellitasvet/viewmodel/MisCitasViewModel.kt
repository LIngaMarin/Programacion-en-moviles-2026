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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/** Una cita con la regla ya calculada: si el dueño todavia la puede cancelar. */
data class CitaItem(
    val detalle: CitaConDetalle,
    val puedeCancelar: Boolean
)

/**
 * Mis citas del dueño: Proximas e Historial, y cancelar con la regla de las 2 horas.
 * Como todo viene de un Flow de Room, si el veterinario confirma o atiende una cita
 * la lista cambia sola.
 */
class MisCitasViewModel(
    private val repo: VetRepository,
    duenoId: Int
) : ViewModel() {

    private val citas = repo.observarCitasDeDueno(duenoId)

    /** PENDIENTE y CONFIRMADA desde ahora, la mas cercana primero. */
    val proximas: StateFlow<List<CitaItem>> = citas.map { lista ->
        val ahora = LocalDateTime.now()
        lista.filter { esActiva(it) && !fechaHora(it).isBefore(ahora) }
            .sortedBy { fechaHora(it) }
            .map { CitaItem(it, puedeCancelar(it)) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** ATENDIDA y CANCELADA, y tambien las que ya pasaron; la mas reciente primero. */
    val historial: StateFlow<List<CitaItem>> = citas.map { lista ->
        val ahora = LocalDateTime.now()
        lista.filter { !esActiva(it) || fechaHora(it).isBefore(ahora) }
            .sortedByDescending { fechaHora(it) }
            .map { CitaItem(it, false) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    /** Regla: solo PENDIENTE o CONFIRMADA y solo si faltan MAS de 2 horas. */
    fun puedeCancelar(c: CitaConDetalle): Boolean {
        val faltan = Duration.between(LocalDateTime.now(), fechaHora(c))
        return esActiva(c) && faltan > Duration.ofHours(2)
    }

    fun cancelar(c: CitaConDetalle) {
        if (!puedeCancelar(c)) {
            _mensaje.value = "Ya no puedes cancelar esta cita"
            return
        }
        viewModelScope.launch { repo.cambiarEstadoCita(c.cita.id, EstadoCita.CANCELADA) }
    }

    fun limpiarMensaje() { _mensaje.value = null }

    private fun esActiva(c: CitaConDetalle): Boolean =
        c.cita.estado == EstadoCita.PENDIENTE.name || c.cita.estado == EstadoCita.CONFIRMADA.name

    private fun fechaHora(c: CitaConDetalle): LocalDateTime =
        LocalDateTime.of(LocalDate.parse(c.cita.fecha), LocalTime.parse(c.cita.hora))
}