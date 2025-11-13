package com.example.ofivirtualapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ofivirtualapp.data.remote.ticket.TicketRequest
import com.example.ofivirtualapp.data.remote.ticket.TicketResponse
import com.example.ofivirtualapp.data.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class TicketUiState(
    val isLoading: Boolean = false,
    val tickets: List<TicketResponse> = emptyList(),
    val errorMsg: String? = null,
    val successMsg: String? = null
)

class TicketViewModel(
    private val repo: TicketRepository = TicketRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(TicketUiState())
    val uiState: StateFlow<TicketUiState> = _uiState.asStateFlow()

    fun loadTickets() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMsg = null, successMsg = null)
        viewModelScope.launch {
            try {
                val response = repo.getAll()
                if (response.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        tickets = response.body() ?: emptyList()
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMsg = "Error al cargar tickets (${response.code()})"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMsg = "Error de conexión al cargar tickets"
                )
            }
        }
    }

    fun createTicket(userId: Long, empresaId: Long? = null, asunto: String, descripcion: String) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMsg = null, successMsg = null)
        viewModelScope.launch {
            try {
                val req = TicketRequest(
                    userId = userId,
                    empresaId = empresaId,
                    asunto = asunto,
                    descripcion = descripcion,
                    estado = null // backend pone ABIERTO por defecto
                )
                val response = repo.create(req)
                if (response.isSuccessful) {
                    // recargar la lista
                    loadTickets()
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMsg = "Ticket creado correctamente"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMsg = "No se pudo crear el ticket (${response.code()})"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMsg = "Error de conexión al crear el ticket"
                )
            }
        }
    }

    fun deleteTicket(id: Long) {
        _uiState.value = _uiState.value.copy(isLoading = true, errorMsg = null, successMsg = null)
        viewModelScope.launch {
            try {
                val response = repo.delete(id)
                if (response.isSuccessful || response.code() == 204) {
                    loadTickets()
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMsg = "Ticket eliminado"
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMsg = "No se pudo eliminar el ticket (${response.code()})"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMsg = "Error de conexión al eliminar ticket"
                )
            }
        }
    }

    fun clearMessages() {
        _uiState.value = _uiState.value.copy(errorMsg = null, successMsg = null)
    }
}
