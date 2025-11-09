package com.example.ofivirtualapp.data.remote

// Lo que ENVÍA tu app al backend

data class RegisterRequest(
    val name: String,
    val email: String,
    val phone: String,
    val password: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

// Lo que RECIBE tu app del backend

data class UserResponse(
    val id: Long,
    val name: String,
    val email: String,
    val phone: String
)
