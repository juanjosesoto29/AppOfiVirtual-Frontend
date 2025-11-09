package com.example.ofivirtualapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ofivirtualapp.data.local.storage.UserPreferences
import com.example.ofivirtualapp.data.remote.EmpresaRequest
import com.example.ofivirtualapp.data.remote.EmpresaResponse
import com.example.ofivirtualapp.data.remote.RetrofitClient
import com.example.ofivirtualapp.data.repository.EmpresaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

data class EmpresaUiState(
    val isLoading: Boolean = false,
    val errorMsg: String? = null,
    val empresa: EmpresaResponse? = null,
    val savedOk: Boolean = false
)

class EmpresaViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val repository = EmpresaRepository(RetrofitClient.empresaApi)

    private val _uiState = MutableStateFlow(EmpresaUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    fun loadEmpresa() {
        viewModelScope.launch {
            _uiState.value = EmpresaUiState(isLoading = true)

            val userId = userPreferences.userId.firstOrNull()

            if (userId == null) {
                _uiState.value = EmpresaUiState(
                    isLoading = false,
                    errorMsg = "No se encontró el usuario logueado"
                )
                return@launch
            }

            val result = repository.getEmpresaByUserId(userId)

            result
                .onSuccess { empresa ->
                    _uiState.value = EmpresaUiState(
                        isLoading = false,
                        empresa = empresa
                    )
                }
                .onFailure { e ->
                    _uiState.value = EmpresaUiState(
                        isLoading = false,
                        errorMsg = e.message ?: "Error al cargar empresa"
                    )
                }
        }
    }

    fun saveEmpresa(form: EmpresaRequest) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMsg = null, savedOk = false)

            val userId = userPreferences.userId.firstOrNull()
            if (userId == null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMsg = "No se encontró el usuario logueado"
                )
                return@launch
            }

            // 👇 Sobrescribimos el userId que venga del form (por si está en 0L)
            val request = form.copy(userId = userId)

            val result = repository.saveEmpresa(request)

            result
                .onSuccess { empresa ->
                    _uiState.value = EmpresaUiState(
                        isLoading = false,
                        empresa = empresa,
                        savedOk = true
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMsg = e.message ?: "Error al guardar empresa",
                        savedOk = false
                    )
                }
        }
    }


    fun clearSavedFlag() {
        _uiState.value = _uiState.value.copy(savedOk = false)
    }
}
