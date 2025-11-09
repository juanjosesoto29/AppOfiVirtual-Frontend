package com.example.ofivirtualapp.data.remote

import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    @POST("api/v1/users/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<UserResponse>

    @POST("api/v1/users/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<UserResponse>

    @GET("api/v1/users/{email}")
    suspend fun getByEmail(
        @Path("email") email: String
    ): Response<UserResponse>
}
