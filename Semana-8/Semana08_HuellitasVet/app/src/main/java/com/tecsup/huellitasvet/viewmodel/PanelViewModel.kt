package com.tecsup.huellitasvet.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.EstadoCita
import com.tecsup.huellitasvet.ui.components.graficos.DatoGrafico
import com.tecsup.huellitasvet.ui.theme.AzulPrimario
import com.tecsup.huellitasvet.ui.theme.colorEstado
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate

/**
 * Panel de la clinica: contadores, ingresos del mes y datos para los graficos.
 * Convierte lo que viene de Room al formato DatoGrafico que usan GraficoDona y GraficoBarras.
 */
class PanelViewModel(private val repo: VetRepository) : ViewModel() {

    private val hoy = LocalDate.now().toString()
    private val inicioMes = LocalDate.now().withDayOfMonth(1).toString()   // dia 1 del mes

    /** Citas de hoy sin contar las canceladas. */
    val citasHoy: StateFlow<Int> = repo.observarCitasDeFecha(hoy)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val pendientes: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoCita.PENDIENTE)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val atendidas: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoCita.ATENDIDA)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    /** Suma del precio de las citas ATENDIDAS desde el dia 1 del mes. */
    val ingresosMes: StateFlow<Double> = repo.observarIngresosDesde(inicioMes)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    /** Citas por estado para el grafico de dona (un Flow por estado, unidos con combine). */
    val porEstado: StateFlow<List<DatoGrafico>> = combine(
        EstadoCita.entries.map { estado ->
            repo.observarCantidadPorEstado(estado).map { cantidad ->
                DatoGrafico(estado.etiqueta, cantidad.toFloat(), colorEstado(estado))
            }
        }
    ) { datos -> datos.toList() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Citas por servicio para el grafico de barras (tonos de azul). */
    val porServicio: StateFlow<List<DatoGrafico>> = repo.observarCitasPorServicio()
        .map { lista ->
            lista.mapIndexed { i, conteo ->
                DatoGrafico(conteo.nombre, conteo.cantidad.toFloat(), coloresBarras[i % coloresBarras.size])
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val coloresBarras = listOf(AzulPrimario, Color(0xFF3B82F6), Color(0xFF60A5FA), Color(0xFF93C5FD))
}