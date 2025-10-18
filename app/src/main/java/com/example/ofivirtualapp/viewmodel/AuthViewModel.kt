package com.example.ofivirtualapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ofivirtualapp.domain.*
import com.example.ofivirtualapp.repository.AuthRepository
import kotlinx.coroutines.launch

// ESTADO PARA EL FORMULARIO DE REGISTRO
data class RegisterFormState(
    val name: String = "",
    val email: String = "",
    val phonePrefix: String = "+56",
    val phoneNumber: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val termsAccepted: Boolean = false,
    // Campos de error
    val nameError: String? = null,
    val emailError: String? = null,
    val phoneError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val termsError: String? = null
)

// ESTADO PARA EL FORMULARIO DE LOGIN
data class LoginFormState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null
)

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    var registerFormState by mutableStateOf(RegisterFormState())
        private set

    var loginFormState by mutableStateOf(LoginFormState())
        private set

    // --- 🔹 FUNCIONES DE ACTUALIZACIÓN PARA EL REGISTRO (AHORA SE USARÁN) 🔹 ---
    fun onRegisterNameChange(name: String) {
        registerFormState = registerFormState.copy(name = name, nameError = null)
    }
    fun onRegisterEmailChange(email: String) {
        registerFormState = registerFormState.copy(email = email, emailError = null)
    }
    fun onRegisterPhonePrefixChange(prefix: String) {
        registerFormState = registerFormState.copy(phonePrefix = prefix, phoneError = null)
    }
    fun onRegisterPhoneNumberChange(number: String) {
        registerFormState = registerFormState.copy(phoneNumber = number, phoneError = null)
    }
    fun onRegisterPasswordChange(password: String) {
        registerFormState = registerFormState.copy(password = password, passwordError = null)
    }
    fun onRegisterConfirmPasswordChange(confirmPassword: String) {
        registerFormState = registerFormState.copy(confirmPassword = confirmPassword, confirmPasswordError = null)
    }
    fun onRegisterTermsChange(accepted: Boolean) {
        registerFormState = registerFormState.copy(termsAccepted = accepted, termsError = null)
    }

    // --- LÓGICA DE REGISTRO (Llamada solo por el botón) ---
    fun register(onSuccess: () -> Unit) {
        val state = registerFormState

        // Se ejecutan todas las validaciones a la vez
        val nameError = validateNameLettersOnly(state.name)
        val emailError = validateEmail(state.email)
        val phoneError = validatePhoneDigitsOnly(state.phoneNumber)
        val passwordError = validateStrongPass(state.password)
        val confirmPasswordError = validateConfirm(state.password, state.confirmPassword)
        val termsError = if (!state.termsAccepted) "Debes aceptar los términos y condiciones" else null

        // Se actualiza el estado de la UI con todos los errores de una vez
        registerFormState = state.copy(
            nameError = nameError,
            emailError = emailError,
            phoneError = phoneError,
            passwordError = passwordError,
            confirmPasswordError = confirmPasswordError,
            termsError = termsError
        )

        // Si alguna validación falló, no se continúa
        val hasError = listOf(nameError, emailError, phoneError, passwordError, confirmPasswordError, termsError).any { it != null }
        if (hasError) {
            return
        }

        // Si todo está OK, se llama al repositorio
        viewModelScope.launch {
            val success = repository.registerUser(
                name = state.name,
                email = state.email,
                fullPhoneNumber = "${state.phonePrefix}${state.phoneNumber}",
                password = state.password
            )
            if (success) {
                onSuccess()
            } else {
                // Si el repositorio devuelve 'false' (ej: email duplicado)
                registerFormState = registerFormState.copy(emailError = "El email ya está en uso o hubo un error.")
            }
        }
    }

    // --- FUNCIONES Y LÓGICA PARA EL LOGIN (Sin cambios, ya estaban bien) ---
    fun onLoginEmailChange(email: String) {
        loginFormState = loginFormState.copy(email = email, emailError = null, generalError = null)
    }

    fun onLoginPasswordChange(password: String) {
        loginFormState = loginFormState.copy(password = password, passwordError = null, generalError = null)
    }

    fun login(onSuccess: () -> Unit) {
        val state = loginFormState
        val emailError = validateEmail(state.email)
        val passwordError = if (state.password.isBlank()) "Debes escribir tu contraseña" else null

        loginFormState = state.copy(
            emailError = emailError,
            passwordError = passwordError
        )

        val hasError = emailError != null || passwordError != null
        if (hasError) return

        viewModelScope.launch {
            val authToken = repository.loginUser(state.email, state.password)
            if (authToken != null) {
                println("ViewModel: Login exitoso, token: $authToken")
                onSuccess()
            } else {
                loginFormState = loginFormState.copy(generalError = "Email o contraseña incorrectos.")
            }
        }
    }

    fun logout() {
        println("ViewModel: Cerrando sesión.")
    }
}
