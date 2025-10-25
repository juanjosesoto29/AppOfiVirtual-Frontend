package com.example.ofivirtualapp.ui.theme.screen

// 🔹 HOGAR ÚNICO PARA NUESTROS MODELOS DE DATOS COMPARTIDOS 🔹

/**
 * Representa un servicio que se puede mostrar en la UI y agregar al carrito.
 * Esta es la ÚNICA definición que usará toda la app.
 */
data class ServicioUI(
    val id: String = java.util.UUID.randomUUID().toString(),
    val categoria: CategoriaServicio,
    val nombre: String,
    val descripcion: String,
    val precioCLP: Int,
    val esPorPersona: Boolean = false // Propiedad que faltaba y causaba errores
)

/**
 * Enum para clasificar los tipos de servicios.
 * Esta es la ÚNICA definición que usará toda la app.
 */
enum class CategoriaServicio {
    OFICINA_VIRTUAL,
    CONTABILIDAD,
    FORMALIZACION
}
