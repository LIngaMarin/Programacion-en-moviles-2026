package com.tecsup.huellitasvet.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.huellitasvet.model.Rol
import com.tecsup.huellitasvet.ui.components.BarraInferior
import com.tecsup.huellitasvet.ui.components.OpcionBarra
import com.tecsup.huellitasvet.ui.components.opcionesDueno
import com.tecsup.huellitasvet.ui.components.opcionesVeterinario
import com.tecsup.huellitasvet.ui.dueno.FichaMascotaScreen
import com.tecsup.huellitasvet.ui.dueno.FormularioMascotaScreen
import com.tecsup.huellitasvet.ui.dueno.InicioScreen
import com.tecsup.huellitasvet.ui.dueno.LoginScreen
import com.tecsup.huellitasvet.ui.dueno.MascotasScreen
import com.tecsup.huellitasvet.ui.dueno.MisCitasScreen
import com.tecsup.huellitasvet.ui.dueno.PedirCitaScreen
import com.tecsup.huellitasvet.ui.dueno.PerfilScreen
import com.tecsup.huellitasvet.ui.dueno.SplashScreen
import com.tecsup.huellitasvet.ui.theme.VetTheme
import com.tecsup.huellitasvet.ui.veterinario.ServiciosScreen
import com.tecsup.huellitasvet.viewmodel.InicioViewModel
import com.tecsup.huellitasvet.viewmodel.MascotaViewModel
import com.tecsup.huellitasvet.viewmodel.MisCitasViewModel
import com.tecsup.huellitasvet.viewmodel.PedirCitaViewModel
import com.tecsup.huellitasvet.viewmodel.ServiciosViewModel
import com.tecsup.huellitasvet.viewmodel.SesionViewModel
import com.tecsup.huellitasvet.viewmodel.vetViewModel
import com.tecsup.huellitasvet.ui.veterinario.AgendaScreen
import com.tecsup.huellitasvet.ui.veterinario.PanelScreen
import com.tecsup.huellitasvet.viewmodel.AgendaViewModel
import com.tecsup.huellitasvet.viewmodel.PanelViewModel
import com.tecsup.huellitasvet.ui.veterinario.AtenderCitaScreen
import com.tecsup.huellitasvet.ui.veterinario.PacientesScreen
import com.tecsup.huellitasvet.viewmodel.AtenderCitaViewModel
import com.tecsup.huellitasvet.viewmodel.PacientesViewModel

/**
 * Grafo de navegacion de la app: Splash, Login y un grafo por perfil
 * (Dueño en verde y Veterinario en azul), cada uno con su barra inferior.
 */
