package com.example.ofivirtualapp.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ofivirtualapp.data.local.storage.UserPreferences
import com.example.ofivirtualapp.data.remote.RetrofitClient
import com.example.ofivirtualapp.ui.theme.screen.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PerfilUiState(
    val user: UserProfile = UserProfile(
        nombre = "Cargando...",
        email = "",
        telefono = "",
        planNombre = "",
        planEstadoVigente = false,
        planVence = ""
    ),
    val avatarUri: Uri? = null,
    val isLoading: Boolean = true
)

class PerfilViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val api = RetrofitClient.userApi

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState = _uiState.asStateFlow()

    fun loadUserProfile() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val email = userPreferences.userEmail.firstOrNull()

            if (email.isNullOrBlank()) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        user = it.user.copy(nombre = "No hay usuario guardado")
                    )
                }
                return@launch
            }

            try {
                val response = api.getByEmail(email)

                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null) {
                        _uiState.update {
                            it.copy(
                                user = UserProfile(
                                    nombre = user.name,
                                    email = user.email,
                                    telefono = user.phone,
                                    // Por ahora dejamos plan “mock” fijo:
                                    planNombre = "Plan Semestral",
                                    planEstadoVigente = true,
                                    planVence = "13/03/2026"
                                ),
                                isLoading = false
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                user = it.user.copy(nombre = "Usuario no encontrado")
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            user = it.user.copy(nombre = "Error al cargar perfil")
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        user = it.user.copy(nombre = "Error de conexión")
                    )
                }
            }
        }
    }

    fun onAvatarChange(uri: Uri?) {
        _uiState.update { current ->
            current.copy(avatarUri = uri)
        }
    }
}
