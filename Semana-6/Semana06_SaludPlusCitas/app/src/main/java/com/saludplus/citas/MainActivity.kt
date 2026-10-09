package com.saludplus.citas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.saludplus.citas.navigation.AppNavigation
import com.saludplus.citas.ui.theme.AzulClaro
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.FondoApp
import com.saludplus.citas.ui.theme.TextoGris

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // todos los colores del tema salen de la paleta de la app (Color.kt),
            // asi ningun control usa el morado que trae Material por defecto
            val coloresApp = lightColorScheme(
                primary = AzulPrimario,
                onPrimary = Color.White,
                secondary = AzulPrimario,
                tertiary = AzulPrimario,
                background = FondoApp,
                surface = Color.White,
                surfaceVariant = AzulClaro,
                onSurfaceVariant = TextoGris,
                surfaceContainer = Color.White,
                surfaceContainerHigh = Color.White,
                surfaceContainerHighest = Color.White
            )
            MaterialTheme(colorScheme = coloresApp) {
                AppNavigation()
            }
        }
    }
}