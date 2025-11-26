package com.example.ofivirtualapp.data.remote.plan

import com.example.ofivirtualapp.data.remote.plan.PlanDto
import retrofit2.http.GET

interface PlanApi {

    @GET("api/v1/planes")
    suspend fun getPlanes(): List<PlanDto>
}