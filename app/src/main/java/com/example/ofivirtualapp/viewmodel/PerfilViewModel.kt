package com.example.ofivirtualapp.viewmodel

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.State
import com.example.ofivirtualapp.ui.theme.screen.UserProfile

// Estado de la UI para la pantalla de Perfil
data class PerfilUiState(
    val user: UserProfile = UserProfile(
        nombre = "Juanjose Soto",
        email = "juanjo@ofivirtual.cl",
        telefono = "+56 9 1234 5678",
        planNombre = "Plan Semestral",
        planEstadoVigente = true,
        planVence = "13/03"
    ),
    val avatarUri: Uri? = null // URI de la imagen seleccionada
)

class PerfilViewModel : ViewModel() {

    private val _uiState = mutableStateOf(PerfilUiState())
    val uiState: State<PerfilUiState> = _uiState

    /**
     * Actualiza el URI del avatar en el estado de la UI.
     */
    fun onAvatarChange(uri: Uri?) {
        _uiState.value = _uiState.value.copy(avatarUri = uri)
    }
}
