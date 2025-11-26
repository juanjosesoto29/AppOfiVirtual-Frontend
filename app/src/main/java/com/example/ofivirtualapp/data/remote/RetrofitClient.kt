package com.example.ofivirtualapp.data.remote

import com.example.ofivirtualapp.data.remote.auth.UserApi
import com.example.ofivirtualapp.data.remote.empresa.EmpresaApi
import com.example.ofivirtualapp.data.remote.plan.PlanApi
import com.example.ofivirtualapp.data.remote.ticket.TicketApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.ofivirtualapp.data.remote.indicadores.IndicadoresApi

object RetrofitClient {

    private const val BASE_URL_USERS = "http://10.155.84.223:8082/"
    private const val BASE_URL_PLANES = "http://10.155.84.223:8085/"
    private const val BASE_URL_SOPORTE = "http://10.155.84.223:8086/" // duoc

    //private const val BASE_URL_SOPORTE = "http://192.168.100.21:8086/" // Oficina
    //private const val BASE_URL_SOPORTE = "http://192.168.1.16:8086/"// casa

    private const val BASE_URL_EXTERNAL = "https://mindicador.cl/"

    val indicadoresApi: IndicadoresApi by lazy {
        buildRetrofit(BASE_URL_EXTERNAL).create(IndicadoresApi::class.java)
    }
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private fun buildRetrofit(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    private val retrofitUsers: Retrofit by lazy {
        buildRetrofit(BASE_URL_USERS)
    }

    val userApi: UserApi by lazy {
        retrofitUsers.create(UserApi::class.java)
    }

    val empresaApi: EmpresaApi by lazy {
        retrofitUsers.create(EmpresaApi::class.java)
    }

    val planApi: PlanApi by lazy {
        buildRetrofit(BASE_URL_PLANES).create(PlanApi::class.java)
    }

    val ticketApi: TicketApi by lazy {
        buildRetrofit(BASE_URL_SOPORTE).create(TicketApi::class.java)
    }
}
