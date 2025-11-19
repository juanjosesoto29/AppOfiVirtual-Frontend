package com.example.ofivirtualapp.viewmodel

import com.example.ofivirtualapp.data.remote.ticket.TicketResponse

data class SoporteUiState(
    val isLoading: Boolean = false,
    val tickets: List<TicketResponse> = emptyList(),
    val errorMsg: String? = null,
    val successMsg: String? = null
)
