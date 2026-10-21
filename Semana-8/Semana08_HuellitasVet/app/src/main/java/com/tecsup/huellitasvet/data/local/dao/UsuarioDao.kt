package com.tecsup.huellitasvet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.tecsup.huellitasvet.data.local.entity.Usuario

/** Acceso a la tabla usuarios. */
@Dao
interface UsuarioDao {

    // Devuelve el usuario si el correo y la clave coinciden; si no, null
    @Query("SELECT * FROM usuarios WHERE correo = :correo AND clave = :clave LIMIT 1")
    suspend fun login(correo: String, clave: String): Usuario?

    // Para recuperar al usuario de la sesion guardada en DataStore (solo se guarda su id)
    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun obtener(id: Int): Usuario?

    @Insert
    suspend fun insertarTodos(lista: List<Usuario>)

    // Se usa en DatosIniciales: si es 0, la base esta vacia y se siembran los datos
    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int
}