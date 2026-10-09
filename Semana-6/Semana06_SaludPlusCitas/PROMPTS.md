# PROMPTS.md – Clínica SaludPlus (Fase 2, rama `mejora-ia-saludplus`)

**Herramienta:** Claude (asistente de IA)
**Mejora obligatoria:** calendario dinámico en la Pantalla 6 (Fecha y hora). En la Fase 1 los días eran una lista fija; en esta fase se generan con `java.time.LocalDate`.

---

## Prompt 1 – Días hábiles reales y mes dinámico

**Qué le pedí:** reemplazar la lista fija de días por los próximos 5 días hábiles desde hoy, y que el mes del título cambie según los días mostrados.

```text
Tengo una app en Jetpack Compose (Clínica SaludPlus) sin ViewModel ni base de datos; los datos están en un object Repositorio.
En FechaHoraScreen los días salen de una lista fija: Repositorio.diasDisponibles = listOf("Lun 15", "Mar 16", "Mie 17", "Jue 18", "Vie 19"),
y el título dice "Setiembre 2026" fijo. horariosDisponibles(medicoId, fecha) compara la fecha de las citas guardadas.

Con java.time.LocalDate quiero:
- Mostrar los próximos 5 días hábiles a partir de hoy (sin sábados, domingos ni días pasados).
- Que el título muestre el mes y año según los días mostrados, por ejemplo "Octubre 2026", con los meses en español ("setiembre").
- Que el bloqueo de horarios ya reservados siga funcionando.
Sin librerías nuevas ni cambiar los nombres de las funciones del Repositorio. El minSdk del proyecto es 26. Comentarios cortos en español.
```

**Respuesta resumida:**
- Creó `util/Fechas.kt` con `diasHabiles(semana)`, que parte de `LocalDate.now()` y avanza día por día saltando sábados y domingos hasta juntar 5. También `diaCorto(fecha)` ("Lun") y `textoMes(fecha)` ("Octubre 2026").
- En `FechaHoraScreen`, la fila de días usa `diasHabiles(0)` y el día elegido se guarda como `fecha.toString()` ("2026-10-12").

**Qué tuve que corregir / revisar:**
- La cita precargada del Repositorio tenía la fecha fija `"Mar 16"`, que ya no coincidía con ningún día. La cambié a `diasHabiles(0)[1].toString()` para seguir probando que un horario reservado no aparece.
- Los nombres de los meses se pusieron en una lista propia para que diga "setiembre" como en el diseño, en vez de depender del idioma del celular.
- Al probar, la pantalla Confirmar mostraba la fecha como "2026-10-12 de setiembre 2026", porque seguía pegando el texto fijo de la Fase 1. Lo corregí en el Prompt 3.

---

## Prompt 2 – Flechas para cambiar de semana

**Qué le pedí:** agregar las flechas < y > al lado del mes para avanzar o retroceder una semana, sin poder ir antes de la semana actual.

```text
En FechaHoraScreen ya tengo diasHabiles(semana) y el título con textoMes(dias.first()).
Agrega una flecha < y una flecha > a los lados del título:
- > avanza una semana y < retrocede una semana.
- No se puede retroceder antes de la semana actual.
- El mes del título debe cambiar solo según la semana mostrada.
Usa remember para el estado, sin ViewModel. Comentarios cortos en español.
```

**Respuesta resumida:**
- Agregó el estado `var semana by remember { mutableIntStateOf(0) }` y `val dias = diasHabiles(semana)`.
- Puso una `Row` con dos `IconButton` (`KeyboardArrowLeft` y `KeyboardArrowRight`) y el mes en el centro. La flecha < tiene `enabled = semana > 0`.

**Qué tuve que corregir / revisar:**
- Al cambiar de semana, el día y la hora elegidos seguían guardados aunque ese día ya no se viera en pantalla, y el botón Continuar quedaba habilitado. Agregué `diaSeleccionado = ""` y `horaSeleccionada = ""` en el `onClick` de las dos flechas.
- Probé en el emulador que la flecha < se vea gris en la semana actual y que el mes cambie al avanzar hasta el mes siguiente.

---

## Prompt 3 – Fecha en texto en español

**Qué le pedí:** que la Pantalla 7 (Confirmar cita) muestre la fecha como "Martes 13 de octubre 2026".

```text
Las citas guardan la fecha como texto con formato de LocalDate ("2026-10-13").
Quiero una función que convierta ese texto en "Martes 13 de octubre 2026" (día de la semana, número, mes en minúscula y año),
y usarla en ConfirmarCitaScreen. Usa las listas de días y meses que ya están en util/Fechas.kt. Comentarios cortos en español.
```

