package com.example.ofivirtualapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ofivirtualapp.data.remote.plan.PlanDto
import com.example.ofivirtualapp.data.remote.RetrofitClient
import com.example.ofivirtualapp.data.repository.PlanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PlanesUiState(
    val isLoading: Boolean = true,
    val planes: List<PlanDto> = emptyList(),
    val errorMsg: String? = null
)

class PlanesViewModel : ViewModel() {

    private val repository = PlanRepository(RetrofitClient.planApi)

    private val _uiState = MutableStateFlow(PlanesUiState())
    val uiState: StateFlow<PlanesUiState> = _uiState

    fun loadPlanes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMsg = null) }
            val result = repository.getPlanes()
            _uiState.update {
                result.fold(
                    onSuccess = { planes -> it.copy(isLoading = false, planes = planes, errorMsg = null) },
                    onFailure = { e -> it.copy(isLoading = false, errorMsg = e.message ?: "Error al cargar planes") }
                )
            }
        }
    }
}
