package com.example.ofivirtualapp.data.remote

data class PlanDto(
    val id: Long,
    val nombre: String,
    val descripcion: String,
    val duracionMeses: Int,
    val precio: Double,
    val activo: Boolean
)
