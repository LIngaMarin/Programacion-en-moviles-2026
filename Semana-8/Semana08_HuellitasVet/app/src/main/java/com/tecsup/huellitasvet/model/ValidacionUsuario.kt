package com.tecsup.huellitasvet.model

/**
 * Validacion de los datos de un usuario nuevo. La usan el registro del dueño (SesionViewModel)
 * y el alta de veterinarios del administrador (AdminViewModel), asi la regla esta en un solo lugar.
 * Devuelve el mensaje de error o null si todo esta bien.
 */
fun validarUsuarioNuevo(
    nombre: String,
    correo: String,
    clave: String,
    confirmar: String,
    telefono: String,
    distrito: String
): String? = when {
    nombre.isBlank() || correo.isBlank() || clave.isBlank() || telefono.isBlank() || distrito.isBlank() ->
        "Completa todos los campos"
    !correo.contains("@") || !correo.contains(".") -> "Ingresa un correo válido"
    // Celular peruano: 9 digitos y empieza con 9
    !Regex("9\\d{8}").matches(telefono) -> "El teléfono debe tener 9 dígitos y empezar con 9"
    distrito.length < 3 -> "Ingresa un distrito válido"
    clave.length < 4 -> "La contraseña debe tener al menos 4 caracteres"
    clave != confirmar -> "Las contraseñas no coinciden"
    else -> null
}
