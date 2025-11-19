package com.example.ofivirtualapp.data.remote.ticket

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TicketApi {

    @GET("api/v1/tickets")
    suspend fun getAll(): Response<List<TicketResponse>>

    @GET("api/v1/tickets/{id}")
    suspend fun getById(
        @Path("id") id: Long
    ): Response<TicketResponse>

    @POST("api/v1/tickets")
    suspend fun create(
        @Body req: TicketRequest
    ): Response<TicketResponse>

    @PUT("api/v1/tickets/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Body req: TicketRequest
    ): Response<TicketResponse>

    @DELETE("api/v1/tickets/{id}")
    suspend fun delete(
        @Path("id") id: Long
    ): Response<Unit>
}
