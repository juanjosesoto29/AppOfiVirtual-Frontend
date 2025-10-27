package com.example.ofivirtualapp.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ofivirtualapp.data.local.storage.UserPreferences
import com.example.ofivirtualapp.data.repository.UserRepository
import com.example.ofivirtualapp.ui.theme.screen.UserProfile
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// El estado de la UI ahora empieza vacío o con un estado de carga
data class PerfilUiState(
    val user: UserProfile = UserProfile(nombre = "Cargando...", email = "", telefono = "", planNombre = "", planEstadoVigente = false, planVence = ""),
    val avatarUri: Uri? = null,
    val isLoading: Boolean = true
)

// 🔹 1. AHORA RECIBE DEPENDENCIAS 🔹
class PerfilViewModel(
    private val userRepository: UserRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState = _uiState.asStateFlow()

    fun loadUserProfile() {
        // Ponemos el estado en "Cargando" al empezar
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            // Leemos el email de las preferencias
            val email = userPreferences.userEmail.firstOrNull()
            if (email != null) {
                // Buscamos el usuario en la base de datos
                val userEntity = userRepository.getUserByEmail(email)
                if (userEntity != null) {
                    // Actualizamos la UI con los datos reales
                    _uiState.update { currentState ->
                        currentState.copy(
                            user = UserProfile(
                                nombre = userEntity.name,
                                email = userEntity.email,
                                telefono = userEntity.phone,
                                planNombre = "Plan Semestral",
                                planEstadoVigente = true,
                                planVence = "13/03/2026"
                            ),
                            isLoading = false
                        )
                    }
                } else {
                    // Si el email existe pero el usuario no está en la BD (caso raro)
                    _uiState.update { it.copy(isLoading = false, user = it.user.copy(nombre = "Usuario no encontrado")) }
                }
            } else {
                // Caso error: no se encontró email
                _uiState.update { it.copy(isLoading = false, user = it.user.copy(nombre = "Error al cargar")) }
            }
        }
    }

    // La función de cambiar avatar no cambia
    fun onAvatarChange(uri: Uri?) {
        _uiState.update { currentState ->
            currentState.copy(avatarUri = uri)
        }
    }
}
