package com.example.ofivirtualapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ofivirtualapp.models.User // Asegúrate de que el modelo User tampoco tenga birthDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Estado de la UI, declarado correctamente a nivel de archivo
data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val registerSuccess: Boolean = false
)

class AuthViewModel : ViewModel() {

    var authUiState by mutableStateOf(AuthUiState())
        private set

    // --- CORRECCIÓN 1: El usuario de demostración ya no tiene fecha de nacimiento ---
    private val demoUser = User(
        email = "test@ofivirtual.cl",
        password = "Ofivirtual1234",
        name = "Usuario Demo"
    )
    private val registeredUsers = mutableListOf(demoUser)

    fun login(email: String, password: String, onLoginSuccess: () -> Unit) {
        viewModelScope.launch {
            authUiState = authUiState.copy(isLoading = true, error = null)
            delay(1500)

            val user = registeredUsers.find { it.email == email }

            if (user != null && user.password == password) {
                authUiState = authUiState.copy(isLoading = false)
                onLoginSuccess()
            } else {
                authUiState = authUiState.copy(isLoading = false, error = "Correo o contraseña incorrectos.")
            }
        }
    }

    // --- CORRECCIÓN 2: La función de registro ahora solo acepta nombre, email y password ---
    fun register(email: String, password: String, name: String, phone: String) {
        viewModelScope.launch {
            authUiState = authUiState.copy(isLoading = true, error = null, registerSuccess = false)
            delay(1500)

            if (registeredUsers.any { it.email == email }) {
                authUiState = authUiState.copy(isLoading = false, error = "El correo electrónico ya está en uso.")
            } else {
                // --- CORRECCIÓN 3: Se crea el usuario sin la fecha de nacimiento ---
                val newUser = User(email = email, password = password, name = name)
                registeredUsers.add(newUser)
                authUiState = authUiState.copy(isLoading = false, registerSuccess = true)
            }
        }
    }

    fun clearError() {
        authUiState = authUiState.copy(error = null)
    }

    fun resetRegisterStatus() {
        authUiState = authUiState.copy(registerSuccess = false)
    }
    /**
     * Limpia el estado de la UI al cerrar sesión.
     * En una app real, aquí se borraría el token de usuario o los datos de sesión.
     */
    fun logout() {
        authUiState = AuthUiState() // Reinicia el estado a su valor inicial
    }
}

