package com.example.ofivirtualapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ofivirtualapp.data.remote.RetrofitClient
import com.example.ofivirtualapp.data.remote.indicadores.IndicadoresResponse
import com.example.ofivirtualapp.data.repository.IndicadoresRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class IndicadoresUiState(
    val isLoading: Boolean = false,
    val data: IndicadoresResponse? = null,
    val error: String? = null
)

class IndicadoresViewModel : ViewModel() {

    private val repository = IndicadoresRepository(RetrofitClient.indicadoresApi)

    private val _uiState = MutableStateFlow(IndicadoresUiState())
    val uiState: StateFlow<IndicadoresUiState> = _uiState

    init {
        cargarIndicadores()
    }

    fun cargarIndicadores() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = repository.obtenerValores()

            _uiState.update { state ->
                result.fold(
                    onSuccess = { datos -> state.copy(isLoading = false, data = datos) },
                    onFailure = { e -> state.copy(isLoading = false, error = e.message) }
                )
            }
        }
    }
}