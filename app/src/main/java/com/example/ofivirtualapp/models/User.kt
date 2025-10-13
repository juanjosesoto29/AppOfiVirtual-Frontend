package com.example.ofivirtualapp.models

/**
 * Modelo de datos para representar a un usuario. * AHORA INCLUYE: nombre y fecha de nacimiento.
 */
data class User(
    val email: String,
    val password: String,
    val name: String? = null,       // Nombre del usuario (opcional
)