**Respuesta resumida:**
- Agregó `textoFecha(fechaGuardada)` en `util/Fechas.kt`, que usa `LocalDate.parse` y arma el texto con las listas de días y meses.
- En `ConfirmarCitaScreen` reemplazó la fila de la fecha por `FilaDato(..., "Fecha", textoFecha(fecha))`.

**Qué tuve que corregir / revisar:**
- La IA solo cambió Confirmar, pero **Cita agendada** y **Mis citas** también seguían pegando "de setiembre" al texto de la Fase 1 y mostraban la fecha mal. Apliqué `textoFecha` también en esas dos pantallas.
- Comprobé en el emulador que, después de agendar un día de la semana siguiente, ese horario ya no aparece para el mismo médico y día. El calendario dinámico no rompió el bloqueo de horarios reservados.

---

## Prompt 4 – Retos extra (pantallas 12 a 15)

**Qué le pedí:** completar las 4 pantallas de reto extra que seguían con `PantallaEnConstruccion`.

```text
En mi app Clínica SaludPlus (Jetpack Compose, sin ViewModel, datos en el object Repositorio) faltan los 4 retos extra:
- Detalle de cita: recibe citaId, muestra los datos y permite cancelarla con un AlertDialog usando Repositorio.cancelarCita (removeIf).
- Resultados: un modelo propio Resultado y una lista fija de exámenes con su estado.
- Notificaciones: generar los mensajes con map sobre las citas del usuario.
- Términos y condiciones: texto con scroll.
Usa los componentes que ya tengo (BarraSuperior, BarraInferior, FilaDato, BotonPrimario) y los mismos colores. Comentarios cortos en español.
```

**Respuesta resumida:** creó el modelo `Resultado` y las 4 pantallas: Detalle con `AlertDialog`, Resultados con lista fija, Notificaciones con `citasDelUsuario().map { }` y Términos con `verticalScroll`.

**Qué tuve que corregir / revisar:**
- En Mis citas las tarjetas no se podían tocar, así que no había forma de llegar al Detalle. Agregué `.clickable { navController.navigate(Rutas.detalleCita(cita.id)) }`.
- Probé que "No" en el AlertDialog no haga nada, y que al cancelar, la cita desaparezca y su horario vuelva a estar disponible.

---

## Prompt 5 – Después de registrarse, ir al Login

**Qué le pedí:** que al registrarse no entre directo al Inicio, sino que tenga que iniciar sesión.

```text
En RegistroScreen, al tocar Registrarme y pasar las validaciones, la app va directo a HOME con la sesión iniciada.
Quiero que después de registrarse vaya al Login y el paciente tenga que iniciar sesión con su teléfono y contraseña.
No cambies el nombre ni los parámetros de registrarUsuario.
```

**Respuesta resumida:** cambió la navegación del Registro para que vaya a `Rutas.LOGIN` en vez de `Rutas.HOME`.

**Qué tuve que corregir / revisar:**
- `registrarUsuario` seguía guardando `usuarioActual = nuevo`, así que el usuario quedaba logueado igual. Quité esa línea para que la sesión solo se inicie en el Login.
- Agregué `popUpTo(Rutas.REGISTRO) { inclusive = true }` y `launchSingleTop = true`. Así, si se llega al Registro desde el Login, no quedan dos Login seguidos en el historial.

---

## Prompt 6 – Locales por distrito antes de agendar

**Qué le pedí:** una opción "Locales" con sedes en distintos distritos, y que solo después de elegir un local se pueda agendar una cita. La opción "Agendar cita" del Inicio se quita.

```text
Agrega un modelo Local (id, nombre, distrito, direccion) y una lista de sedes de SaludPlus en distritos de Lima dentro del Repositorio.
En el Inicio, reemplaza la tarjeta "Agendar cita" por "Locales". Al elegir un local se guarda en el Repositorio y recién ahí
se pasa a Especialidades. La cita debe guardar el local y Confirmar debe mostrar su dirección.
Usa los colores y componentes que ya tiene la app. Sin ViewModel. Comentarios cortos en español.
```

**Respuesta resumida:** creó `Local`, la lista `locales`, `localActual` y `elegirLocal()` en el Repositorio, la pantalla `LocalesScreen` y la ruta `LOCALES`. `Cita` tiene un campo nuevo `local`, y `agendarCita` lo llena con el local elegido.

**Qué tuve que corregir / revisar:**
- Las especialidades destacadas y "Ver todas" del Inicio seguían llevando directo a agendar, sin pasar por Locales. Les agregué la condición `if (Repositorio.localActual == null)` para mandar primero a Locales.
- Confirmar seguía con la dirección fija "Av. Los Olivos 123". La cambié por el nombre y la dirección del local elegido, y agregué el local en Cita agendada y en el Detalle.
- El botón de Mis citas vacía seguía diciendo "Agendar cita". Lo cambié a "Elegir local", y `cerrarSesion()` ahora también borra el local elegido.

