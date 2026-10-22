# Prompts utilizados

Herramienta: **Gemini en Android Studio (modo Agente)**, modelo Gemini 3.6 Flash. En la mejora 3 la cuota de ese modelo se agotó y se cambió a otro modelo disponible para terminar.

Rama: `mejora-ia-huellitas` (creada desde `main` con la Fase 1 completa).

---

## Prompt 1 – Gráfico de peso de la mascota

**Texto enviado a Gemini:**

```text
Tengo una app Android en Kotlin con Jetpack Compose, Material 3, MVVM, Room y un VetRepository (interface).
Paquete: com.tecsup.huellitasvet

Quiero agregar en la ficha de la mascota un grafico de linea con el peso registrado en cada cita ATENDIDA.

Lo que ya existe:
- Entidad Cita (tabla "citas") con: id, mascotaId, fecha (String 'aaaa-MM-dd'), hora (String 'HH:mm'), estado (String: PENDIENTE, CONFIRMADA, ATENDIDA, CANCELADA), pesoRegistrado (Double?).
- data/local/dao/CitaDao.kt
- data/repository/VetRepository.kt (interface) y VetRepositoryRoom.kt (implementacion con Room)
- viewmodel/MascotaViewModel.kt: recibe (repo: VetRepository, mascotaId: Int?, duenoId: Int) y expone mascota y historial como StateFlow con stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ...)
- ui/dueno/FichaMascotaScreen.kt: tiene pestañas "Datos" e "Historial clínico" y usa vm.historial
- En ui/components/graficos ya existen DatoGrafico, GraficoDona y GraficoBarras (hechos con Canvas)

Lo que necesito, respetando MVVM:
1. Una data class PuntoPeso(fecha: String, peso: Double) en el paquete model.
2. En CitaDao una consulta que devuelva Flow<List<PuntoPeso>> con la fecha y el pesoRegistrado de las citas ATENDIDAS de una mascota, sin pesos nulos, ordenadas de la mas antigua a la mas reciente.
3. La funcion observarPesos(mascotaId: Int): Flow<List<PuntoPeso>> declarada primero en la interface VetRepository y luego implementada en VetRepositoryRoom.
4. En MascotaViewModel un val pesos: StateFlow<List<PuntoPeso>> (lista vacia si mascotaId es null).
5. Un Composable GraficoLineas(puntos: List<PuntoPeso>) en ui/components/graficos, dibujado con Canvas: linea que une los pesos, un circulo en cada punto, el peso encima de cada punto y la fecha corta debajo. Usa MaterialTheme.colorScheme.primary, sin colores fijos.
6. En FichaMascotaScreen, dentro de la pestaña "Historial clínico" y encima de la lista, una tarjeta "Evolución del peso" con el GraficoLineas. Solo se muestra si hay 2 o mas puntos.

Reglas: la View no usa DAO ni Repository, el ViewModel solo conoce la interface VetRepository. No uses XML ni librerias externas. No cambies la version de la base de datos (no hay entidades nuevas).
Muestrame solo los archivos que cambian, completos.
```

**Qué generó la IA:** creó `model/PuntoPeso.kt` y `ui/components/graficos/GraficoLineas.kt` (Canvas con línea, círculos, peso arriba y fecha abajo, usando `TextMeasurer`), agregó `observarPesos` en `CitaDao` con `pesoRegistrado AS peso`, la declaró en `VetRepository`, la implementó en `VetRepositoryRoom`, expuso `pesos` como `StateFlow` en `MascotaViewModel` y agregó la tarjeta "Evolución del peso" en la pestaña Historial de `FichaMascotaScreen`.

**Qué tuve que corregir yo:** nada en el código. Al inicio Gemini no encontró `CitaDao.kt` ("File not found") y tuvo que buscarlo por su cuenta. También agregó un `try/catch` innecesario alrededor de `diaMes()` (las fechas siempre vienen en formato válido); se dejó porque no afecta.

**¿Respeta MVVM?:** Sí. La función se declaró primero en la interface, el ViewModel solo usa `VetRepository` y la pantalla solo observa `vm.pesos` con `collectAsState()`.

---

## Prompt 2 – Reprogramar cita

**Texto enviado a Gemini:**

