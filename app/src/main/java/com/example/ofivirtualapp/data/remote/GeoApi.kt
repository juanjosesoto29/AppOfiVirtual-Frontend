package com.example.ofivirtualapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

data class NominatimResult(
    val display_name: String?,
    val address: AddressData?
)

data class AddressData(
    val city: String?,
    val state: String?,
    val suburb: String?,
    val country: String?
)

interface GeoApi {
    @GET("search?format=json&country=Chile")
    suspend fun searchAddress(@Query("q") query: String): List<NominatimResult>
}
