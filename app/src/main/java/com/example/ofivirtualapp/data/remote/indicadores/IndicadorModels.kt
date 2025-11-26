package com.example.ofivirtualapp.data.remote.indicadores

data class IndicadoresResponse(
    val uf: IndicadorData,
    val dolar: IndicadorData,
    val euro: IndicadorData
)

data class IndicadorData(
    val codigo: String,
    val nombre: String,
    val valor: Double
)