```text
Tengo una app Android en Kotlin con Jetpack Compose, Material 3, MVVM, Room y un VetRepository (interface).
Paquete: com.tecsup.huellitasvet

Quiero que el dueño pueda REPROGRAMAR una cita (cambiar su fecha y hora).

Lo que ya existe:
- Entidad Cita (tabla "citas"): id, mascotaId, servicioId, fecha (String 'aaaa-MM-dd'), hora (String 'HH:mm'), estado (String: PENDIENTE, CONFIRMADA, ATENDIDA, CANCELADA), precio.
- CitaDao con observarHorasOcupadas(fecha): Flow<List<String>> (horas de citas no canceladas de ese dia) y observarUna(id): Flow<CitaConDetalle?>.
- VetRepository (interface) y VetRepositoryRoom: ya tienen observarHorasOcupadas(fecha) y observarCita(id).
- model/Horarios.kt: Horarios.delDia es la lista de 18 horarios de 09:00 a 17:30.
- ui/components: FechaChip(fecha: LocalDate, seleccionado, onClick), HoraChip(hora, seleccionado, ocupado, onClick, modifier), AppTopBar(titulo, onAtras), VetButton(texto, onClick, habilitado = ...), fechaCorta(iso), AccionTexto(texto, onClick, peligro).
- viewmodel/MisCitasViewModel.kt: expone proximas: StateFlow<List<CitaItem>> donde CitaItem(detalle: CitaConDetalle, puedeCancelar: Boolean). puedeCancelar es true si la cita es PENDIENTE o CONFIRMADA y faltan mas de 2 horas (Duration.between).
- ui/dueno/MisCitasScreen.kt: cada tarjeta (CitaCard) tiene en su slot "accion" un AccionTexto("Cancelar") cuando puedeCancelar es true.
- ui/dueno/PedirCitaScreen.kt y viewmodel/PedirCitaViewModel.kt: ya resuelven elegir fecha (hoy a 7 dias con FechaChip) y hora (HoraChip, ocupadas y pasadas en gris usando flatMapLatest y combine). Usalos como referencia de estilo.
- ui/navigation/Rutas.kt (object con constantes y funciones) y ui/navigation/NavVet.kt (grafo del dueño; cada pantalla se envuelve en PantallaDueno(navController, oscuro) { ... } y los ViewModels se crean con vetViewModel(key = ...) { app -> MiViewModel(app.repository, ...) }).

Lo que necesito, respetando MVVM:
1. En CitaDao una funcion suspend reprogramar(id, fecha, hora) que cambie fecha y hora y deje la cita en estado PENDIENTE (para que el veterinario la vuelva a confirmar).
2. suspend fun reprogramarCita(citaId: Int, fecha: String, hora: String) declarada primero en la interface VetRepository e implementada en VetRepositoryRoom.
3. Un ReprogramarCitaViewModel(repo, citaId) con: la cita actual, fecha elegida (por defecto la fecha actual de la cita), hora elegida, horas no disponibles (ocupadas de ese dia + pasadas si es hoy; la hora actual de ESTA misma cita no cuenta como ocupada) y una funcion confirmar(onListo). Reglas: solo PENDIENTE o CONFIRMADA, solo si faltan mas de 2 horas para la cita actual, la nueva fecha entre hoy y 7 dias, y antes de guardar vuelve a consultar que el horario siga libre. Errores en un StateFlow mensaje.
4. Una pantalla ReprogramarCitaScreen en ui/dueno: muestra la cita actual (mascota, servicio, fecha y hora), los chips de fechas, la grilla de horas de 4 columnas y el boton "Guardar nuevo horario".
5. Ruta REPROGRAMAR_CITA = "reprogramar_cita/{citaId}" y la funcion reprogramarCita(citaId) en Rutas, y su composable en el grafo del dueño en NavVet (sin barra inferior). Al guardar vuelve atras.
6. En MisCitasScreen, junto a "Cancelar", un AccionTexto("Reprogramar") (no rojo) que solo aparece cuando puedeCancelar es true y navega a la nueva pantalla. MisCitasScreen recibe un nuevo parametro onReprogramar: (Int) -> Unit.

Reglas: la View no usa DAO ni Repository; el ViewModel solo conoce la interface VetRepository; las reglas de negocio van en el ViewModel. No uses XML ni librerias externas. No cambies la version de la base de datos (no hay entidades nuevas).
```

