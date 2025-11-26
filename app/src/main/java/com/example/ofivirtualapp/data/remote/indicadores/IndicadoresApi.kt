package com.example.ofivirtualapp.data.remote.indicadores

import retrofit2.Response
import retrofit2.http.GET

interface IndicadoresApi {
    @GET("api")
    suspend fun getIndicadores(): Response<IndicadoresResponse>
}