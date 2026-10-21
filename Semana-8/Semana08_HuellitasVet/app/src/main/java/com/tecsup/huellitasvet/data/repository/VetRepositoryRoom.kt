package com.tecsup.huellitasvet.data.repository

import com.tecsup.huellitasvet.data.local.VetDatabase
import com.tecsup.huellitasvet.data.local.entity.Servicio
import kotlinx.coroutines.flow.Flow

/**
 * GUIADO. Implementacion del Repository con Room.
 * Las funciones de Servicio ya estan hechas (ejemplo resuelto); el resto se completa en el Paso 4.
 */
class VetRepositoryRoom(private val db: VetDatabase) : VetRepository {

    private val servicioDao = db.servicioDao()
    // TODO (Paso 4): usuarioDao, mascotaDao y citaDao

    // ---------- Servicios (ejemplo resuelto) ----------
    // observar*: solo delegan en el DAO, sin suspend
    override fun observarServicios(): Flow<List<Servicio>> = servicioDao.observarTodos()

    override fun observarServiciosActivos(): Flow<List<Servicio>> = servicioDao.observarActivos()

    // REPLACE: si el id ya existe lo actualiza, si es 0 lo inserta como nuevo
    override suspend fun guardarServicio(s: Servicio) {
        servicioDao.insertar(s)
    }

    override suspend fun eliminarServicio(s: Servicio) {
        servicioDao.eliminar(s)
    }

    override suspend fun cambiarActivo(id: Int, activo: Boolean) {
        servicioDao.cambiarActivo(id, activo)
    }

    override suspend fun servicioTieneCitas(id: Int): Boolean {
        // TODO (Paso 4): devolver true si el CitaDao cuenta citas de este servicio (> 0)
        TODO("Paso 4")
    }

    // TODO (Paso 4): implementar las demas funciones de VetRepository
}
