package com.example.ofivirtualapp.data.repository

import com.example.ofivirtualapp.data.remote.indicadores.IndicadoresApi
import com.example.ofivirtualapp.data.remote.indicadores.IndicadoresResponse

class IndicadoresRepository(
    private val api: IndicadoresApi
) {
    suspend fun obtenerValores(): Result<IndicadoresResponse> {
        return try {
            val response = api.getIndicadores()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al cargar indicadores"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}