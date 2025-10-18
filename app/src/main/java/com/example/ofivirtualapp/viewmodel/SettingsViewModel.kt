package com.example.ofivirtualapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Define un estado para las configuraciones, si necesitas observarlas en la UI.
data class AppSettingsState(
    val hasCompletedOnboarding: Boolean = false,
    val userAuthToken: String? = null
)

/**
 * Gestiona el estado y la persistencia de las configuraciones de la aplicación,
 * como el estado de onboarding o el token de autenticación.
 *
 * Hereda de AndroidViewModel para recibir de forma segura el contexto de la aplicación
 * y poder así interactuar con SharedPreferences.
 */
class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    // SharedPreferences es el mecanismo para guardar datos simples de forma persistente.
    private val sharedPreferences = application.getSharedPreferences(
        "OfiVirtualAppSettings", // Nombre del archivo de preferencias
        Context.MODE_PRIVATE
    )

    // StateFlow para que la UI pueda reaccionar a cambios en las configuraciones si es necesario.
    private val _uiState = MutableStateFlow(AppSettingsState())
    val uiState = _uiState.asStateFlow()

    companion object {
        // Constantes para las claves de SharedPreferences. Evita errores de tipeo.
        const val KEY_ONBOARDING_COMPLETE = "key_onboarding_complete"
        const val KEY_AUTH_TOKEN = "key_auth_token"
    }

    init {
        // Al iniciar el ViewModel, carga inmediatamente los valores guardados.
        loadInitialSettings()
    }

    private fun loadInitialSettings() {
        viewModelScope.launch {
            val onboardingComplete = sharedPreferences.getBoolean(KEY_ONBOARDING_COMPLETE, false)
            val authToken = sharedPreferences.getString(KEY_AUTH_TOKEN, null)
            _uiState.update {
                it.copy(
                    hasCompletedOnboarding = onboardingComplete,
                    userAuthToken = authToken
                )
            }
        }
    }

    /**
     * Marca que el usuario ha completado el flujo de onboarding.
     */
    fun completeOnboarding() {
        viewModelScope.launch {
            sharedPreferences.edit().putBoolean(KEY_ONBOARDING_COMPLETE, true).apply()
            _uiState.update { it.copy(hasCompletedOnboarding = true) }
        }
    }

    /**
     * Guarda el token de autenticación del usuario al iniciar sesión.
     */
    fun login(token: String, remember: Boolean) {
        viewModelScope.launch {
            if (remember) {
                sharedPreferences.edit().putString(KEY_AUTH_TOKEN, token).apply()
            }
            _uiState.update { it.copy(userAuthToken = token) }
        }
    }

    /**
     * Limpia el token de autenticación al cerrar sesión.
     */
    fun logout() {
        viewModelScope.launch {
            sharedPreferences.edit().remove(KEY_AUTH_TOKEN).apply()
            _uiState.update { it.copy(userAuthToken = null) }
        }
    }
}
