package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.EstadoCita
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Agenda del veterinario: citas del dia elegido ordenadas por hora,
 * y los cambios de estado permitidos (confirmar y cancelar).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AgendaViewModel(private val repo: VetRepository) : ViewModel() {

    /** Chips de los proximos dias: hoy y los 6 siguientes. */
    val fechas: List<LocalDate> = (0L..6L).map { LocalDate.now().plusDays(it) }

    private val _fechaSeleccionada = MutableStateFlow(LocalDate.now())   // por defecto: hoy
    val fechaSeleccionada: StateFlow<LocalDate> = _fechaSeleccionada.asStateFlow()

    /** Citas del dia elegido, ordenadas por hora (lo ordena el DAO). */
    val citas: StateFlow<List<CitaConDetalle>> = _fechaSeleccionada
        .flatMapLatest { f -> repo.observarCitasPorFecha(f.toString()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun elegirFecha(f: LocalDate) {
        _fechaSeleccionada.value = f
    }

    /** PENDIENTE -> CONFIRMADA. */
    fun confirmar(c: CitaConDetalle) {
        if (c.cita.estado != EstadoCita.PENDIENTE.name) return
        viewModelScope.launch { repo.cambiarEstadoCita(c.cita.id, EstadoCita.CONFIRMADA) }
    }

    /** Desde PENDIENTE o CONFIRMADA se puede cancelar. ATENDIDA y CANCELADA son finales. */
    fun cancelar(c: CitaConDetalle) {
        val estado = c.cita.estado
        if (estado != EstadoCita.PENDIENTE.name && estado != EstadoCita.CONFIRMADA.name) return
        viewModelScope.launch { repo.cambiarEstadoCita(c.cita.id, EstadoCita.CANCELADA) }
    }
}