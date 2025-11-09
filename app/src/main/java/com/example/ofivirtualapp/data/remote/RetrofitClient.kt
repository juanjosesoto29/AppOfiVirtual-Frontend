package com.example.ofivirtualapp.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // 🔹 Base URL del backend Spring Boot
    // Si usas el EMULADOR de Android:
    private const val BASE_URL = "http://192.168.1.109:8082/"

    // "http://10.0.2.2:8082/"
    // Si pruebas en un celular físico (misma red WiFi que tu PC):
    // private const val BASE_URL = "http://192.168.X.X:8082/"   // reemplaza con tu IP local

    // Logging para ver las peticiones/respuestas en Logcat
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    // Configuración base de Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 🔹 API de Usuarios (login / register / perfil)
    val userApi: UserApi by lazy {
        retrofit.create(UserApi::class.java)
    }

    // 🔹 API de Empresas (nueva parte)
    val empresaApi: EmpresaApi by lazy {
        retrofit.create(EmpresaApi::class.java)
    }
}
