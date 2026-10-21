package com.tecsup.huellitasvet.data.prefs

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tecsup.huellitasvet.model.Rol
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Se declara UNA sola vez, a nivel de archivo: crea el archivo de preferencias "ajustes"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ajustes")

/**
 * Preferencias pequeñas del dispositivo (clave-valor) con DataStore.
 * Las mascotas, citas y servicios NO van aqui: van en Room.
 */
class PreferenciasStore(private val context: Context) {

    // Claves de cada preferencia
    private object Claves {
        val PERFIL_ACTIVO = stringPreferencesKey("perfilActivo")
        val USUARIO_ID = intPreferencesKey("usuarioId")
        val MODO_OSCURO = booleanPreferencesKey("modoOscuro")
        val NOTIFICACIONES = booleanPreferencesKey("notificaciones")
        val ACEPTAR_CITAS = booleanPreferencesKey("aceptarCitas")
    }

    // ---------- Lectura: un Flow por cada preferencia ----------
    /** CLIENTE, ADMIN o "" (vacio = no hay sesion) */
    val perfilActivo: Flow<String> = context.dataStore.data.map { it[Claves.PERFIL_ACTIVO] ?: "" }

    val usuarioId: Flow<Int> = context.dataStore.data.map { it[Claves.USUARIO_ID] ?: 0 }

    val modoOscuro: Flow<Boolean> = context.dataStore.data.map { it[Claves.MODO_OSCURO] ?: false }

    val notificaciones: Flow<Boolean> = context.dataStore.data.map { it[Claves.NOTIFICACIONES] ?: true }

    val aceptarCitas: Flow<Boolean> = context.dataStore.data.map { it[Claves.ACEPTAR_CITAS] ?: true }

    // ---------- Escritura: funciones suspend ----------
    suspend fun guardarSesion(rol: Rol, usuarioId: Int) {
        context.dataStore.edit {
            it[Claves.PERFIL_ACTIVO] = rol.name
            it[Claves.USUARIO_ID] = usuarioId
        }
    }

    suspend fun guardarModoOscuro(activo: Boolean) {
        context.dataStore.edit { it[Claves.MODO_OSCURO] = activo }
    }

    suspend fun guardarNotificaciones(activo: Boolean) {
        context.dataStore.edit { it[Claves.NOTIFICACIONES] = activo }
    }

    suspend fun guardarAceptarCitas(activo: Boolean) {
        context.dataStore.edit { it[Claves.ACEPTAR_CITAS] = activo }
    }

    // Borra solo la sesion; el modo oscuro y los demas ajustes se mantienen
    suspend fun cerrarSesion() {
        context.dataStore.edit {
            it.remove(Claves.PERFIL_ACTIVO)
            it.remove(Claves.USUARIO_ID)
        }
    }
}