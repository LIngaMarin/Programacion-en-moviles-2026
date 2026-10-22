package com.tecsup.huellitasvet.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.tecsup.huellitasvet.data.local.entity.Vacuna
import kotlinx.coroutines.flow.Flow

/**
 * Acceso a la tabla vacunas.
 * Escrituras -> suspend. Lecturas que actualizan la pantalla -> Flow.
 */
@Dao
interface VacunaDao {

    @Query("SELECT * FROM vacunas WHERE mascotaId = :mascotaId ORDER BY fecha DESC, id DESC")
    fun observarPorMascota(mascotaId: Int): Flow<List<Vacuna>>

    @Insert
    suspend fun insertar(v: Vacuna): Long

    @Insert
    suspend fun insertarTodas(lista: List<Vacuna>)

    @Delete
    suspend fun eliminar(v: Vacuna)
}
