package com.tecsup.huellitasvet.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.huellitasvet.VetApp

/**
 * ENTREGADO. Fabrica de ViewModels: permite crear un ViewModel que recibe parametros
 * en su constructor (el repository, un id, etc.).
 */
class VetViewModelFactory(private val crear: () -> ViewModel) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = crear() as T
}

/**
 * Crea (o recupera si ya existe) un ViewModel con acceso a VetApp (repository, preferencias).
 * Uso:  val vm = vetViewModel { app -> ServiciosViewModel(app.repository) }
 * key: usala cuando el ViewModel depende de un id (ej. "mascota-3").
 */
@Composable
inline fun <reified VM : ViewModel> vetViewModel(
    key: String? = null,
    noinline crear: (VetApp) -> VM
): VM {
    val app = LocalContext.current.applicationContext as VetApp
    return viewModel(key = key, factory = VetViewModelFactory { crear(app) })
}
