package com.example.ofivirtualapp.repository

import kotlinx.coroutines.delay

// Data class para simular un usuario en nuestra base de datos falsa.
// Es importante que tenga los mismos campos que usamos.
private data class UserMock(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val passHash: String // Guardamos un "hash" simulado, no la contraseña real.
)

/**
 * Simula un repositorio de autenticación.
 * En una app real, aquí se harían las llamadas de red a tu backend (Firebase, API REST, etc.).
 *
 * Esta versión CORREGIDA asegura que los usuarios registrados se puedan usar para iniciar sesión.
 */
class AuthRepository {

    // --- BASE DE DATOS FALSA ---
    // Usamos 'var' para que la lista pueda ser modificada (añadir nuevos usuarios).
    private var registeredUsers = mutableListOf(
        UserMock("1", "Usuario Ejemplo", "test@test.com", "+56911111111", "Password123!"),
        UserMock("2", "Jane Doe", "jane@doe.com", "+1123456789", "Password123!"),
        UserMock("3", "Admin", "admin@ofivirtual.cl", "+56987654321", "Admin123*")
    )

    /**
     * Simula el registro de un nuevo usuario.
     *
     * 🔹 CAMBIO CLAVE: Ahora añade el usuario a la lista si el registro es exitoso.
     */
    suspend fun registerUser(name: String, email: String, fullPhoneNumber: String, password: String): Boolean {
        // Simulamos un pequeño retraso de red
        delay(1000)

        // 1. Comprueba si el email o el teléfono ya existen en la lista.
        val userExists = registeredUsers.any { it.email.equals(email, ignoreCase = true) || it.phone == fullPhoneNumber }

        if (userExists) {
            // Si el usuario ya existe, el registro falla.
            return false
        } else {
            // 2. 🔹 ¡LA CORRECCIÓN! 🔹 Se crea el nuevo usuario.
            val newUser = UserMock(
                id = (registeredUsers.size + 1).toString(), // ID simple y autoincremental
                name = name,
                email = email,
                phone = fullPhoneNumber,
                passHash = password // En la simulación guardamos la pass directamente. En producción NUNCA se hace.
            )
            // 3. Y se AÑADE a nuestra base de datos falsa.
            registeredUsers.add(newUser)

            // Imprimimos en la consola para verificar que se guardó (opcional pero útil)
            println("AuthRepository: Usuario registrado y guardado -> $newUser")
            println("AuthRepository: Lista de usuarios actual -> ${registeredUsers.map { it.email }}")

            // Si se añadió correctamente, el registro es exitoso.
            return true
        }
    }

    /**
     * Simula el inicio de sesión de un usuario.
     */
    suspend fun loginUser(email: String, password: String): String? {
        // Simulamos un pequeño retraso de red
        delay(1000)

        // Imprimimos en la consola para depurar qué estamos intentando loguear
        println("AuthRepository: Intentando iniciar sesión con email: $email")

        // 1. Busca en la lista de usuarios (que ahora incluye a los recién registrados)
        //    un usuario que coincida EXACTAMENTE en email y contraseña.
        val foundUser = registeredUsers.find {
            it.email.equals(email, ignoreCase = true) && it.passHash == password
        }

        return if (foundUser != null) {
            // 2. Si se encuentra el usuario, devuelve un "token" de autenticación falso.
            println("AuthRepository: Usuario encontrado. Login exitoso para ${foundUser.email}")
            "FAKE_JWT_TOKEN_FOR_${foundUser.id}"
        } else {
            // 3. Si no se encuentra, devuelve null.
            println("AuthRepository: Usuario NO encontrado. Email o contraseña incorrectos.")
            null
        }
    }
}
