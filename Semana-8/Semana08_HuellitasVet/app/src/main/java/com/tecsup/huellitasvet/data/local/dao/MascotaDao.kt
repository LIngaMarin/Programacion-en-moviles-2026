package com.tecsup.huellitasvet.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.tecsup.huellitasvet.data.local.entity.Mascota
import com.tecsup.huellitasvet.data.local.relation.MascotaConDueno
import kotlinx.coroutines.flow.Flow

/**
 * Acceso a la tabla mascotas.
 * Escrituras -> suspend. Lecturas que actualizan la pantalla -> Flow.
 */
@Dao
interface MascotaDao {

    @Insert
    suspend fun insertar(m: Mascota): Long

    @Insert
    suspend fun insertarTodas(lista: List<Mascota>)

    @Update
    suspend fun actualizar(m: Mascota)

    @Delete
    suspend fun eliminar(m: Mascota)

    // Se usa al registrar una atencion: el peso nuevo de la mascota
    @Query("UPDATE mascotas SET peso = :peso WHERE id = :id")
    suspend fun actualizarPeso(id: Int, peso: Double)

    // Solo las mascotas de un dueño
    @Query("SELECT * FROM mascotas WHERE duenoId = :duenoId ORDER BY nombre")
    fun observarPorDueno(duenoId: Int): Flow<List<Mascota>>

    @Query("SELECT * FROM mascotas WHERE id = :id")
    fun observarUna(id: Int): Flow<Mascota?>

    // Todas las mascotas con su dueño (para Pacientes del veterinario).
    // @Transaction porque Room hace 2 consultas (mascotas + usuarios) y deben ser consistentes
    @Transaction
    @Query("SELECT * FROM mascotas ORDER BY nombre")
    fun observarTodas(): Flow<List<MascotaConDueno>>

    // Si es mayor que 0, la mascota tiene citas y no se puede eliminar
    @Query("SELECT COUNT(*) FROM citas WHERE mascotaId = :mascotaId")
    suspend fun contarCitas(mascotaId: Int): Int
}