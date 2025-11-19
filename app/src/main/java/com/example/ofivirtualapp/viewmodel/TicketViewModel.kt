package com.example.ofivirtualapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ofivirtualapp.data.remote.ticket.TicketRequest
import com.example.ofivirtualapp.data.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TicketViewModel(
    private val repo: TicketRepository = TicketRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SoporteUiState())
    val uiState: StateFlow<SoporteUiState> = _uiState.asStateFlow()

    // 🔹 Cargar todos los tickets (GET ALL)
    fun loadTickets() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMsg = null,
                successMsg = null
            )
            try {
                val resp = repo.getAll()
                if (resp.isSuccessful) {
                    val data = resp.body().orEmpty()
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        tickets = data
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMsg = "Error ${resp.code()}: ${resp.message()}"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMsg = "Error al cargar tickets: ${e.message}"
                )
            }
        }
    }

    // 🔹 Crear ticket (POST)
    fun createTicket(
        userId: Long,
        empresaId: Long?,
        asunto: String,
        descripcion: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                errorMsg = null,
                successMsg = null
            )
            try {
                val req = TicketRequest(
                    userId = userId,
                    empresaId = empresaId,
                    asunto = asunto,
                    descripcion = descripcion
                )
                val resp = repo.create(req)
                if (resp.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        successMsg = "Ticket enviado correctamente"
                    )
                    // recargar lista
                    loadTickets()
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMsg = "Error ${resp.code()}: ${resp.message()}"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMsg = "Error al crear ticket: ${e.message}"
                )
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    // 🔹 Eliminar ticket (DELETE)
    fun deleteTicket(id: Long) {
        viewModelScope.launch {
            try {
                val resp = repo.delete(id)
                if (resp.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        successMsg = "Ticket eliminado correctamente"
                    )
                    loadTickets()
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMsg = "No se pudo eliminar (código ${resp.code()})"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMsg = "Error al eliminar ticket: ${e.message}"
                )
            }
        }
    }

    // 🔹 Limpiar mensajes (lo usa tu pantalla)
    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMsg = null,
            successMsg = null
        )
    }

    // Opcionales: por si después quieres pantalla de detalle / edición
    // usan getById y update del repo (ya quedan “funcionales”)
    fun loadTicketById(id: Long, onLoaded: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                val resp = repo.getById(id)
                if (resp.isSuccessful) {
                    // aquí podrías guardar el ticket en otro estado si lo necesitas
                    onLoaded()
                }
            } catch (_: Exception) { }
        }
    }

    fun updateTicket(
        id: Long,
        userId: Long,
        empresaId: Long?,
        asunto: String,
        descripcion: String
    ) {
        viewModelScope.launch {
            try {
                val req = TicketRequest(
                    userId = userId,
                    empresaId = empresaId,
                    asunto = asunto,
                    descripcion = descripcion
                )
                val resp = repo.update(id, req)
                if (resp.isSuccessful) {
                    _uiState.value = _uiState.value.copy(
                        successMsg = "Ticket actualizado"
                    )
                    loadTickets()
                }
            } catch (_: Exception) { }
        }
    }
}
