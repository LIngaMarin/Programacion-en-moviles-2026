# PROMPTS.md – TECSUP Fit (Fase 2, rama `mejora-ia-fit`)

**Herramienta:** agente de Gemini en Android Studio (modo Agent)
**Modelo:** Gemini 3.6 Flash
**Mejora funcional:** cancelar una reserva de clase con un AlertDialog de confirmación.

---

## Prompt 1 – Cancelar reserva con AlertDialog

**Qué le pedí:** agregar el botón "Cancelar reserva" solo en las reservas Confirmadas, un AlertDialog de confirmación, y que al aceptar la reserva pase al estado "Cancelada" con etiqueta roja.

```text
Actúa como desarrollador Android con experiencia en Jetpack Compose y Material 3.

CONTEXTO
App "TECSUP Fit" (reserva de clases de gimnasio) sin ViewModel: el estado se maneja con remember y mutableStateOf.
- model/Modelos.kt tiene data class Reserva(clase, dia, hora, sala, estado). estado puede ser "Confirmada" o "Completada".
- navigation/AppNavigation.kt crea la lista compartida: val reservas = remember { reservasIniciales.toMutableStateList() } y se la pasa a ReservasScreen.
- screens/ReservasScreen.kt muestra las reservas en una LazyColumn con el composable TarjetaReserva.

TAREA
Agrega la opción de cancelar una reserva con un AlertDialog de confirmación:
1. En TarjetaReserva, solo para las reservas con estado "Confirmada", agrega un TextButton "Cancelar reserva" con texto color 0xFFB3261E.
2. Al tocarlo, se muestra un AlertDialog con título "Cancelar reserva", texto "¿Seguro que deseas cancelar tu clase de [clase] el [dia], [hora]?", botón de confirmar "Sí, cancelar" (texto color 0xFFB3261E) y botón de descartar "No".
3. Si se confirma, la reserva se reemplaza en la lista por una copia con estado "Cancelada" (usar copy). Si se descarta, no cambia nada.
4. Las reservas "Cancelada" se muestran con tarjeta color 0xFFF0F0F0 y etiqueta con fondo 0xFFFDE7E9 y texto 0xFFB3261E.
5. Controla qué reserva se va a cancelar con un estado var reservaACancelar by remember { mutableStateOf<Reserva?>(null) } en ReservasScreen; el diálogo se muestra cuando no es null.

RESTRICCIONES
- Solo modifica ReservasScreen.kt; el parámetro reservas debe pasar a ser MutableList<Reserva>.
- No uses ViewModel, no agregues dependencias y no cambies el diseño, el bottomBar, el FloatingActionButton ni la navegación.
- Comentarios cortos en español solo donde ayuden a entender la lógica.

RESULTADO ESPERADO
Compila sin errores; en Mis reservas, al cancelar una reserva confirmada y aceptar el diálogo, su estado cambia a "Cancelada" con etiqueta roja; al elegir "No" no pasa nada. Al terminar, muestra la lista de archivos modificados.
```

**Resultado:** compiló a la primera. Solo modificó `ReservasScreen.kt`: cambió `reservas` de `List<Reserva>` a `MutableList<Reserva>`, agregó el estado `reservaACancelar`, el `AlertDialog`, el botón en `TarjetaReserva` y los colores del estado "Cancelada".

**Qué tuve que corregir / revisar:**
- Antes de escribir el prompt identifiqué que `ReservasScreen` recibía `List<Reserva>` (solo lectura) y así no se podía cambiar el estado, por eso pedí el cambio a `MutableList<Reserva>`.
- No hizo falta tocar `AppNavigation.kt`: la lista creada con `toMutableStateList()` ya es mutable.
- Acepté los cambios con Keep All y verifiqué en el emulador que el diálogo muestre la clase, el día y la hora reales, que el botón solo aparezca en reservas Confirmadas y que "No" no cambie nada.

---

## Commits de esta rama

| # | Commit | Prompt |
|---|---|---|
| 1 | Cancelación de reservas con AlertDialog | Prompt 1 |
| 2 | Documentación de prompts (este archivo) | – |
| 3 | README con la mejora | – |