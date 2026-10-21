package com.tecsup.huellitasvet.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tecsup.huellitasvet.model.Rol

// Se definen TODOS los colores del esquema para que ningun control use el lila por defecto de Material
private fun esquemaClaro(primario: Color, contenedor: Color, textoContenedor: Color, fondo: Color): ColorScheme =
    lightColorScheme(
        primary = primario,
        onPrimary = Blanco,
        primaryContainer = contenedor,
        onPrimaryContainer = textoContenedor,
        secondary = primario,
        onSecondary = Blanco,
        secondaryContainer = contenedor,
        onSecondaryContainer = textoContenedor,
        tertiary = primario,
        onTertiary = Blanco,
        tertiaryContainer = contenedor,
        onTertiaryContainer = textoContenedor,
        background = fondo,
        onBackground = TextoPrincipal,
        surface = Blanco,
        onSurface = TextoPrincipal,
        surfaceVariant = contenedor,
        onSurfaceVariant = TextoSecundario,
        surfaceTint = primario,
        surfaceContainerLowest = Blanco,
        surfaceContainerLow = Blanco,
        surfaceContainer = Blanco,
        surfaceContainerHigh = Blanco,
        surfaceContainerHighest = GrisOcupado,
        outline = Borde,
        outlineVariant = Borde,
        error = EstadoCancelada,
        onError = Blanco
    )

private fun esquemaOscuro(primario: Color, contenedor: Color): ColorScheme =
    darkColorScheme(
        primary = primario,
        onPrimary = OscuroFondo,
        primaryContainer = contenedor,
        onPrimaryContainer = OscuroTexto,
        secondary = primario,
        onSecondary = OscuroFondo,
        secondaryContainer = contenedor,
        onSecondaryContainer = OscuroTexto,
        tertiary = primario,
        onTertiary = OscuroFondo,
        tertiaryContainer = contenedor,
        onTertiaryContainer = OscuroTexto,
        background = OscuroFondo,
        onBackground = OscuroTexto,
        surface = OscuroSuperficie,
        onSurface = OscuroTexto,
        surfaceVariant = OscuroSuperficieAlta,
        onSurfaceVariant = OscuroTextoSecundario,
        surfaceTint = primario,
        surfaceContainerLowest = OscuroSuperficie,
        surfaceContainerLow = OscuroSuperficie,
        surfaceContainer = OscuroSuperficie,
        surfaceContainerHigh = OscuroSuperficieAlta,
        surfaceContainerHighest = OscuroSuperficieAlta,
        outline = OscuroBorde,
        outlineVariant = OscuroBorde,
        error = EstadoCancelada,
        onError = Blanco
    )

private val DuenoClaro = esquemaClaro(VerdePrimario, VerdeContenedor, VerdeOscuroTexto, VerdeFondo)
private val DuenoOscuro = esquemaOscuro(VerdeOscuroPrimario, VerdeOscuroContenedor)
private val VetClaro = esquemaClaro(AzulPrimario, AzulContenedor, AzulOscuroTexto, AzulFondo)
private val VetOscuro = esquemaOscuro(AzulOscuroPrimario, AzulOscuroContenedor)

/**
 * Tema de la app: entrega un ColorScheme segun el perfil (Dueño verde, Veterinario azul)
 * y el modo oscuro. En las pantallas se usa MaterialTheme.colorScheme.primary, nunca un color fijo.
 */
@Composable
fun VetTheme(
    rol: Rol = Rol.CLIENTE,
    oscuro: Boolean = false,
    content: @Composable () -> Unit
) {
    val esquema = when (rol) {
        Rol.CLIENTE -> if (oscuro) DuenoOscuro else DuenoClaro
        Rol.ADMIN -> if (oscuro) VetOscuro else VetClaro
    }
    MaterialTheme(
        colorScheme = esquema,
        typography = Typography,
        content = content
    )
}
