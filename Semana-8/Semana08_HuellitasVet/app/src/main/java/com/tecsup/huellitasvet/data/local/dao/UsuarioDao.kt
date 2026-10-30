package com.tecsup.huellitasvet.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.tecsup.huellitasvet.data.local.entity.Usuario
import kotlinx.coroutines.flow.Flow

/** Acceso a la tabla usuarios. */
@Dao
interface UsuarioDao {

    // Devuelve el usuario si el correo y la clave coinciden; si no, null
    @Query("SELECT * FROM usuarios WHERE correo = :correo AND clave = :clave LIMIT 1")
    suspend fun login(correo: String, clave: String): Usuario?

    // Para recuperar al usuario de la sesion guardada en DataStore (solo se guarda su id)
    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun obtener(id: Int): Usuario?


    // Para "Cambiar de perfil": el primer usuario del otro rol (modo practica)
    @Query("SELECT * FROM usuarios WHERE rol = :rol ORDER BY id LIMIT 1")
    suspend fun obtenerPorRol(rol: String): Usuario?

    @Insert
    suspend fun insertarTodos(lista: List<Usuario>)

    // Registro: inserta un usuario nuevo y devuelve el id que Room le genero
    @Insert
    suspend fun insertar(usuario: Usuario): Long

    // Registro: cuantos usuarios tienen ese correo (0 = el correo esta libre)
    @Query("SELECT COUNT(*) FROM usuarios WHERE correo = :correo")
    suspend fun contarPorCorreo(correo: String): Int

    // Se usa en DatosIniciales: si es 0, la base esta vacia y se siembran los datos
    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contar(): Int

    // ---------- Administrador ----------
    // Dueños y veterinarios (los administradores no se listan): primero por rol y luego por nombre
    @Query("SELECT * FROM usuarios WHERE rol != 'ADMINISTRADOR' ORDER BY rol, nombre")
    fun observarGestionables(): Flow<List<Usuario>>

    // Cuantos usuarios hay de un rol (para no dejar la clinica sin veterinarios)
    @Query("SELECT COUNT(*) FROM usuarios WHERE rol = :rol")
    suspend fun contarPorRol(rol: String): Int

    @Delete
    suspend fun eliminar(u: Usuario)
}
