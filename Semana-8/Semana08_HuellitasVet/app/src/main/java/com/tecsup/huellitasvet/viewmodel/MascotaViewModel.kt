package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.Sexo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

/** Errores del formulario, uno por campo (null = ese campo esta bien). */
data class ErroresMascota(
    val nombre: String? = null,
    val especie: String? = null,
    val sexo: String? = null,
    val anio: String? = null,
    val peso: String? = null
) {
    val hayErrores: Boolean
        get() = listOf(nombre, especie, sexo, anio, peso).any { it != null }
}

/**
 * Registrar, editar y eliminar una mascota, y ver su historial clinico.
 * mascotaId = null -> mascota nueva. duenoId = dueño que la registra.
 */
class MascotaViewModel(
    private val repo: VetRepository,
    private val mascotaId: Int?,
    private val duenoId: Int
) : ViewModel() {

    val mascota: StateFlow<Mascota?> =
        (if (mascotaId != null) repo.observarMascota(mascotaId) else flowOf(null))
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    /** Historial clinico: citas ATENDIDAS de la mascota, la mas reciente primero (lo ordena el DAO). */
    val historial: StateFlow<List<CitaConDetalle>> =
        (if (mascotaId != null) repo.observarHistorial(mascotaId) else flowOf(emptyList()))
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _errores = MutableStateFlow(ErroresMascota())
    val errores: StateFlow<ErroresMascota> = _errores.asStateFlow()

    // Mensaje para el usuario (ej. cuando no se puede eliminar)
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    /** Valida los campos; si todo esta bien guarda (nueva o editada) y llama a onListo. */
    fun guardar(
        nombre: String,
        especie: Especie?,
        raza: String,
        anioTexto: String,
        pesoTexto: String,
        sexo: Sexo?,
        onListo: () -> Unit
    ) {
        val anioActual = LocalDate.now().year
        val anio = anioTexto.trim().toIntOrNull()
        val peso = pesoTexto.trim().replace(',', '.').toDoubleOrNull()

        val errores = ErroresMascota(
            nombre = if (nombre.trim().length < 2) "El nombre debe tener al menos 2 caracteres" else null,
            especie = if (especie == null) "Elige la especie" else null,
            sexo = if (sexo == null) "Elige el sexo" else null,
            anio = if (anio == null || anio < 1990 || anio > anioActual) "Año entre 1990 y $anioActual" else null,
            peso = if (peso == null || peso <= 0.0) "El peso debe ser mayor que 0" else null
        )
        _errores.value = errores
        if (errores.hayErrores || especie == null || sexo == null || anio == null || peso == null) return

        viewModelScope.launch {
            repo.guardarMascota(
                Mascota(
                    id = mascotaId ?: 0,
                    duenoId = mascota.value?.duenoId ?: duenoId,
                    nombre = nombre.trim(),
                    especie = especie.name,
                    raza = raza.trim(),
                    anioNacimiento = anio,
                    peso = peso,
                    sexo = sexo.name
                )
            )
            onListo()
        }
    }

    /** Solo elimina si la mascota no tiene citas; si tiene, muestra un mensaje y no la borra. */
    fun eliminar(m: Mascota, onEliminada: () -> Unit) {
        viewModelScope.launch {
            if (repo.mascotaTieneCitas(m.id)) {
                _mensaje.value = "No se puede eliminar a ${m.nombre}: tiene citas registradas"
            } else {
                repo.eliminarMascota(m)
                onEliminada()
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}