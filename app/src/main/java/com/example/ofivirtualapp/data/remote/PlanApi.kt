package com.example.ofivirtualapp.data.remote

import retrofit2.http.GET

interface PlanApi {

    @GET("api/v1/planes")
    suspend fun getPlanes(): List<PlanDto>
}
