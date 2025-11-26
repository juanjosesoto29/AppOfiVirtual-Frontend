package com.example.ofivirtualapp.data.remote.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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