---

## Prompt 7 – Mis doctores por categorías

**Qué le pedí:** una opción "Mis doctores" en el menú del Inicio que muestre los médicos agrupados por especialidad.

```text
Agrega una opción "Mis doctores" en el menú del Inicio. Debe abrir una pantalla con todos los médicos
agrupados por especialidad: un título por cada especialidad y debajo sus médicos.
Reutiliza TarjetaMedico y medicosPorEspecialidad del Repositorio. Mismos colores de la app.
```

**Respuesta resumida:** creó `MisDoctoresScreen`, que recorre `Repositorio.especialidades` con `forEach` y, por cada una, agrega un título con un `item` y sus médicos con `items(medicosPorEspecialidad(id))`. También agregó la ruta `MIS_DOCTORES` y la tarjeta en el Inicio.

**Qué tuve que corregir / revisar:**
- Revisé que cada especialidad muestre cuántos médicos tiene y que los médicos salgan ordenados por calificación, igual que en la pantalla Médicos.
- Las tarjetas del Inicio ya eran 4 en 2 filas. Puse "Mis doctores" como tarjeta ancha debajo, con los mismos colores azules de la app.
- Varias especialidades tenían un solo médico y se pedía mínimo 2 por categoría. Agregué un segundo médico a Pediatría, Cardiología, Dermatología, Traumatología y Oftalmología.
---

## Prompt 8 – Colores de la app en todo el tema

**Qué le pedí:** que ningún control use los colores por defecto de Material, sino la paleta de la app.

```text
Algunos controles de mi app salen con el morado/lila que trae Material 3 por defecto (por ejemplo el AlertDialog).
Quiero que todo el tema use la paleta de mi Color.kt: AzulPrimario, AzulClaro, FondoApp, TextoGris y blanco para las superficies.
```

**Respuesta resumida:** en `MainActivity`, el `lightColorScheme` ahora define `primary`, `secondary`, `tertiary`, `background`, `surface`, `surfaceVariant` y los `surfaceContainer` con los colores de `Color.kt`.

**Qué tuve que corregir / revisar:**
- Antes solo estaba definido `primary`, y Material completaba lo demás con su morado. Por eso el AlertDialog se veía lila.
- Al AlertDialog de cancelar le puse `containerColor = Color.White` y el botón "No" en `TextoGris`, para que combine con las tarjetas.

---

## Prompt 9 – Médicos por local

**Qué le pedí:** que cada local tenga sus propios médicos, porque era raro que todas las sedes tuvieran los mismos.

```text
En mi app todas las sedes muestran los mismos médicos. Quiero que cada médico atienda solo en algunos locales
y que al agendar solo salgan los médicos del local elegido. En "Mis doctores" deben seguir saliendo todos
(mínimo 2 por especialidad), pero indicando en qué distritos atiende cada uno.
```

**Respuesta resumida:** agregó a `Medico` una lista `locales` con los ids de las sedes donde atiende. En el `Repositorio` creó `medicosDelLocal`, que filtra por `localActual`, y `sedesDelMedico`, que arma el texto de los distritos con `filter` + `joinToString`. La pantalla Médicos usa `medicosDelLocal` y "Mis doctores" muestra las sedes en la tarjeta.

**Qué tuve que corregir / revisar:**
- Repartí los médicos para que cada local tenga al menos 1 médico de cada especialidad. Si no, alguna sede se quedaba con la lista vacía.
- `medicosPorEspecialidad` no se cambió, porque "Mis doctores" debe seguir mostrando a todos (mínimo 2 por categoría).
- A `locales` le dejé un valor por defecto (todos los locales) para no romper los demás usos de `Medico`.

---

## Commits de esta rama

| # | Commit | Prompt |
|---|---|---|
| 1 | Días hábiles con LocalDate y mes dinámico | Prompt 1 |
| 2 | Flechas para cambiar de semana sin retroceder antes de la actual | Prompt 2 |
| 3 | Fecha en texto en español en Confirmar, Cita agendada y Mis citas | Prompt 3 |
| 4 | Documentación de prompts (este archivo) | – |
| 5 | Retos extra: detalle de cita, resultados, notificaciones y términos | Prompt 4 |
| 6 | El registro manda al login | Prompt 5 |
| 7 | Locales por distrito antes de agendar | Prompt 6 |
| 8 | Mis doctores agrupados por especialidad | Prompt 7 |
| 9 | Colores del tema con la paleta de la app | Prompt 8 |
| 10 | Actualización de prompts (este archivo) | – |
| 11 | Segundo médico en las especialidades que tenían uno | Prompt 7 |
| 12 | Médicos por local | Prompt 9 |