**Qué generó la IA:** `CitaDao.reprogramar` (cambia fecha y hora y deja la cita en PENDIENTE), `reprogramarCita` en la interface y en `VetRepositoryRoom`, `ReprogramarCitaViewModel` con todas las reglas (estado, 2 horas con `Duration`, fechas de hoy a 7 días, horas ocupadas y pasadas con `flatMapLatest` y `combine`, sin contar la hora de la misma cita, y nueva consulta antes de guardar), `ReprogramarCitaScreen`, la ruta en `Rutas`, el destino en `NavVet` y el botón "Reprogramar" en `MisCitasScreen`.

**Qué tuve que corregir yo:** un error de lógica. Al abrir la pantalla, el ViewModel ya marcaba la misma fecha y hora de la cita, así que el botón "Guardar" quedaba activo: si se tocaba sin cambiar nada, la cita "se reprogramaba" al mismo horario y pasaba de CONFIRMADA a PENDIENTE. Agregué en `confirmar()` la validación "Elige un horario distinto al actual".

**¿Respeta MVVM?:** Sí. Las reglas viven en el ViewModel, la función nueva se declaró primero en la interface y la pantalla solo observa los `StateFlow` y avisa los clics.

---

## Prompt 3 – Carnet de vacunas

**Texto enviado a Gemini:**

```text
Tengo una app Android en Kotlin con Jetpack Compose, Material 3, MVVM, Room y un VetRepository (interface).
Paquete: com.tecsup.huellitasvet

Quiero agregar un CARNET DE VACUNAS por mascota, visible en la ficha de la mascota.

Lo que ya existe:
- data/local/entity: Servicio, Usuario, Mascota (id, duenoId, nombre, especie, raza, anioNacimiento, peso, sexo) y Cita. Las FK se declaran en @Entity con foreignKeys = [ForeignKey(...)] e indices = [Index("...")].
- data/local/dao: ServicioDao, UsuarioDao, MascotaDao, CitaDao (lecturas con Flow, escrituras con suspend).
- data/local/VetDatabase.kt: @Database(entities = [Servicio, Usuario, Mascota, Cita], version = 1, exportSchema = false), singleton con Room.databaseBuilder(..., "huellitasvet.db").
- data/local/DatosIniciales.kt: siembra datos solo si usuarioDao().contar() == 0. Las mascotas tienen id fijo: 1 Luna (gato), 2 Max (perro), 3 Rocky (perro), 4 Michi (gato). Usa LocalDate.now().plusDays(n).toString() para fechas.
- data/repository/VetRepository.kt (interface) y VetRepositoryRoom.kt.
- viewmodel/MascotaViewModel.kt: recibe (repo, mascotaId: Int?, duenoId: Int) y expone mascota, historial y pesos como StateFlow con stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ...).
- ui/dueno/FichaMascotaScreen.kt: FichaMascotaScreen(vm, onVolver, onPedirCita: (() -> Unit)? = null, onEditar: (() -> Unit)? = null). Tiene un selector de pestañas propio (Pestanas) con "Datos" e "Historial clínico". El dueño la abre con onPedirCita y onEditar; el veterinario la abre desde Pacientes sin esos botones (ruta FICHA_PACIENTE en ui/navigation/NavVet.kt).
- ui/components: VetTextField(valor, onValorCambia, etiqueta, error = ...), VetButton(texto, onClick, ...), fechaCorta(iso), diaMes(iso). Los colores salen de MaterialTheme.colorScheme y de ui/theme/Color.kt (EstadoConfirmada verde, EstadoCancelada rojo).

Lo que necesito, respetando MVVM:
1. Entidad Vacuna (tabla "vacunas"): id (PK autogenerada), mascotaId (ForeignKey hacia Mascota con su Index), nombre (String), fecha (String 'aaaa-MM-dd', fecha de aplicacion) y proximaDosis (String? 'aaaa-MM-dd', puede ser nula).
2. VacunaDao: observarPorMascota(mascotaId): Flow<List<Vacuna>> ordenada de la mas reciente a la mas antigua, insertar(v) e insertarTodas(lista) suspend, eliminar(v) suspend.
3. En VetDatabase: agregar Vacuna a entities, abstract fun vacunaDao(), subir version a 2 y en el builder usar fallbackToDestructiveMigration(dropAllTables = true) (es una app de practica: al cambiar la version se recrea la base y se vuelven a sembrar los datos).
4. En DatosIniciales: sembrar unas 5 vacunas de ejemplo para Luna, Max y Rocky (algunas con proxima dosis futura y una con proxima dosis ya vencida).
5. Declarar primero en la interface VetRepository: observarVacunas(mascotaId): Flow<List<Vacuna>>, suspend agregarVacuna(v: Vacuna) y suspend eliminarVacuna(v: Vacuna); luego implementarlas en VetRepositoryRoom.
6. En MascotaViewModel: val vacunas: StateFlow<List<Vacuna>> y fun agregarVacuna(nombre, fecha, proximaDosis, onListo) con validaciones: nombre de al menos 3 caracteres, fecha valida (LocalDate.parse) que no sea futura, y proximaDosis opcional pero si viene debe ser valida y posterior a la fecha. Errores expuestos en un StateFlow. Tambien fun eliminarVacuna(v).
7. En FichaMascotaScreen: una tercera pestaña "Vacunas" con el carnet: cada vacuna en una tarjeta con nombre, fecha de aplicacion y proxima dosis, y una etiqueta "Al día" (verde) o "Vencida" (roja) si la proxima dosis ya paso. La regla de "vencida" se calcula en el ViewModel, no en la View. Si no tiene vacunas, mostrar "Aún no tiene vacunas registradas".
8. Solo el veterinario puede agregar vacunas: FichaMascotaScreen recibe un nuevo parametro onAgregarVacuna habilitado con un booleano puedeAgregarVacuna: Boolean = false. Si es true, en la pestaña Vacunas sale un boton "Agregar vacuna" que abre un AlertDialog con los campos nombre, fecha y proxima dosis (texto 'aaaa-MM-dd'). En NavVet, solo la ruta FICHA_PACIENTE (veterinario) pasa puedeAgregarVacuna = true.

Reglas: la View no usa DAO ni Repository; el ViewModel solo conoce la interface VetRepository; las reglas de negocio van en el ViewModel. No uses XML ni librerias externas.
```

