package com.example.ofivirtualapp.ui.theme.screen

data class ServicioUI(
    val id: String = java.util.UUID.randomUUID().toString(),
    val categoria: CategoriaServicio,
    val nombre: String,
    val descripcion: String,
    val precioCLP: Int,
    val esPorPersona: Boolean = false
)


enum class CategoriaServicio {
    OFICINA_VIRTUAL,
    CONTABILIDAD,
    FORMALIZACION
}
