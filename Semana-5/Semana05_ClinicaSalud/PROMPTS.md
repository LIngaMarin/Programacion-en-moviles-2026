# PROMPTS.md – Clínica Salud+ (Fase 2, rama `mejora-ia-clinica`)

**Herramienta:** agente de Gemini en Android Studio (modo Agent)
**Modelo:** Gemini 3.6 Flash
**Mejora funcional:** cancelar una cita con un AlertDialog de confirmación, con aviso Snackbar y reflejo en las estadísticas del perfil.

La mejora se pidió en 3 prompts pequeños, uno por cambio, para poder revisar y probar cada parte antes de continuar.

---

## Prompt 1 – Cancelar cita con AlertDialog

**Qué le pedí:** agregar el botón "Cancelar cita" solo en las citas Confirmadas, un AlertDialog de confirmación, y que al aceptar la cita pase al estado "Cancelada" con etiqueta roja.

```text
Actúa como desarrollador Android con experiencia en Jetpack Compose y Material 3.

CONTEXTO
Proyecto "Clínica Salud+" (reserva de citas médicas) sin ViewModel: el estado se maneja con remember y mutableStateOf.
- model/Modelos.kt tiene data class Cita(medico, especialidad, fecha, hora, estado). estado puede ser "Confirmada" o "Completada".
- navigation/AppNavigation.kt crea la lista compartida: val citas = remember { citasIniciales.toMutableStateList() } y se la pasa a MisCitasScreen.
- screens/MisCitasScreen.kt muestra las citas en una LazyColumn con el composable TarjetaCita, que colorea la etiqueta según el estado.

TAREA
Agrega la opción de cancelar una cita con un AlertDialog de confirmación:
1. En TarjetaCita, solo para las citas con estado "Confirmada", agrega un TextButton "Cancelar cita" con texto color 0xFFB3261E.
2. Al tocarlo, se muestra un AlertDialog con título "Cancelar cita", texto "¿Seguro que deseas cancelar tu cita con [médico] el [fecha], [hora]?", botón de confirmar "Sí, cancelar" (texto color 0xFFB3261E) y botón de descartar "No".
3. Si se confirma, la cita se reemplaza en la lista por una copia con estado "Cancelada" (usar copy). Si se descarta, no cambia nada.
4. Las citas "Cancelada" se muestran con tarjeta color 0xFFF3EFF7 y etiqueta con fondo 0xFFFDE7E9 y texto 0xFFB3261E.
5. Controla qué cita se va a cancelar con un estado var citaACancelar by remember { mutableStateOf<Cita?>(null) } en MisCitasScreen; el diálogo se muestra cuando no es null.

RESTRICCIONES
- Solo modifica MisCitasScreen.kt y, si es necesario, el tipo del parámetro en AppNavigation.kt (MisCitasScreen debe recibir MutableList<Cita>).
- No uses ViewModel, no agregues dependencias y no cambies el diseño ni la navegación existente.
- Comentarios cortos en español solo donde ayuden a entender la lógica.

RESULTADO ESPERADO
Compila sin errores; en Mis citas, al cancelar una cita confirmada y aceptar el diálogo, su estado cambia a "Cancelada" con etiqueta roja; al elegir "No" no pasa nada. Al terminar, muestra la lista de archivos modificados.
```

**Resultado:** compiló a la primera. Solo modificó `MisCitasScreen.kt`: cambió el parámetro `citas` de `List<Cita>` a `MutableList<Cita>` y agregó el estado `citaACancelar`, el `AlertDialog` y los colores del estado "Cancelada".

**Qué tuve que corregir / revisar:**
- Antes de escribir el prompt identifiqué que `MisCitasScreen` recibía `List<Cita>` (solo lectura), así que no iba a poder modificar la lista. Por eso pedí el cambio a `MutableList<Cita>` en el prompt.
- No hizo falta tocar `AppNavigation.kt`: la lista creada con `toMutableStateList()` ya es mutable, así que el mismo argumento sirve.
- Revisé en el emulador que el diálogo muestre el nombre real del médico, la fecha y la hora (no los textos de ejemplo entre corchetes), que el botón solo aparezca en citas Confirmadas y que "No" no cambie nada.

---

## Prompt 2 – Aviso con Snackbar y opción Deshacer

**Qué le pedí:** mostrar un Snackbar al cancelar, con la acción "Deshacer" para devolver la cita a Confirmada.

