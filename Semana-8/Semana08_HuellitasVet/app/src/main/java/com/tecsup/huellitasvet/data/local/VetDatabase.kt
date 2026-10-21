package com.tecsup.huellitasvet.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tecsup.huellitasvet.data.local.dao.ServicioDao
import com.tecsup.huellitasvet.data.local.entity.Servicio

/**
 * GUIADO. Base de datos Room de la app (archivo huellitasvet.db).
 * Por ahora solo tiene la tabla servicios (ejemplo resuelto).
 */
@Database(
    entities = [Servicio::class /* TODO (Paso 3): agrega las otras 3 entidades */],
    version = 1,
    exportSchema = false
)
abstract class VetDatabase : RoomDatabase() {

    abstract fun servicioDao(): ServicioDao
    // TODO (Paso 3): usuarioDao(), mascotaDao() y citaDao()

    companion object {
        // @Volatile: todos los hilos ven siempre el mismo valor de INSTANCE
        @Volatile
        private var INSTANCE: VetDatabase? = null

        /** Singleton: una sola instancia de la base de datos para toda la app. */
        fun obtener(context: Context): VetDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                VetDatabase::class.java,
                "huellitasvet.db"
            ).build().also { INSTANCE = it }
        }
    }
}
