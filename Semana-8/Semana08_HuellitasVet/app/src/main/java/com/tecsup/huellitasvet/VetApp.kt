package com.tecsup.huellitasvet

import android.app.Application
import com.tecsup.huellitasvet.data.local.DatosIniciales
import com.tecsup.huellitasvet.data.local.VetDatabase
import com.tecsup.huellitasvet.data.prefs.PreferenciasStore
import com.tecsup.huellitasvet.data.repository.VetRepository
import com.tecsup.huellitasvet.data.repository.VetRepositoryRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Application de la app (registrada en el AndroidManifest con android:name).
 * Crea una sola vez la base de datos, el repository y las preferencias que usan los ViewModels.
 */
class VetApp : Application() {

    lateinit var db: VetDatabase
        private set
    lateinit var repository: VetRepository
        private set
    lateinit var preferencias: PreferenciasStore
        private set

    override fun onCreate() {
        super.onCreate()
        db = VetDatabase.obtener(this)
        repository = VetRepositoryRoom(db)
        preferencias = PreferenciasStore(this)

        // Siembra los datos de prueba en segundo plano (Dispatchers.IO), sin bloquear la pantalla
        CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
            DatosIniciales.sembrar(db)
        }
    }
}