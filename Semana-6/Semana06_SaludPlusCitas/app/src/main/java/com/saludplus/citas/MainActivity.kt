package com.saludplus.citas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import com.saludplus.citas.navigation.AppNavigation
import com.saludplus.citas.ui.theme.AzulPrimario

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // color principal azul del diseño para botones y campos de texto
            MaterialTheme(colorScheme = lightColorScheme(primary = AzulPrimario)) {
                AppNavigation()
            }
        }
    }
}