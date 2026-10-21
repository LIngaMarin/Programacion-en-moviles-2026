package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.model.Horarios
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * ViewModel para reprogramar una cita existente.
 * Mantiene la cita actual, fecha y hora elegidas, y valida reglas de negocio.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ReprogramarCitaViewModel(
    private val repo: VetRepository,
    private val citaId: Int
) : ViewModel() {

    /** Cita actual a reprogramar. */
    val citaActual: StateFlow<CitaConDetalle?> = repo.observarCita(citaId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    /** Fechas permitidas: de hoy a 7 dias despues. */
    val fechas: List<LocalDate> = (0L..7L).map { LocalDate.now().plusDays(it) }

    /** Los 18 horarios del dia. */
    val horarios: List<String> = Horarios.delDia

    private val _fecha = MutableStateFlow(LocalDate.now())
    val fecha: StateFlow<LocalDate> = _fecha.asStateFlow()

    private val _hora = MutableStateFlow<String?>(null)
    val hora: StateFlow<String?> = _hora.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    /** Horas ocupadas en el dia elegido. */
    val horasOcupadas: StateFlow<List<String>> = _fecha
        .flatMapLatest { f -> repo.observarHorasOcupadas(f.toString()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * Horas no disponibles: ocupadas de ese dia (la hora actual de ESTA misma cita no cuenta)
     * + las horas pasadas si la fecha elegida es hoy.
     */
    val horasNoDisponibles: StateFlow<Set<String>> = combine(_fecha, horasOcupadas, citaActual) { f, ocupadas, c ->
        val ocupadasSinEsta = if (c != null && f.toString() == c.cita.fecha) {
            ocupadas - c.cita.hora
        } else {
            ocupadas
        }
        val pasadas = if (f == LocalDate.now()) {
            val ahora = LocalTime.now()
            horarios.filter { !LocalTime.parse(it).isAfter(ahora) }
        } else emptyList()
        (ocupadasSinEsta + pasadas).toSet()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    init {
        viewModelScope.launch {
            val c = repo.observarCita(citaId).filterNotNull().first()
            _fecha.value = LocalDate.parse(c.cita.fecha)
            _hora.value = c.cita.hora
        }
    }

    /** Al cambiar fecha, la hora elegida se limpia. */
    fun elegirFecha(f: LocalDate) {
        _fecha.value = f
        _hora.value = null
    }

    fun elegirHora(h: String) {
        if (h !in horasNoDisponibles.value) {
            _hora.value = h
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    /**
     * Valida reglas de negocio, re-verifica disponibilidad y guarda el nuevo horario.
     */
    fun confirmar(onListo: () -> Unit) {
        val c = citaActual.value
        if (c == null) {
            _mensaje.value = "No se encontró la cita"
            return
        }

        // Regla: solo PENDIENTE o CONFIRMADA
        val estado = c.cita.estado
        if (estado != EstadoCita.PENDIENTE.name && estado != EstadoCita.CONFIRMADA.name) {
            _mensaje.value = "Solo se pueden reprogramar citas pendientes o confirmadas"
            return
        }

        // Regla: faltan mas de 2 horas para la cita actual
        val fechaHoraCitaActual = LocalDateTime.of(
            LocalDate.parse(c.cita.fecha),
            LocalTime.parse(c.cita.hora)
        )
        val faltan = Duration.between(LocalDateTime.now(), fechaHoraCitaActual)
        if (faltan <= Duration.ofHours(2)) {
            _mensaje.value = "Ya no puedes reprogramar esta cita (faltan menos de 2 horas)"
            return
        }

        val f = _fecha.value
        val h = _hora.value
        if (h == null) {
            _mensaje.value = "Elige la fecha y la hora"
            return
        }

        // Correccion: si elige el mismo horario que ya tenia, no se reprograma
        if (f.toString() == c.cita.fecha && h == c.cita.hora) {
            _mensaje.value = "Elige un horario distinto al actual"
            return
        }

        // Regla: fecha entre hoy y 7 dias
        val hoy = LocalDate.now()
        val maxFecha = hoy.plusDays(7)
        if (f.isBefore(hoy) || f.isAfter(maxFecha)) {
            _mensaje.value = "La fecha debe estar entre hoy y 7 días"
            return
        }

        if (f == hoy && !LocalTime.parse(h).isAfter(LocalTime.now())) {
            _mensaje.value = "Esa hora ya pasó, elige otra"
            _hora.value = null
            return
        }

        viewModelScope.launch {
            // Vuelve a consultar que el horario siga libre
            val ocupadas = repo.observarHorasOcupadas(f.toString()).first()
            val ocupadasSinEsta = if (f.toString() == c.cita.fecha) ocupadas - c.cita.hora else ocupadas
            if (h in ocupadasSinEsta) {
                _mensaje.value = "Ese horario se acaba de ocupar, elige otro"
                _hora.value = null
                return@launch
            }

            repo.reprogramarCita(citaId, f.toString(), h)
            onListo()
        }
    }
}
