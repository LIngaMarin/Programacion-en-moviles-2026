package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.EstadoCita
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * Inicio del dueño: sus mascotas y su proxima cita.
 * Recibe el id del dueño que inicio sesion.
 */
class InicioViewModel(
    private val repo: VetRepository,
    private val duenoId: Int
) : ViewModel() {

    val mascotas: StateFlow<List<Mascota>> = repo.observarMascotasDeDueno(duenoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Citas PENDIENTE o CONFIRMADA desde ahora, ordenadas por fecha y hora (la regla vive aqui)
    private val proximas = repo.observarCitasDeDueno(duenoId).map { lista ->
        val ahora = LocalDateTime.now()
        lista
            .filter { it.cita.estado == EstadoCita.PENDIENTE.name || it.cita.estado == EstadoCita.CONFIRMADA.name }
            .filter { !fechaHora(it).isBefore(ahora) }
            .sortedBy { fechaHora(it) }
    }

    /** La primera cita pendiente o confirmada desde ahora (null si no tiene). */
    val proximaCita: StateFlow<CitaConDetalle?> = proximas
        .map { it.firstOrNull() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    /** Tarjeta "Recordatorio" del mockup: la siguiente cita despues de la proxima (null si no hay). */
    val recordatorio: StateFlow<CitaConDetalle?> = proximas
        .map { it.getOrNull(1) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Fecha y hora de la cita juntas, para compararlas con LocalDateTime.now()
    private fun fechaHora(c: CitaConDetalle): LocalDateTime =
        LocalDateTime.of(LocalDate.parse(c.cita.fecha), LocalTime.parse(c.cita.hora))
}