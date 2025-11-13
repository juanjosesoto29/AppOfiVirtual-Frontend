package com.example.ofivirtualapp.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // 🔹 BACKEND AUTH / USUARIOS / EMPRESA
    // (ajusta la IP según tu PC en la red)
    // private const val BASE_URL_USERS = "http://192.168.1.109:8082/"
    private const val BASE_URL_USERS = "http://10.155.85.185:8082/"
    // 🔹 BACKEND PLANES / SUSCRIPCIONES
    private const val BASE_URL_PLANES = "http://10.155.85.185:8085/"

    // Logging para ver las peticiones/respuestas en Logcat
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    // 🔧 Función auxiliar para crear instancias de Retrofit con distinta baseUrl
    private fun buildRetrofit(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // ✅ Retrofit para USERS / EMPRESAS (8082)
    private val retrofitUsers: Retrofit by lazy {
        buildRetrofit(BASE_URL_USERS)
    }

    // 🔹 API de Usuarios (login / register)
    val userApi: UserApi by lazy {
        retrofitUsers.create(UserApi::class.java)
    }

    // 🔹 API de Empresas
    val empresaApi: EmpresaApi by lazy {
        retrofitUsers.create(EmpresaApi::class.java)
    }

    // ✅ API de Planes / Suscripciones (8085)
    val planApi: PlanApi by lazy {
        buildRetrofit(BASE_URL_PLANES).create(PlanApi::class.java)
    }
}