@Composable
fun NavVet(navController: NavHostController = rememberNavController()) {
    // Un solo SesionViewModel para toda la app (se crea fuera del NavHost)
    val sesion = vetViewModel { app -> SesionViewModel(app.repository, app.preferencias) }
    val oscuro by sesion.modoOscuro.collectAsState()
    val perfilGuardado by sesion.perfilGuardado.collectAsState()
    val usuario by sesion.usuarioActual.collectAsState()

    NavHost(navController = navController, startDestination = Rutas.SPLASH) {

        composable(Rutas.SPLASH) {
            // Si ya hay un perfil guardado, salta el login y va directo a su pantalla principal
            LaunchedEffect(perfilGuardado) {
                when (perfilGuardado) {
                    Rol.CLIENTE.name -> irAGrafo(navController, Rutas.GRAFO_DUENO)
                    Rol.ADMIN.name -> irAGrafo(navController, Rutas.GRAFO_VETERINARIO)
                }
            }
            VetTheme(rol = Rol.CLIENTE, oscuro = oscuro) {
                SplashScreen(
                    onComenzar = { navController.navigate(Rutas.LOGIN) },
                    onYaTengoCuenta = { navController.navigate(Rutas.LOGIN) }
                )
            }
        }

        composable(Rutas.LOGIN) {
            LoginScreen(
                vm = sesion,
                oscuro = oscuro,
                onIngresar = { rol ->
                    val grafo = if (rol == Rol.CLIENTE) Rutas.GRAFO_DUENO else Rutas.GRAFO_VETERINARIO
                    irAGrafo(navController, grafo)
                }
            )
        }

        // ---------- Grafo del Dueño ----------
        navigation(startDestination = Rutas.INICIO, route = Rutas.GRAFO_DUENO) {
            composable(Rutas.INICIO) {
                PantallaDueno(navController, oscuro, rutaBarra = Rutas.INICIO) {
                    val u = usuario
                    if (u == null) {
                        PantallaPendiente("Cargando…")
                    } else {
                        val vm = vetViewModel(key = "inicio-${u.id}") { app -> InicioViewModel(app.repository, u.id) }
                        InicioScreen(
                            vm = vm,
                            nombre = u.nombre,
                            onVerMascota = { id -> navController.navigate(Rutas.fichaMascota(id)) },
                            onAgregarMascota = { navController.navigate(Rutas.formularioMascota()) },
                            onVerTodas = { irATab(navController, Rutas.MASCOTAS, Rutas.INICIO) },
                            onPedirCita = { navController.navigate(Rutas.pedirCita()) }
                        )
                    }
                }
            }
            composable(Rutas.MASCOTAS) {
                PantallaDueno(navController, oscuro, rutaBarra = Rutas.MASCOTAS) {
                    val u = usuario
                    if (u == null) {
                        PantallaPendiente("Cargando…")
                    } else {
                        val vm = vetViewModel(key = "mascotas-${u.id}") { app -> InicioViewModel(app.repository, u.id) }
                        MascotasScreen(
                            vm = vm,
                            onVerMascota = { id -> navController.navigate(Rutas.fichaMascota(id)) },
                            onAgregar = { navController.navigate(Rutas.formularioMascota()) }
                        )
                    }
                }
            }
            composable(Rutas.MIS_CITAS) {
                PantallaDueno(navController, oscuro, rutaBarra = Rutas.MIS_CITAS) {
                    val u = usuario
                    if (u == null) {
                        PantallaPendiente("Cargando…")
                    } else {
                        val vm = vetViewModel(key = "mis-citas-${u.id}") { app -> MisCitasViewModel(app.repository, u.id) }
                        MisCitasScreen(
                            vm = vm,
                            onPedirCita = { navController.navigate(Rutas.pedirCita()) }
                        )
                    }
                }
            }
            composable(Rutas.PERFIL) {
                PantallaDueno(navController, oscuro, rutaBarra = Rutas.PERFIL) {
                    // TODO (Paso 13): perfil completo
                    PerfilScreen(nombre = usuario?.nombre ?: "", correo = usuario?.correo ?: "")
                }
            }
            // Formulario: sin id = nueva mascota; con id = editar
            composable(
                Rutas.FORMULARIO_MASCOTA,
                arguments = listOf(navArgument("mascotaId") { type = NavType.IntType; defaultValue = -1 })
            ) { entrada ->
                val id = entrada.arguments?.getInt("mascotaId") ?: -1
                val mascotaId = if (id == -1) null else id
                PantallaDueno(navController, oscuro) {
                    val u = usuario
                    if (u == null) {
                        PantallaPendiente("Cargando…")
                    } else {
                        val vm = vetViewModel(key = "form-mascota-$id") { app -> MascotaViewModel(app.repository, mascotaId, u.id) }
                        FormularioMascotaScreen(
                            vm = vm,
                            esEdicion = mascotaId != null,
                            onVolver = { navController.popBackStack() },
                            // Si se elimino, se cierra tambien la ficha de esa mascota
                            onEliminada = { navController.popBackStack(Rutas.FICHA_MASCOTA, inclusive = true) }
                        )
                    }
                }
            }
            composable(
                Rutas.FICHA_MASCOTA,
                arguments = listOf(navArgument("mascotaId") { type = NavType.IntType })
            ) { entrada ->
                val id = entrada.arguments?.getInt("mascotaId") ?: 0
                PantallaDueno(navController, oscuro) {
                    val vm = vetViewModel(key = "ficha-$id") { app -> MascotaViewModel(app.repository, id, usuario?.id ?: 0) }
                    FichaMascotaScreen(
                        vm = vm,
                        onVolver = { navController.popBackStack() },
                        onPedirCita = { navController.navigate(Rutas.pedirCita(id)) },
                        onEditar = { navController.navigate(Rutas.formularioMascota(id)) }
                    )
                }
            }
            // Pedir cita: puede llegar con una mascota ya elegida (desde su ficha)
            composable(
                Rutas.PEDIR_CITA,
                arguments = listOf(navArgument("mascotaId") { type = NavType.IntType; defaultValue = -1 })
            ) { entrada ->
                val id = entrada.arguments?.getInt("mascotaId") ?: -1
                PantallaDueno(navController, oscuro) {
                    val u = usuario
                    if (u == null) {
                        PantallaPendiente("Cargando…")
                    } else {
                        val vm = vetViewModel(key = "pedir-cita-$id") { app ->
                            PedirCitaViewModel(app.repository, u.id, if (id == -1) null else id)
                        }
                        PedirCitaScreen(
                            vm = vm,
                            onVolver = { navController.popBackStack() },
                            // Al crear la cita vuelve a Mis citas (ahi aparece como PENDIENTE)
                            onCreada = {
                                // Ir a Mis citas sin restaurar la pila guardada (asi no vuelve a Pedir cita)
                                navController.navigate(Rutas.MIS_CITAS) {
                                    popUpTo(Rutas.INICIO)
                                    launchSingleTop = true
                                }
                            }                        )
                    }
                }
            }
        }

        // ---------- Grafo del Veterinario ----------
        navigation(startDestination = Rutas.PANEL, route = Rutas.GRAFO_VETERINARIO) {
            composable(Rutas.PANEL) {
                PantallaVeterinario(navController, oscuro, rutaBarra = Rutas.PANEL) {
                    val vm = vetViewModel { app -> PanelViewModel(app.repository) }
                    PanelScreen(
                        vm = vm,
                        onVerPendientes = { irATab(navController, Rutas.AGENDA, Rutas.PANEL) }
                    )
                }
            }
            composable(Rutas.AGENDA) {
                PantallaVeterinario(navController, oscuro, rutaBarra = Rutas.AGENDA) {
                    val vm = vetViewModel { app -> AgendaViewModel(app.repository) }
                    AgendaScreen(
                        vm = vm,
                        onAtender = { id -> navController.navigate(Rutas.atenderCita(id)) }                    )
                }
            }
            composable(Rutas.PACIENTES) {
                PantallaVeterinario(navController, oscuro, rutaBarra = Rutas.PACIENTES) {
                    val vm = vetViewModel { app -> PacientesViewModel(app.repository) }
                    PacientesScreen(
                        vm = vm,
                        onVerPaciente = { id -> navController.navigate(Rutas.fichaPaciente(id)) }
                    )
                }
            }
            // Ficha del paciente: la misma ficha del dueño, pero sin "Pedir cita" ni "Editar"
            composable(
                Rutas.FICHA_PACIENTE,
                arguments = listOf(navArgument("mascotaId") { type = NavType.IntType })
            ) { entrada ->
                val id = entrada.arguments?.getInt("mascotaId") ?: 0
                PantallaVeterinario(navController, oscuro) {
                    val vm = vetViewModel(key = "paciente-$id") { app -> MascotaViewModel(app.repository, id, 0) }
                    FichaMascotaScreen(vm = vm, onVolver = { navController.popBackStack() })
                }
            }
            composable(
                Rutas.ATENDER_CITA,
                arguments = listOf(navArgument("citaId") { type = NavType.IntType })
            ) { entrada ->
                val id = entrada.arguments?.getInt("citaId") ?: 0
                PantallaVeterinario(navController, oscuro) {
                    val vm = vetViewModel(key = "atender-$id") { app -> AtenderCitaViewModel(app.repository, id) }
                    AtenderCitaScreen(vm = vm, onVolver = { navController.popBackStack() })
                }
            }
            composable(Rutas.SERVICIOS) {
                PantallaVeterinario(navController, oscuro, rutaBarra = Rutas.SERVICIOS) {
                    val vm = vetViewModel { app -> ServiciosViewModel(app.repository) }
                    ServiciosScreen(
                        vm = vm,
                        onEditar = { /* TODO (Paso 13): abrir el formulario de servicio */ },
                        onNuevo = { /* TODO (Paso 13): abrir el formulario de servicio */ }
                    )
                }
            }
        }
    }
}

