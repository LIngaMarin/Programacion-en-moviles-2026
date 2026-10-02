# PROMPTS.md – TECSUP Store (Fase 2, rama `mejora-ia-store`)

**Herramienta:** Claude (asistente de IA)
**Mejora obligatoria:** badge con contador en el ítem "Favoritos" del drawer, mostrando cuántos productos marcó el usuario desde el DropdownMenu de cada producto.

---

## Prompt 1 – Lista compartida de favoritos

**Qué le pedí:** que la opción "Favoritos" del DropdownMenu marque y desmarque el producto, y que esa información la puedan ver otras pantallas.

```text
Tengo una app en Jetpack Compose (TECSUP Store) sin ViewModel, el estado se maneja con remember y mutableStateOf.
- components/TarjetaProducto.kt tiene un DropdownMenu con Favoritos, Compartir y Reportar; cada opción solo cierra el menú.
- navigation/AppNavegacion.kt tiene el ModalNavigationDrawer que envuelve al NavHost con Inicio, Mis pedidos, Favoritos y Perfil.
- screens/FavoritosScreen.kt solo muestra un mensaje fijo "Aun no tienes favoritos".

Quiero que la opción "Favoritos" del DropdownMenu marque o desmarque el producto. La lista de favoritos debe poder usarse desde Inicio, desde la pantalla Favoritos y más adelante desde el drawer.
Reglas: sin ViewModel, sin dependencias nuevas, sin cambiar el diseño ni la navegación. Comentarios cortos en español.
Guíame paso por paso indicando qué archivo modifico y si agrego o reemplazo.
```

**Resultado:**
- La lista `favoritos` (`mutableStateListOf<Int>()`, solo ids) se creó en `AppNavegacion.kt`, junto con la función `cambiarFavorito(id)` que agrega o quita.
- `TarjetaProducto` recibe `esFavorito` y `onFavorito`. Ya no guarda la lista, solo avisa hacia arriba. Si el producto ya está marcado, el texto cambia a "Quitar de favoritos" con el corazón relleno.
- `InicioScreen` pasa `producto.id in favoritos` y el `id` a cada tarjeta.
- `FavoritosScreen` filtra los productos con `productos.filter { it.id in favoritos }` y muestra el mensaje solo si la lista está vacía.

**Qué tuve que corregir / revisar:**
- La pantalla Favoritos seguía con el mensaje fijo de la Fase 1. Con el contador iba a decir "Aun no tienes favoritos" aunque hubiera productos marcados, por eso también se actualizó.
- La lista no podía ir dentro de `TarjetaProducto`, porque cada tarjeta tiene su propio estado y el drawer nunca se enteraría. Se dejó en `AppNavegacion`, que es lo único que ven las pantallas y el drawer.
- Los cambios se aplicaron en orden: tarjeta, Inicio, Favoritos y AppNavegacion. Entre paso y paso aparecía error en el archivo siguiente hasta completar el último.
- Probé en el emulador que marcar y desmarcar cambie el texto del menú y la lista de la pantalla Favoritos.

---

## Prompt 2 – Badge con contador en el drawer

**Qué le pedí:** mostrar en el ítem Favoritos del drawer cuántos productos están marcados.

```text
Ya tengo la lista favoritos (mutableStateListOf<Int>) en AppNavegacion.kt.
components/AppDrawer.kt dibuja los ítems con una función ItemMenu(texto, icono, seleccionado, onClick) que usa NavigationDrawerItem y resalta el ítem activo.

Agrega un badge con el número de favoritos solo en el ítem "Favoritos" del drawer.
- El badge solo debe verse si hay al menos 1 favorito.
- Color morado del tema (MoradoStore) con texto blanco.
- No cambies los demás ítems, el encabezado ni el resaltado del ítem activo.
Sin ViewModel, sin dependencias nuevas. Comentarios cortos en español.
```

**Resultado:**
- `AppDrawer` recibe `totalFavoritos`, y desde `AppNavegacion` se le pasa `favoritos.size`.
- `ItemMenu` tiene un parámetro nuevo, `contador: Int = 0`. Al tener valor por defecto, los demás ítems no cambiaron.
- Se usó el parámetro `badge` propio de `NavigationDrawerItem`: si `contador > 0` dibuja un `Badge` morado con el número, y si no, pasa `null`.

**Qué tuve que corregir / revisar:**
- Verifiqué que con 0 favoritos el badge no aparezca, en vez de mostrar un "0".
- Probé en el emulador marcar 2 productos (badge en 2) y quitar 1 (badge en 1). El número se actualiza solo porque `favoritos` es una lista observable.

---

## Commits de esta rama

| # | Commit | Prompt |
|---|---|---|
| 1 | Lista compartida de favoritos; el DropdownMenu marca y desmarca productos | Prompt 1 |
| 2 | Badge con el contador de favoritos en el ítem Favoritos del drawer | Prompt 2 |
| 3 | Documentación de prompts (este archivo) | – |