**Qué generó la IA:** la entidad `Vacuna` (FK a `Mascota` con su `Index`), `VacunaDao`, la base de datos en **versión 2** con `fallbackToDestructiveMigration`, 5 vacunas de ejemplo (una vencida), las funciones en la interface y en `VetRepositoryRoom`, en `MascotaViewModel` el `StateFlow` de vacunas, `agregarVacuna` con validaciones y `esVacunaVencida`, y en `FichaMascotaScreen` la pestaña "Vacunas" con etiquetas "Al día" / "Vencida" y el diálogo para agregar (solo para el veterinario).

**Qué tuve que corregir yo:** la cuota del modelo Gemini 3.6 Flash se agotó justo al final ("RESOURCE_EXHAUSTED") y la respuesta se quedó reintentando. Revisé con `git diff` que los 9 archivos estuvieran completos (lo estaban y compilaban) y cambié de modelo para que terminara su resumen. Como observación, pasó el ViewModel completo a los composables internos (`Vacunas`, `AgregarVacunaDialog`) en vez de solo los datos y lambdas; no rompe MVVM, pero es menos limpio. También agregó un botón para eliminar vacunas que no se pidió.

**¿Respeta MVVM?:** Sí. La regla de "vencida" y las validaciones están en el ViewModel, las funciones nuevas se declararon primero en la interface y se subió la versión de la base por la entidad nueva.

---

## Prompt 4 – Calendario mensual en la agenda

**Texto enviado a Gemini:**

