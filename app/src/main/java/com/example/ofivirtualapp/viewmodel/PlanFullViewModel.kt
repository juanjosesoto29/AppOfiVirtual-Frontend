package com.example.ofivirtualapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ofivirtualapp.data.remote.plan.PlanDto
import com.example.ofivirtualapp.data.remote.RetrofitClient
import com.example.ofivirtualapp.data.repository.PlanRepository
import com.example.ofivirtualapp.ui.theme.screen.PlanOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PlanFullBackendState(
    val opcionesOficinaVirtual: List<PlanOption> = listOf(
        PlanOption("Sin Oficina Virtual", 0),
        PlanOption("Plan Semestral", 86_000),
        PlanOption("Plan Anual", 126_000)
    ),
    val errorMsg: String? = null
)

class PlanFullViewModel : ViewModel() {

    private val repo = PlanRepository(RetrofitClient.planApi)

    private val _uiState = MutableStateFlow(PlanFullBackendState())
    val uiState: StateFlow<PlanFullBackendState> = _uiState

    fun loadOficinaVirtualPlanes() {
        viewModelScope.launch {
            val result = repo.getPlanes()
            _uiState.update { current ->
                result.fold(
                    onSuccess = { planes ->
                        val sem = planes.find { it.nombre.contains("Oficina Virtual Semestral", ignoreCase = true) }
                        val an = planes.find { it.nombre.contains("Oficina Virtual Anual", ignoreCase = true) }

                        val opciones = buildList {
                            add(PlanOption("Sin Oficina Virtual", 0))
                            sem?.let { add(PlanOption("Plan Semestral", it.precio.toInt())) }
                            an?.let { add(PlanOption("Plan Anual", it.precio.toInt())) }
                        }

                        current.copy(
                            opcionesOficinaVirtual = if (opciones.size > 1) opciones else current.opcionesOficinaVirtual,
                            errorMsg = null
                        )
                    },
                    onFailure = { e ->
                        current.copy(errorMsg = e.message)
                    }
                )
            }
        }
    }
}
