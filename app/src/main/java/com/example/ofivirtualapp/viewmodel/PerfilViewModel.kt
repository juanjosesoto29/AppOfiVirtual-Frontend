package com.example.ofivirtualapp.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ofivirtualapp.data.local.storage.UserPreferences
import com.example.ofivirtualapp.data.repository.UserRepository
import com.example.ofivirtualapp.ui.theme.screen.UserProfile
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


data class PerfilUiState(
    val user: UserProfile = UserProfile(nombre = "Cargando...", email = "", telefono = "", planNombre = "", planEstadoVigente = false, planVence = ""),
    val avatarUri: Uri? = null,
    val isLoading: Boolean = true
)

class PerfilViewModel(
    private val userRepository: UserRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState = _uiState.asStateFlow()

    fun loadUserProfile() {

        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {

            val email = userPreferences.userEmail.firstOrNull()
            if (email != null) {

                val userEntity = userRepository.getUserByEmail(email)
                if (userEntity != null) {

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
                    _uiState.update { it.copy(isLoading = false, user = it.user.copy(nombre = "Usuario no encontrado")) }
                }
            } else {

                _uiState.update { it.copy(isLoading = false, user = it.user.copy(nombre = "Error al cargar")) }
            }
        }
    }

    fun onAvatarChange(uri: Uri?) {
        _uiState.update { currentState ->
            currentState.copy(avatarUri = uri)
        }
    }
}