```text
Tengo una app Android en Kotlin con Jetpack Compose, Material 3, MVVM, Room y un VetRepository (interface).
Paquete: com.tecsup.huellitasvet

Quiero agregar una VISTA DE MES (calendario mensual) en la agenda del veterinario, con un punto en los dias que tienen citas.

Lo que ya existe:
- Entidad Cita (tabla "citas"): fecha (String 'aaaa-MM-dd'), hora (String 'HH:mm'), estado (String: PENDIENTE, CONFIRMADA, ATENDIDA, CANCELADA).
- data/local/dao/CitaDao.kt (lecturas con Flow, escrituras con suspend) y observarPorFecha(fecha): Flow<List<CitaConDetalle>>.
- data/repository/VetRepository.kt (interface) y VetRepositoryRoom.kt.
- viewmodel/AgendaViewModel.kt: fechas (hoy y los 6 dias siguientes), fechaSeleccionada: StateFlow<LocalDate>, citas: StateFlow<List<CitaConDetalle>> del dia elegido (con flatMapLatest), elegirFecha(f), confirmar(c) y cancelar(c).
- ui/veterinario/AgendaScreen.kt: AppTopBar("Agenda"), una fila (LazyRow) de FechaChip con los proximos dias y debajo la linea de tiempo de citas del dia.
- ui/components/Formatos.kt tiene funciones en español (diaCorto, fechaCorta) con listas propias de dias y meses; no uses Locale para los nombres.
- Colores: solo MaterialTheme.colorScheme (primary, onPrimary, primaryContainer, onSurfaceVariant...).

Lo que necesito, respetando MVVM:
1. En CitaDao una consulta Flow<List<String>> con las fechas DISTINTAS que tienen citas no canceladas entre dos fechas (inicio y fin del mes).
2. fun observarFechasConCitas(inicio: String, fin: String): Flow<List<String>> declarada primero en la interface VetRepository e implementada en VetRepositoryRoom.
3. En AgendaViewModel: mesVisible: StateFlow<YearMonth> (por defecto el mes actual), diasConCitas: StateFlow<Set<LocalDate>> del mes visible (con flatMapLatest), y funciones mesAnterior() y mesSiguiente(). Al tocar un dia del calendario se usa el elegirFecha(f) que ya existe.
4. Un Composable CalendarioMensual en ui/components: cabecera con flechas "<" y ">" y el nombre del mes en español ("Octubre 2026"), fila de iniciales L M M J V S D (la semana empieza en lunes), y la grilla de dias del mes hecha con Column y Row (no uses LazyVerticalGrid porque va dentro de un LazyColumn). El dia seleccionado va en un circulo color primary, el dia de hoy con borde, y los dias con citas llevan un punto pequeño debajo.
5. En AgendaScreen: un selector de dos opciones "Semana" / "Mes" debajo de la barra superior. En "Semana" se ve lo de ahora (los FechaChip). En "Mes" se ve el CalendarioMensual. En ambos casos, debajo sigue la lista de citas del dia elegido.

Reglas: la View no usa DAO ni Repository; el ViewModel solo conoce la interface VetRepository; las reglas van en el ViewModel. No uses XML ni librerias externas. No cambies la version de la base de datos (no hay entidades nuevas).
```

**Qué generó la IA:** la consulta `SELECT DISTINCT fecha … BETWEEN` (sin canceladas) en `CitaDao`, `observarFechasConCitas` en la interface y en `VetRepositoryRoom`, en `AgendaViewModel` el `mesVisible` (`YearMonth`), `diasConCitas` con `flatMapLatest` y las funciones `mesAnterior()` y `mesSiguiente()`, el componente `CalendarioMensual` (grilla con `Column`/`Row`, semana desde el lunes, día elegido en círculo, hoy con borde y punto en días con citas), la función `nombreMesAnio` en `Formatos` y el selector "Semana / Mes" en `AgendaScreen`.

**Qué tuve que corregir yo:** en `Formatos.kt` escribió "Septiembre", pero la app usa la forma peruana (las fechas cortas usan "set"). Lo cambié a "Setiembre" para que sea consistente.

**¿Respeta MVVM?:** Sí. El cálculo de los días con citas está en el ViewModel, la consulta nueva se declaró primero en la interface y el calendario es un componente que solo recibe datos y lambdas.

---

## Reflexión (5 líneas)

- **¿Qué mejoró la IA y qué no?** Generó rápido código que atraviesa todas las capas (entidad, DAO, Repository, ViewModel y View) siguiendo el estilo del proyecto, pero no siempre consideró casos de uso reales, como guardar el mismo horario al reprogramar.
- **¿Qué error cometió?** Dejó un error de lógica que pasaba una cita CONFIRMADA a PENDIENTE sin cambiar nada, usó "Septiembre" en vez de "Setiembre" y en una mejora se quedó sin cuota a mitad de la respuesta.
- **¿Qué aprendí sobre MVVM, Room y reglas de negocio?** Que darle a la IA el contrato claro (interface primero, reglas en el ViewModel, View que solo observa) hace que respete la arquitectura, y que al agregar una entidad hay que subir la versión de Room.
- También aprendí que lecturas con `Flow` y escrituras con `suspend` hacen que las pantallas se actualicen solas, como el calendario y el carnet.
- Siempre hay que revisar el `git diff` y probar en el emulador: la IA ayuda, pero la responsabilidad de que las reglas de negocio estén bien es mía.
