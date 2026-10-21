package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.relation.MascotaConDueno
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Especie
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/** Un paciente para la lista: la mascota con su dueño y la fecha de su ultima atencion. */
data class Paciente(
    val datos: MascotaConDueno,
    val ultimaVisita: String?      // 'aaaa-MM-dd' o null si nunca fue atendido
)

/**
 * Pacientes del veterinario: todas las mascotas con su dueño,
 * filtradas por texto (mascota o dueño) y por especie con combine.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class PacientesViewModel(private val repo: VetRepository) : ViewModel() {

    private val _texto = MutableStateFlow("")
    val texto: StateFlow<String> = _texto.asStateFlow()

    private val _especie = MutableStateFlow<Especie?>(null)   // null = Todos
    val especie: StateFlow<Especie?> = _especie.asStateFlow()

    private val todas = repo.observarTodasLasMascotas()

    // Ultima visita de cada mascota: la primera cita del historial (ya viene la mas reciente primero)
    private val ultimasVisitas: Flow<Map<Int, String?>> = todas.flatMapLatest { lista ->
        if (lista.isEmpty()) flowOf(emptyMap())
        else combine(
            lista.map { p ->
                repo.observarHistorial(p.mascota.id).map { historial ->
                    p.mascota.id to historial.firstOrNull()?.cita?.fecha
                }
            }
        ) { pares -> pares.toMap() }
    }

    val pacientes: StateFlow<List<Paciente>> =
        combine(todas, _texto, _especie, ultimasVisitas) { lista, texto, especie, visitas ->
            val buscar = texto.trim()
            lista
                .filter { p ->
                    buscar.isBlank() ||
                            p.mascota.nombre.contains(buscar, ignoreCase = true) ||
                            p.dueno.nombre.contains(buscar, ignoreCase = true)
                }
                .filter { p -> especie == null || p.mascota.especie == especie.name }
                .map { p -> Paciente(p, visitas[p.mascota.id]) }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun cambiarTexto(nuevo: String) { _texto.value = nuevo }

    fun elegirEspecie(e: Especie?) { _especie.value = e }
}