/** Navega al grafo de un perfil y limpia toda la pila: con "atras" no se vuelve al login. */
private fun irAGrafo(navController: NavHostController, grafo: String) {
    navController.navigate(grafo) {
        popUpTo(navController.graph.id) { inclusive = true }
        launchSingleTop = true
    }
}

/** Cambia de opcion de la barra inferior sin apilar pantallas repetidas. */
private fun irATab(navController: NavHostController, ruta: String, rutaInicio: String) {
    navController.navigate(ruta) {
        popUpTo(rutaInicio) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

/** Pantalla del dueño: tema verde y, si rutaBarra no es null, la barra inferior de 4 opciones. */
@Composable
private fun PantallaDueno(
    navController: NavHostController,
    oscuro: Boolean,
    rutaBarra: String? = null,
    contenido: @Composable () -> Unit
) {
    VetTheme(rol = Rol.CLIENTE, oscuro = oscuro) {
        if (rutaBarra == null) contenido()
        else ConBarra(navController, opcionesDueno, rutaBarra, contenido)
    }
}

/** Pantalla del veterinario: tema azul y, si rutaBarra no es null, la barra inferior de 5 opciones. */
@Composable
private fun PantallaVeterinario(
    navController: NavHostController,
    oscuro: Boolean,
    rutaBarra: String? = null,
    contenido: @Composable () -> Unit
) {
    VetTheme(rol = Rol.ADMIN, oscuro = oscuro) {
        if (rutaBarra == null) contenido()
        else ConBarra(navController, opcionesVeterinario, rutaBarra, contenido)
    }
}

/** Scaffold con la NavigationBar del perfil y el contenido de la pantalla encima. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ConBarra(
    navController: NavHostController,
    opciones: List<OpcionBarra>,
    rutaActual: String,
    contenido: @Composable () -> Unit
) {
    Scaffold(
        bottomBar = {
            BarraInferior(opciones = opciones, rutaActual = rutaActual, onNavegar = { ruta ->
                // Solo navega si esa pantalla ya existe en el grafo (las demas se agregan en los siguientes pasos)
                val grafo = navController.currentBackStackEntry?.destination?.parent
                if (grafo?.findNode(ruta) != null) {
                    irATab(navController, ruta, opciones.first().ruta)
                }
            })
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
        ) {
            contenido()
        }
    }
}

/** Texto temporal para las pantallas que se hacen en los siguientes pasos. */
@Composable
private fun PantallaPendiente(texto: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(texto, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}