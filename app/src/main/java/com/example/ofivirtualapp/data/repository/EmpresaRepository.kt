package com.example.ofivirtualapp.data.repository

import com.example.ofivirtualapp.data.remote.empresa.EmpresaApi
import com.example.ofivirtualapp.data.remote.empresa.EmpresaRequest
import com.example.ofivirtualapp.data.remote.empresa.EmpresaResponse

class EmpresaRepository(
    private val api: EmpresaApi
) {

    suspend fun getEmpresaByUserId(userId: Long): Result<EmpresaResponse> {
        return try {
            val response = api.getEmpresaByUserId(userId)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Respuesta vacía"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Error al obtener empresa"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveEmpresa(request: EmpresaRequest): Result<EmpresaResponse> {
        return try {
            val response = api.createOrUpdateEmpresa(request)
            if (response.isSuccessful) {
                response.body()?.let { Result.success(it) }
                    ?: Result.failure(Exception("Respuesta vacía"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Error al guardar empresa"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
