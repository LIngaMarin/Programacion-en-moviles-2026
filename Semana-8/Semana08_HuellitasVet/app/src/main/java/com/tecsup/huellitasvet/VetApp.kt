package com.tecsup.huellitasvet

import android.app.Application
import com.tecsup.huellitasvet.data.local.DatosIniciales
import com.tecsup.huellitasvet.data.local.VetDatabase
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.data.repository.VetRepositoryRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * GUIADO. Application de la app (registrada en el AndroidManifest con android:name).
 * Crea una sola vez la base de datos y el repository que usan todos los ViewModels.
 */
class VetApp : Application() {

    lateinit var db: VetDatabase
        private set
    lateinit var repository: VetRepository
        private set
    // TODO (Paso 5): crear aqui PreferenciasStore para que la usen los ViewModels

    override fun onCreate() {
        super.onCreate()
        db = VetDatabase.obtener(this)
        repository = VetRepositoryRoom(db)

        // Siembra los datos de prueba en segundo plano (Dispatchers.IO), sin bloquear la pantalla
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            DatosIniciales.sembrar(db)
        }
    }
}