```text
Continuando con la cancelación de citas que acabas de implementar en screens/MisCitasScreen.kt:

TAREA
Después de confirmar la cancelación en el AlertDialog, muestra un Snackbar como aviso:
1. Agrega un SnackbarHostState con remember y úsalo en el parámetro snackbarHost del Scaffold de MisCitasScreen (con SnackbarHost).
2. Al confirmar "Sí, cancelar", además de cambiar el estado a "Cancelada", muestra el Snackbar con el mensaje "Cita con [nombre del médico] cancelada" y el botón de acción "Deshacer".
3. Si el usuario toca "Deshacer", la cita vuelve a su estado "Confirmada" en la misma posición de la lista.
4. Para mostrar el Snackbar usa rememberCoroutineScope y scope.launch, porque showSnackbar es una función suspend.

RESTRICCIONES
- Solo modifica MisCitasScreen.kt.
- No cambies el AlertDialog, el diseño de las tarjetas, el FloatingActionButton ni la navegación.
- No uses ViewModel ni agregues dependencias.
- Comentarios cortos en español solo donde ayuden a entender la lógica.

RESULTADO ESPERADO
Compila sin errores; al cancelar una cita aparece abajo el Snackbar "Cita con ... cancelada" con el botón "Deshacer", y al tocarlo la cita vuelve a estar "Confirmada". Al terminar, muestra la lista de archivos modificados.
```

**Resultado:** compiló sin errores. Agregó `SnackbarHostState`, `rememberCoroutineScope` y el `snackbarHost` en el Scaffold; al elegir "Deshacer" (`SnackbarResult.ActionPerformed`) la cita vuelve a Confirmada.

**Qué tuve que corregir / revisar:**
- Al terminar, los cambios quedaron pendientes en el panel ("Changes 1 file"); tuve que aceptarlos con Keep All antes de enviar el siguiente prompt para que no se mezclaran.
- Probé que "Deshacer" devuelva la cita a la misma posición de la lista y con el estado Confirmada.

---

## Prompt 3 – Estadística de canceladas en el Perfil

**Qué le pedí:** que el Perfil muestre también cuántas citas se cancelaron.

```text
Ahora que las citas pueden tener el estado "Cancelada", actualiza la pantalla de perfil para que lo refleje.

CONTEXTO
- screens/PerfilScreen.kt recibe citas: List<Cita> y muestra dos estadísticas en un Row con el composable CajaDato: "Confirmadas" y "Completadas", calculadas con citas.count { it.estado == ... }.

TAREA
1. Agrega una tercera estadística "Canceladas" calculada igual con citas.count { it.estado == "Cancelada" }.
2. Muéstrala como un tercer CajaDato en el mismo Row, con el mismo peso (weight 1f) que los otros dos.
3. El número de "Canceladas" debe mostrarse en color 0xFFB3261E para diferenciarlo; para esto agrega a CajaDato un parámetro opcional colorValor: Color con valor por defecto Color.Unspecified, sin cambiar cómo se ven las otras dos cajas.

RESTRICCIONES
- Solo modifica PerfilScreen.kt.
- No cambies el resto del diseño, el Switch ni los datos del paciente.
- No uses ViewModel ni agregues dependencias.
- Comentarios cortos en español solo donde ayuden a entender la lógica.

RESULTADO ESPERADO
Compila sin errores; en Perfil se ven tres cajas (Confirmadas, Completadas, Canceladas) y, al cancelar una cita en Mis citas, el número de Canceladas sube y el de Confirmadas baja. Al terminar, muestra la lista de archivos modificados.
```

**Resultado:** compiló sin errores. Solo modificó `PerfilScreen.kt`: agregó `val canceladas`, el parámetro opcional `colorValor` en `CajaDato` y la tercera caja en rojo.

**Qué tuve que corregir / revisar:**
- Detecté que después del prompt 1 el Perfil no contaba las citas canceladas (solo Confirmadas y Completadas), por eso hice este prompt.
- Verifiqué con 2 citas agendadas y 1 cancelada que el Perfil muestre Confirmadas 1 y Canceladas 1, y que las otras cajas mantengan su color.

---

## Commits de esta rama

| # | Commit | Prompt |
|---|---|---|
| 1 | Cancelación de citas con AlertDialog | Prompt 1 |
| 2 | Snackbar con opción Deshacer | Prompt 2 |
| 3 | Estadística de citas canceladas en el Perfil | Prompt 3 |
| 4 | Documentación de prompts (este archivo) | – |