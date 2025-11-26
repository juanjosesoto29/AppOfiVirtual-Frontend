package com.example.ofivirtualapp.data.repository

import com.example.ofivirtualapp.data.remote.plan.PlanApi
import com.example.ofivirtualapp.data.remote.plan.PlanDto

class PlanRepository(
    private val api: PlanApi
) {
    suspend fun getPlanes(): Result<List<PlanDto>> = try {
        val planes = api.getPlanes()
        Result.success(planes)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
