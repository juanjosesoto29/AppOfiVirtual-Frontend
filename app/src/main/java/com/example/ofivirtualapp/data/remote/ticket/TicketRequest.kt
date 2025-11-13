package com.example.ofivirtualapp.data.remote.ticket

data class TicketRequest(
    val userId: Long,
    val empresaId: Long? = null,
    val asunto: String,
    val descripcion: String,
    val estado: String? = null
)
