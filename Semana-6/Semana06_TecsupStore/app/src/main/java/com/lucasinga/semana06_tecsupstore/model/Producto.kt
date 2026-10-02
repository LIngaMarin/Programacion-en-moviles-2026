package com.lucasinga.semana06_tecsupstore.model

data class Producto(
    val id: Int,
    val nombre: String,
    val precio: Double,
    val categoria: String
)

val categorias = listOf("Todos", "Audio", "Tecnologia", "Accesorios")

val productos = listOf(
    Producto(1, "Audifonos", 89.0, "Audio"),
    Producto(2, "Smartwatch", 199.0, "Tecnologia"),
    Producto(3, "Funda celular", 25.0, "Accesorios"),
    Producto(4, "Parlante bluetooth", 120.0, "Audio"),
    Producto(5, "Tablet", 650.0, "Tecnologia"),
    Producto(6, "Cargador rapido", 45.0, "Accesorios")
)

val nombreUsuario = "Lucas Inga Marin"
val correoUsuario = "lucas.inga@tecsup.edu.pe"