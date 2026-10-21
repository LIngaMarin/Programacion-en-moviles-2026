package com.tecsup.huellitasvet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.tecsup.huellitasvet.data.local.entity.Cita
import com.tecsup.huellitasvet.data.local.relation.CitaConDetalle
import com.tecsup.huellitasvet.model.ConteoServicio
import com.tecsup.huellitasvet.model.PuntoPeso
import kotlinx.coroutines.flow.Flow

/**
 * Acceso a la tabla citas.
 * Las lecturas que devuelven CitaConDetalle llevan @Transaction porque Room
 * arma la cita con su mascota, el dueño y el servicio en varias consultas.
 */
@Dao
interface CitaDao {

    // ---------- Escritura ----------
    @Insert
    suspend fun insertar(c: Cita): Long

    @Insert
    suspend fun insertarTodas(lista: List<Cita>)

    @Query("UPDATE citas SET estado = :estado WHERE id = :id")
    suspend fun actualizarEstado(id: Int, estado: String)

    @Query("UPDATE citas SET fecha = :fecha, hora = :hora, estado = 'PENDIENTE' WHERE id = :id")
    suspend fun reprogramar(id: Int, fecha: String, hora: String)

    // Pasa la cita a ATENDIDA y guarda los datos de la atencion
    @Query(
        "UPDATE citas SET estado = 'ATENDIDA', diagnostico = :diagnostico, " +
                "tratamiento = :tratamiento, pesoRegistrado = :peso WHERE id = :id"
    )
    suspend fun registrarAtencion(id: Int, diagnostico: String, tratamiento: String, peso: Double)

    @Query("SELECT * FROM citas WHERE id = :id")
    suspend fun obtener(id: Int): Cita?

    // ---------- Horarios ----------
    // Cuantas citas (no canceladas) hay en esa fecha y hora: si es > 0, el horario esta ocupado
    @Query("SELECT COUNT(*) FROM citas WHERE fecha = :fecha AND hora = :hora AND estado != 'CANCELADA'")
    suspend fun contarOcupadas(fecha: String, hora: String): Int

    // Horas ocupadas de un dia (no cuenta las canceladas)
    @Query("SELECT hora FROM citas WHERE fecha = :fecha AND estado != 'CANCELADA'")
    fun observarHorasOcupadas(fecha: String): Flow<List<String>>

    // ---------- Lectura ----------
    // Citas de todas las mascotas de un dueño (INNER JOIN con mascotas)
    @Transaction
    @Query(
        "SELECT c.* FROM citas c INNER JOIN mascotas m ON c.mascotaId = m.id " +
                "WHERE m.duenoId = :duenoId ORDER BY c.fecha, c.hora"
    )
    fun observarPorDueno(duenoId: Int): Flow<List<CitaConDetalle>>

    // Agenda del veterinario: citas de un dia ordenadas por hora
    @Transaction
    @Query("SELECT * FROM citas WHERE fecha = :fecha ORDER BY hora")
    fun observarPorFecha(fecha: String): Flow<List<CitaConDetalle>>

    @Transaction
    @Query("SELECT * FROM citas WHERE id = :id")
    fun observarUna(id: Int): Flow<CitaConDetalle?>

    // Historial clinico: solo atendidas, la mas reciente primero
    @Transaction
    @Query("SELECT * FROM citas WHERE mascotaId = :mascotaId AND estado = 'ATENDIDA' ORDER BY fecha DESC, hora DESC")
    fun observarHistorial(mascotaId: Int): Flow<List<CitaConDetalle>>

    // Pesos registrados en citas ATENDIDAS de una mascota, ordenados de la mas antigua a la mas reciente
    @Query(
        "SELECT fecha, pesoRegistrado AS peso FROM citas " +
                "WHERE mascotaId = :mascotaId AND estado = 'ATENDIDA' AND pesoRegistrado IS NOT NULL " +
                "ORDER BY fecha ASC, hora ASC"
    )
    fun observarPesos(mascotaId: Int): Flow<List<PuntoPeso>>

    // Si es mayor que 0, el servicio tiene citas y no se puede eliminar
    @Query("SELECT COUNT(*) FROM citas WHERE servicioId = :servicioId")
    suspend fun contarPorServicioId(servicioId: Int): Int

    // ---------- Panel ----------
    // Citas de un dia sin contar las canceladas
    @Query("SELECT COUNT(*) FROM citas WHERE fecha = :fecha AND estado != 'CANCELADA'")
    fun contarPorFecha(fecha: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM citas WHERE estado = :estado")
    fun contarPorEstado(estado: String): Flow<Int>

    // Cuantas citas tiene cada servicio (JOIN con servicios + GROUP BY)
    @Query(
        "SELECT s.nombre AS nombre, COUNT(c.id) AS cantidad FROM citas c " +
                "INNER JOIN servicios s ON c.servicioId = s.id " +
                "WHERE c.estado != 'CANCELADA' GROUP BY s.id, s.nombre ORDER BY cantidad DESC"
    )
    fun contarPorServicio(): Flow<List<ConteoServicio>>

    // Suma del precio de las citas ATENDIDAS desde una fecha (0.0 si no hay ninguna)
    @Query("SELECT COALESCE(SUM(precio), 0.0) FROM citas WHERE estado = 'ATENDIDA' AND fecha >= :fechaInicio")
    fun ingresosDesde(fechaInicio: String): Flow<Double>
}