package com.tecsup.huellitasvet.ui.theme

import androidx.compose.ui.graphics.Color
import com.tecsup.huellitasvet.model.EstadoCita

// ---------- Dueño (esmeralda) ----------
val VerdePrimario = Color(0xFF059669)
val VerdeContenedor = Color(0xFFD1FAE5)
val VerdeFondo = Color(0xFFF6FDF9)
val VerdeOscuroTexto = Color(0xFF064E3B)

// ---------- Veterinario (azul) ----------
val AzulPrimario = Color(0xFF1D4ED8)
val AzulContenedor = Color(0xFFDBEAFE)
val AzulFondo = Color(0xFFF7F9FF)
val AzulOscuroTexto = Color(0xFF1E3A8A)

// ---------- Estados de la cita ----------
val EstadoPendiente = Color(0xFFD97706)
val EstadoConfirmada = Color(0xFF16A34A)
val EstadoAtendida = Color(0xFF475569)
val EstadoCancelada = Color(0xFFDC2626)

// ---------- Neutros ----------
val Blanco = Color(0xFFFFFFFF)
val TextoPrincipal = Color(0xFF111827)
val TextoSecundario = Color(0xFF6B7280)
val Borde = Color(0xFFE5E7EB)
val AmbarClaro = Color(0xFFFEF3C7)
val GrisOcupado = Color(0xFFF1F5F9)

// ---------- Modo oscuro ----------
val OscuroFondo = Color(0xFF0F1513)
val OscuroSuperficie = Color(0xFF18201D)
val OscuroSuperficieAlta = Color(0xFF212B27)
val OscuroTexto = Color(0xFFE5E7EB)
val OscuroTextoSecundario = Color(0xFF9CA3AF)
val OscuroBorde = Color(0xFF2F3A36)
val VerdeOscuroPrimario = Color(0xFF34D399)
val VerdeOscuroContenedor = Color(0xFF065F46)
val AzulOscuroPrimario = Color(0xFF93C5FD)
val AzulOscuroContenedor = Color(0xFF1E3A8A)

/** Color de cada estado (se usa en EstadoCitaChip y en el grafico de dona). */
fun colorEstado(estado: EstadoCita): Color = when (estado) {
    EstadoCita.PENDIENTE -> EstadoPendiente
    EstadoCita.CONFIRMADA -> EstadoConfirmada
    EstadoCita.ATENDIDA -> EstadoAtendida
    EstadoCita.CANCELADA -> EstadoCancelada
}
