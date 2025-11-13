package com.example.ofivirtualapp.data.remote.ticket

data class TicketResponse(
    val id: Long,
    val userId: Long,
    val empresaId: Long?,
    val asunto: String,
    val descripcion: String,
    val estado: String,
    val fechaCreacion: String
)
