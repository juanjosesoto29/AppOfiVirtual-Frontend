package com.example.ofivirtualapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ofivirtualapp.data.remote.RetrofitClient
import com.example.ofivirtualapp.data.repository.AuthRepository
import com.example.ofivirtualapp.domain.validation.validateEmail
import com.example.ofivirtualapp.domain.validation.validateNameLettersOnly
import com.example.ofivirtualapp.domain.validation.validatePhoneDigitsOnly
import com.example.ofivirtualapp.domain.validation.validateStrongPassword
import com.example.ofivirtualapp.domain.validation.validateConfirm
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// -----------------------------
// UI STATE: LOGIN
// -----------------------------
data class LoginUiState(
    val email: String = "",
    val pass: String = "",
    val emailError: String? = null,
    val passError: String? = null,
    val canSubmit: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMsg: String? = null,
    val success: Boolean = false,
    val userId: Long? = null
)


// -----------------------------
// UI STATE: REGISTER
// -----------------------------
data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val pass: String = "",
    val confirm: String = "",
    val nameError: String? = null,
    val emailError: String? = null,
    val phoneError: String? = null,
    val passError: String? = null,
    val confirmError: String? = null,
    val canSubmit: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMsg: String? = null,
    val success: Boolean = false
)

class AuthViewModel : ViewModel() {

    // Repositorio que llama al microservicio usuarios-servicio
    private val repository = AuthRepository(RetrofitClient.userApi)

    // -------- LOGIN --------
    private val _login = MutableStateFlow(LoginUiState())
    val login: StateFlow<LoginUiState> = _login.asStateFlow()

    // -------- REGISTER --------
    private val _register = MutableStateFlow(RegisterUiState())
    val register: StateFlow<RegisterUiState> = _register.asStateFlow()


    //------LIAMPIA LOS CAMPOS ----
    /** Limpia TODO: campos y errores del login */
    fun resetLoginForm() {
        _login.value = LoginUiState()
    }

    /** Limpia TODO: campos y errores del registro */
    fun resetRegisterForm() {
        _register.value = RegisterUiState()
    }

    /** Para usar al cerrar sesión: limpia todo Auth */
    fun resetAuthForms() {
        resetLoginForm()
        resetRegisterForm()
    }

    // ============================================================
    // LOGIN: HANDLERS DE CAMBIO DE CAMPOS
    // ============================================================
    fun onLoginEmailChange(value: String) {
        val error = validateEmail(value)
        val current = _login.value.copy(
            email = value,
            emailError = error
        )
        _login.value = current.copy(
            canSubmit = canLoginSubmit(current, passwordError = current.passError)
        )
    }

    fun onLoginPassChange(value: String) {
        // Para login basta con que no esté vacío (puedes endurecer si quieres)
        val error = if (value.isBlank()) "La contraseña es obligatoria" else null
        val current = _login.value.copy(
            pass = value,
            passError = error
        )
        _login.value = current.copy(
            canSubmit = canLoginSubmit(current, passwordError = error)
        )
    }

    private fun canLoginSubmit(state: LoginUiState, passwordError: String?): Boolean {
        return state.email.isNotBlank() &&
                state.emailError == null &&
                state.pass.isNotBlank() &&
                passwordError == null &&
                !state.isSubmitting
    }

    // ============================================================
    // LOGIN: SUBMIT
    // ============================================================
    fun submitLogin() {
        val state = _login.value

        // Validación simple antes de llamar al backend
        val emailError = validateEmail(state.email)
        val passError = if (state.pass.isBlank()) "La contraseña es obligatoria" else null

        if (emailError != null || passError != null) {
            _login.value = state.copy(
                emailError = emailError,
                passError = passError,
                canSubmit = false
            )
            return
        }

        viewModelScope.launch {
            _login.value = state.copy(
                isSubmitting = true,
                errorMsg = null,
                success = false
            )

            val result = repository.login(state.email, state.pass)

            result
                .onSuccess { user ->
                    _login.value = _login.value.copy(
                        isSubmitting = false,
                        success = true,
                        errorMsg = null,
                        userId = user.id,
                        // opcional: email = user.email (por si quieres refrescar)
                    )
                }
                .onFailure { e ->
                    _login.value = _login.value.copy(
                        isSubmitting = false,
                        success = false,
                        errorMsg = e.message ?: "Error al iniciar sesión"
                    )
                }
        }
    }

