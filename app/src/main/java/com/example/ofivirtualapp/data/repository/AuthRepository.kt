package com.example.ofivirtualapp.data.repository

import com.example.ofivirtualapp.data.remote.auth.LoginRequest
import com.example.ofivirtualapp.data.remote.auth.RegisterRequest
import com.example.ofivirtualapp.data.remote.auth.UserApi
import com.example.ofivirtualapp.data.remote.auth.UserResponse

class AuthRepository(
    private val api: UserApi
) {

    suspend fun register(
        name: String,
        email: String,
        phone: String,
        password: String
    ): Result<UserResponse> {
        return try {
            val response = api.register(RegisterRequest(name, email, phone, password))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) Result.success(body)
                else Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Error al registrar"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<UserResponse> {
        return try {
            val response = api.login(LoginRequest(email, password))
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) Result.success(body)
                else Result.failure(Exception("Respuesta vacía del servidor"))
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Error al iniciar sesión"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
