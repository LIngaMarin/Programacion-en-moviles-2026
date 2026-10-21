package com.tecsup.huellitasvet.data.repository

import com.tecsup.huellitasvet.data.local.entity.Servicio
import kotlinx.coroutines.flow.Flow

/**
 * ENTREGADO. El contrato del Repository: los ViewModels solo conocen esta interface.
 * Hoy la implementa VetRepositoryRoom (Room); mas adelante se podra implementar con
 * un servicio web sin cambiar ViewModels ni pantallas.
 *
 * Las funciones de usuarios, mascotas, citas y panel usan clases que se crean en los
 * Pasos 1 y 2 (Usuario, Mascota, Cita, MascotaConDueno, CitaConDetalle). Por eso estan
 * comentadas: en el Paso 4 se descomentan y se implementan en VetRepositoryRoom.
 */
interface VetRepository {

    // Servicios (ejemplo resuelto)
    fun observarServicios(): Flow<List<Servicio>>
    fun observarServiciosActivos(): Flow<List<Servicio>>
    suspend fun guardarServicio(s: Servicio)
    suspend fun eliminarServicio(s: Servicio)
    suspend fun cambiarActivo(id: Int, activo: Boolean)
    suspend fun servicioTieneCitas(id: Int): Boolean

    // TODO (Paso 4): descomentar cuando existan las entidades y relaciones
    /*
    // Usuarios
    suspend fun login(correo: String, clave: String): Usuario?

    // Mascotas
    fun observarMascotasDeDueno(duenoId: Int): Flow<List<Mascota>>
    fun observarMascota(id: Int): Flow<Mascota?>
    fun observarTodasLasMascotas(): Flow<List<MascotaConDueno>>
    suspend fun guardarMascota(m: Mascota)
    suspend fun eliminarMascota(m: Mascota)
    suspend fun mascotaTieneCitas(id: Int): Boolean

    // Citas
    fun observarHorasOcupadas(fecha: String): Flow<List<String>>
    suspend fun crearCita(mascotaId: Int, servicioId: Int, fecha: String,
                          hora: String, nota: String): Long
    fun observarCitasDeDueno(duenoId: Int): Flow<List<CitaConDetalle>>
    fun observarCitasPorFecha(fecha: String): Flow<List<CitaConDetalle>>
    fun observarCita(id: Int): Flow<CitaConDetalle?>
    fun observarHistorial(mascotaId: Int): Flow<List<CitaConDetalle>>
    suspend fun cambiarEstadoCita(id: Int, estado: EstadoCita)
    suspend fun registrarAtencion(citaId: Int, diagnostico: String,
                                  tratamiento: String, peso: Double)

    // Panel
    fun observarCitasDeFecha(fecha: String): Flow<Int>
    fun observarCantidadPorEstado(estado: EstadoCita): Flow<Int>
    fun observarCitasPorServicio(): Flow<List<ConteoServicio>>
    fun observarIngresosDesde(fechaInicio: String): Flow<Double>
    */
}
