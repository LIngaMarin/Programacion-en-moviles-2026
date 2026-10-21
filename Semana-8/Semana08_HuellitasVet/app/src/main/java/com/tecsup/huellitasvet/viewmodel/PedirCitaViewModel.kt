package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Servicio
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Horarios
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

/**
 * Pedir una cita: elegir mascota, servicio, fecha y hora sin chocar con otras citas.
 * Todas las reglas de fechas y horarios viven aqui.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class PedirCitaViewModel(
    private val repo: VetRepository,
    duenoId: Int,
    mascotaInicial: Int?
) : ViewModel() {

    /** Solo las mascotas del dueño. */
    val mascotas: StateFlow<List<Mascota>> = repo.observarMascotasDeDueno(duenoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Solo los servicios ACTIVOS: los inactivos no aparecen al pedir cita. */
    val servicios: StateFlow<List<Servicio>> = repo.observarServiciosActivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Fechas permitidas: de hoy hasta 7 dias despues. */
    val fechas: List<LocalDate> = (0L..7L).map { LocalDate.now().plusDays(it) }

    /** Los 18 horarios del dia (09:00 a 17:30 cada 30 min). */
    val horarios: List<String> = Horarios.delDia

    // ---------- Lo que elige el dueño ----------
    private val _mascotaId = MutableStateFlow(mascotaInicial)
    val mascotaId: StateFlow<Int?> = _mascotaId.asStateFlow()

    private val _servicioId = MutableStateFlow<Int?>(null)
    val servicioId: StateFlow<Int?> = _servicioId.asStateFlow()

    private val _fecha = MutableStateFlow(LocalDate.now())   // por defecto: hoy
    val fecha: StateFlow<LocalDate> = _fecha.asStateFlow()

    private val _hora = MutableStateFlow<String?>(null)
    val hora: StateFlow<String?> = _hora.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    /** Horas ocupadas del dia elegido: cambia sola cuando cambia la fecha (flatMapLatest). */
    val horasOcupadas: StateFlow<List<String>> = _fecha
        .flatMapLatest { f -> repo.observarHorasOcupadas(f.toString()) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /**
     * Horas que NO se pueden elegir: las ocupadas por otra cita (no cancelada)
     * y, si la fecha es hoy, las que ya pasaron.
     */
    val horasNoDisponibles: StateFlow<Set<String>> = combine(_fecha, horasOcupadas) { f, ocupadas ->
        val pasadas = if (f == LocalDate.now()) {
            val ahora = LocalTime.now()
            horarios.filter { !LocalTime.parse(it).isAfter(ahora) }
        } else emptyList()
        (ocupadas + pasadas).toSet()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    fun elegirMascota(id: Int) { _mascotaId.value = id }

    fun elegirServicio(id: Int) { _servicioId.value = id }

    /** Si la fecha cambia, se limpia la hora elegida. */
    fun elegirFecha(f: LocalDate) {
        _fecha.value = f
        _hora.value = null
    }

    fun elegirHora(h: String) {
        if (h !in horasNoDisponibles.value) _hora.value = h
    }

    fun limpiarMensaje() { _mensaje.value = null }

    /** Valida todo, vuelve a revisar que el horario siga libre y crea la cita (PENDIENTE). */
    fun confirmar(onCreada: () -> Unit) {
        val mascota = _mascotaId.value
        val servicio = _servicioId.value
        val f = _fecha.value
        val h = _hora.value
        if (mascota == null || servicio == null || h == null) {
            _mensaje.value = "Elige la mascota, el servicio, la fecha y la hora"
            return
        }
        viewModelScope.launch {
            // El servicio debe seguir activo
            if (servicios.value.none { it.id == servicio }) {
                _mensaje.value = "Ese servicio ya no está disponible"
                return@launch
            }
            // Si es hoy, la hora no puede haber pasado
            if (f == LocalDate.now() && !LocalTime.parse(h).isAfter(LocalTime.now())) {
                _mensaje.value = "Esa hora ya pasó, elige otra"
                _hora.value = null
                return@launch
            }
            // Vuelve a consultar: dos citas no pueden compartir fecha y hora
            val ocupadas = repo.observarHorasOcupadas(f.toString()).first()
            if (h in ocupadas) {
                _mensaje.value = "Ese horario se acaba de ocupar, elige otro"
                _hora.value = null
                return@launch
            }
            val id = repo.crearCita(mascota, servicio, f.toString(), h, "")
            if (id > 0) onCreada() else _mensaje.value = "No se pudo crear la cita"
        }
    }
}