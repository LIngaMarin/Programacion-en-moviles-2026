package com.tecsup.huellitasvet.data.local

import com.tecsup.huellitasvet.data.local.entity.Servicio

/**
 * GUIADO. Datos de prueba que se cargan solo la primera vez que se abre la app.
 * Solo datos ficticios (Ley 29733).
 */
object DatosIniciales {

    suspend fun sembrar(db: VetDatabase) {
        // TODO (Paso 3): cambiar la condicion a usuarioDao().contar() == 0 y sembrar
        // usuarios, mascotas y unas 10 citas (fechas con LocalDate.now().plusDays(n).toString())
        if (db.servicioDao().contar() == 0) {
            db.servicioDao().insertarTodos(servicios)
        }
    }

    // 4 servicios: Desparasitacion empieza inactivo
    private val servicios = listOf(
        Servicio(nombre = "Consulta general", descripcion = "Evaluación completa del estado de salud", duracionMin = 30, precio = 60.0, icono = "heart"),
        Servicio(nombre = "Vacunación", descripcion = "Aplicación de vacunas según su calendario", duracionMin = 30, precio = 45.0, icono = "syringe"),
        Servicio(nombre = "Baño y corte", descripcion = "Baño, secado y corte de pelo", duracionMin = 60, precio = 50.0, icono = "scissors"),
        Servicio(nombre = "Desparasitación", descripcion = "Tratamiento antiparasitario interno y externo", duracionMin = 15, precio = 25.0, activo = false, icono = "heart")
    )
}
