package com.example.ofivirtualapp.data.remote

import retrofit2.Response
import retrofit2.http.*

interface EmpresaApi {

    @POST("api/v1/empresas")
    suspend fun createOrUpdateEmpresa(
        @Body request: EmpresaRequest
    ): Response<EmpresaResponse>

    @GET("api/v1/empresas/usuario/{userId}")
    suspend fun getEmpresaByUserId(
        @Path("userId") userId: Long
    ): Response<EmpresaResponse>
}
