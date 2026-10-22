package com.tecsup.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.entity.Vacuna
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.model.Especie
import com.tecsup.huellitasvet.model.PuntoPeso
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

/** Errores del formulario de vacuna (null = ese campo esta bien). */
data class ErroresVacuna(
    val nombre: String? = null,
    val fecha: String? = null,
    val proximaDosis: String? = null
) {
    val hayErrores: Boolean
        get() = listOf(nombre, fecha, proximaDosis).any { it != null }
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

    /** Registro de pesos en citas ATENDIDAS de la mascota, ordenados cronológicamente. */
    val pesos: StateFlow<List<PuntoPeso>> =
        (if (mascotaId != null) repo.observarPesos(mascotaId) else flowOf(emptyList()))
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Vacunas registradas para la mascota, ordenadas de la mas reciente a la mas antigua. */
    val vacunas: StateFlow<List<Vacuna>> =
        (if (mascotaId != null) repo.observarVacunas(mascotaId) else flowOf(emptyList()))
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _errores = MutableStateFlow(ErroresMascota())
    val errores: StateFlow<ErroresMascota> = _errores.asStateFlow()

    private val _erroresVacuna = MutableStateFlow(ErroresVacuna())
    val erroresVacuna: StateFlow<ErroresVacuna> = _erroresVacuna.asStateFlow()

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

    /** Regla de negocio: la vacuna se considera vencida si tiene proxima dosis y esa fecha es anterior a hoy. */
    fun esVacunaVencida(v: Vacuna): Boolean {
        val prox = v.proximaDosis ?: return false
        return try {
            LocalDate.parse(prox).isBefore(LocalDate.now())
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Valida y agrega una vacuna a la mascota actual.
     * Validaciones:
     * - nombre: minimo 3 caracteres
     * - fecha: valida (LocalDate.parse) y no futura
     * - proximaDosis: opcional, pero si viene debe ser valida y posterior a fecha
     */
    fun agregarVacuna(
        nombre: String,
        fecha: String,
        proximaDosis: String?,
        onListo: () -> Unit
    ) {
        val nom = nombre.trim()
        val fec = fecha.trim()
        val prox = proximaDosis?.trim()?.ifBlank { null }
        val hoy = LocalDate.now()

        val errNombre = if (nom.length < 3) "El nombre debe tener al menos 3 caracteres" else null

        var fecParsed: LocalDate? = null
        var errFecha: String? = null
        if (fec.isBlank()) {
            errFecha = "Ingresa la fecha (aaaa-MM-dd)"
        } else {
            try {
                fecParsed = LocalDate.parse(fec)
                if (fecParsed.isAfter(hoy)) {
                    errFecha = "La fecha no puede ser futura"
                }
            } catch (e: Exception) {
                errFecha = "Fecha inválida (usar aaaa-MM-dd)"
            }
        }

        var errProx: String? = null
        if (prox != null) {
            try {
                val proxParsed = LocalDate.parse(prox)
                if (fecParsed != null && !proxParsed.isAfter(fecParsed)) {
                    errProx = "Debe ser posterior a la fecha de aplicación"
                }
            } catch (e: Exception) {
                errProx = "Fecha inválida (usar aaaa-MM-dd)"
            }
        }

        val err = ErroresVacuna(
            nombre = errNombre,
            fecha = errFecha,
            proximaDosis = errProx
        )
        _erroresVacuna.value = err

        if (err.hayErrores || fecParsed == null) return

        val mId = mascotaId ?: return
        viewModelScope.launch {
            repo.agregarVacuna(
                Vacuna(
                    mascotaId = mId,
                    nombre = nom,
                    fecha = fec,
                    proximaDosis = prox
                )
            )
            _erroresVacuna.value = ErroresVacuna()
            onListo()
        }
    }

    fun eliminarVacuna(v: Vacuna) {
        viewModelScope.launch {
            repo.eliminarVacuna(v)
        }
    }

    fun limpiarErroresVacuna() {
        _erroresVacuna.value = ErroresVacuna()
    }
}