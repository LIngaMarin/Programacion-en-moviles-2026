package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.saludplus.citas.R
import com.saludplus.citas.navigation.Rutas
import com.saludplus.citas.ui.components.BotonPrimario
import com.saludplus.citas.ui.theme.AzulPrimario
import com.saludplus.citas.ui.theme.TextoGris

@Composable
fun SplashScreen(navController: NavController) {
    // fondo del mismo celeste que el borde de la imagen, asi todo se ve como una sola pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE7F4FE))
            .statusBarsPadding()
    ) {
        // ilustracion del doctor completa y de borde a borde (FillWidth: no se recorta)
        Image(
            painter = painterResource(R.drawable.doctor_splash),
            contentDescription = "Doctor",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )

        // en el espacio que queda: logo, nombre y botones
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // logo: corazon azul dentro de un circulo blanco
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Logo SaludPlus",
                    colorFilter = ColorFilter.tint(AzulPrimario),
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(Modifier.height(8.dp))
            Text("Clinica", fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = AzulPrimario)
            Text("SaludPlus", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = AzulPrimario)
            Text("Tu salud, nuestra prioridad", color = TextoGris)
            Spacer(Modifier.height(20.dp))

            BotonPrimario("Comenzar") { navController.navigate(Rutas.REGISTRO) }
            TextButton(onClick = { navController.navigate(Rutas.LOGIN) }) {
                Text("Ya tengo una cuenta", color = AzulPrimario)
            }
        }
    }
}
