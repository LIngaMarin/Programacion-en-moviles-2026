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

## Commits de esta rama

| # | Commit | Prompt |
|---|---|---|
| 1 | Días hábiles con LocalDate y mes dinámico | Prompt 1 |
| 2 | Flechas para cambiar de semana sin retroceder antes de la actual | Prompt 2 |
| 3 | Fecha en texto en español en Confirmar, Cita agendada y Mis citas | Prompt 3 |
| 4 | Documentación de prompts (este archivo) | – |