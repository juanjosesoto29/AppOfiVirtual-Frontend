package com.example.ofivirtualapp.data.repository

import com.example.ofivirtualapp.data.remote.RetrofitClient
import com.example.ofivirtualapp.data.remote.ticket.TicketRequest
import com.example.ofivirtualapp.data.remote.ticket.TicketResponse
import retrofit2.Response

class TicketRepository {

    suspend fun getAll(): Response<List<TicketResponse>> {
        return RetrofitClient.ticketApi.getAll()
    }

    suspend fun getById(id: Long): Response<TicketResponse> {
        return RetrofitClient.ticketApi.getById(id)
    }

    suspend fun create(req: TicketRequest): Response<TicketResponse> {
        return RetrofitClient.ticketApi.create(req)
    }

    suspend fun update(id: Long, req: TicketRequest): Response<TicketResponse> {
        return RetrofitClient.ticketApi.update(id, req)
    }

    suspend fun delete(id: Long): Response<Unit> {
        return RetrofitClient.ticketApi.delete(id)
    }
}
