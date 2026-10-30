package com.tecsup.huellitasvet.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.tecsup.huellitasvet.data.local.entity.TipoServicio
import kotlinx.coroutines.flow.Flow

/**
 * Acceso a la tabla tipos_servicio.
 * Para editar se usa @Update (no REPLACE): REPLACE borra la fila y sus servicios quedarian sin tipo.
 */
@Dao
interface TipoServicioDao {

    @Query("SELECT * FROM tipos_servicio ORDER BY nombre")
    fun observarTodos(): Flow<List<TipoServicio>>

    @Insert
    suspend fun insertar(t: TipoServicio): Long

    @Insert
    suspend fun insertarTodos(lista: List<TipoServicio>)

    @Update
    suspend fun actualizar(t: TipoServicio)

    // Al eliminar, la ForeignKey (SET_NULL) deja sus servicios "Sin tipo"
    @Delete
    suspend fun eliminar(t: TipoServicio)

    // Evita nombres repetidos sin importar mayusculas; exceptoId para no chocar consigo mismo al editar
    @Query("SELECT COUNT(*) FROM tipos_servicio WHERE LOWER(nombre) = LOWER(:nombre) AND id != :exceptoId")
    suspend fun contarPorNombre(nombre: String, exceptoId: Int): Int
}