    fun clearLoginResult() {
        _login.value = _login.value.copy(
            success = false,
            errorMsg = null,
            isSubmitting = false
        )
    }

    // ============================================================
    // REGISTER: HANDLERS DE CAMBIO DE CAMPOS
    // ============================================================
    fun onRegisterNameChange(value: String) {
        val error = validateNameLettersOnly(value)
        updateRegister { it.copy(name = value, nameError = error) }
    }

    fun onRegisterEmailChange(value: String) {
        val error = validateEmail(value)
        updateRegister { it.copy(email = value, emailError = error) }
    }

    fun onRegisterPhoneChange(value: String) {
        val error = validatePhoneDigitsOnly(value)
        updateRegister { it.copy(phone = value, phoneError = error) }
    }

    fun onRegisterPassChange(value: String) {
        val error = validateStrongPassword(value)
        // confirm también podría invalidarse
        val confirmError = validateConfirm(value, _register.value.confirm)
        updateRegister {
            it.copy(pass = value, passError = error, confirmError = confirmError)
        }
    }

    fun onRegisterConfirmChange(value: String) {
        val error = validateConfirm(_register.value.pass, value)
        updateRegister { it.copy(confirm = value, confirmError = error) }
    }

    private fun updateRegister(transform: (RegisterUiState) -> RegisterUiState) {
        val newState = transform(_register.value)
        val canSubmit = with(newState) {
            nameError == null &&
                    emailError == null &&
                    phoneError == null &&
                    passError == null &&
                    confirmError == null &&
                    name.isNotBlank() &&
                    email.isNotBlank() &&
                    phone.isNotBlank() &&
                    pass.isNotBlank() &&
                    confirm.isNotBlank() &&
                    !isSubmitting
        }
        _register.value = newState.copy(canSubmit = canSubmit)
    }

    // ============================================================
    // REGISTER: SUBMIT
    // ============================================================
    fun submitRegister() {
        val state = _register.value

        // Chequeo rápido: si no se puede enviar, no seguimos
        if (!state.canSubmit) {
            // Forzamos validación por si llegó aquí sin pasar por updateRegister
            val nameError = validateNameLettersOnly(state.name)
            val emailError = validateEmail(state.email)
            val phoneError = validatePhoneDigitsOnly(state.phone)
            val passError = validateStrongPassword(state.pass)
            val confirmError = validateConfirm(state.pass, state.confirm)

            val canSubmitFixed =
                nameError == null &&
                        emailError == null &&
                        phoneError == null &&
                        passError == null &&
                        confirmError == null &&
                        state.name.isNotBlank() &&
                        state.email.isNotBlank() &&
                        state.phone.isNotBlank() &&
                        state.pass.isNotBlank() &&
                        state.confirm.isNotBlank()

            _register.value = state.copy(
                nameError = nameError,
                emailError = emailError,
                phoneError = phoneError,
                passError = passError,
                confirmError = confirmError,
                canSubmit = canSubmitFixed
            )

            if (!canSubmitFixed) return
        }

        viewModelScope.launch {
            _register.value = _register.value.copy(
                isSubmitting = true,
                errorMsg = null,
                success = false
            )

            val result = repository.register(
                name = state.name,
                email = state.email,
                phone = state.phone,
                password = state.pass
            )

            result
                .onSuccess { _ ->
                    _register.value = _register.value.copy(
                        isSubmitting = false,
                        success = true,
                        errorMsg = null
                    )
                }
                .onFailure { e ->
                    _register.value = _register.value.copy(
                        isSubmitting = false,
                        success = false,
                        errorMsg = e.message ?: "Error al registrar usuario"
                    )
                }
        }
    }

    fun clearRegisterResult() {
        _register.value = _register.value.copy(
            success = false,
            errorMsg = null,
            isSubmitting = false
        )
    }
}
