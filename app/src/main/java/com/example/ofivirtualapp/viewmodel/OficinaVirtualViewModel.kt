package com.example.ofivirtualapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ofivirtualapp.data.remote.PlanDto
import com.example.ofivirtualapp.data.remote.RetrofitClient
import com.example.ofivirtualapp.data.repository.PlanRepository
import com.example.ofivirtualapp.ui.theme.screen.PlanOV
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OficinaVirtualUiState(
    val isLoading: Boolean = true,
    val planes: List<PlanOV> = emptyList(),
    val errorMsg: String? = null
)

class OficinaVirtualViewModel : ViewModel() {

    private val repository = PlanRepository(RetrofitClient.planApi)

    private val _uiState = MutableStateFlow(OficinaVirtualUiState())
    val uiState: StateFlow<OficinaVirtualUiState> = _uiState

    fun loadPlanes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMsg = null) }
            val result = repository.getPlanes()
            _uiState.update { current ->
                result.fold(
                    onSuccess = { planesDto ->
                        // Filtramos SOLO los planes de oficina virtual
                        val oficinaPlanes = planesDto.filter {
                            it.nombre.contains("Oficina Virtual", ignoreCase = true)
                        }

                        val mapped = oficinaPlanes.map { it.toPlanOV() }

                        current.copy(
                            isLoading = false,
                            planes = mapped,
                            errorMsg = null
                        )
                    },
                    onFailure = { e ->
                        current.copy(
                            isLoading = false,
                            errorMsg = e.message ?: "Error al cargar planes de Oficina Virtual"
                        )
                    }
                )
            }
        }
    }
}

// Mapeo desde DTO del backend a tu modelo de UI
private fun PlanDto.toPlanOV(): PlanOV =
    PlanOV(
        nombre = this.nombre,
        precioCLP = this.precio.toInt(),
        duracionMeses = this.duracionMeses,
        bullets = when (this.nombre) {
            "Plan Oficina Virtual Semestral" -> listOf(
                "Incluye domicilio tributario.",
                "Recepción de correspondencia."
            )
            "Plan Oficina Virtual Anual" -> listOf(
                "Incluye domicilio tributario.",
                "Recepción de correspondencia.",
                "Precio más conveniente."
            )
            else -> listOf(this.descripcion)
        }
    )
