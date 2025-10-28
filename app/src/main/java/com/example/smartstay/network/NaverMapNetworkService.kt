package com.example.smartstay.network

import com.example.smartstay.model.NaverDirectionsResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NaverMapNetworkService {
    /**
     * Naver Directions 5 API
     * reference: https://api.ncloud-docs.com/docs/application-maps-directions5
     */
    @GET("map-direction/v1/driving")
    suspend fun getNaverMapDrivingDirections(
        @Header("x-ncp-apigw-api-key-id") apiKeyId: String,
        @Header("x-ncp-apigw-api-key") apiKey: String,
        @Query("start") startLngLat: String, // ex. 127.12345,37.12345 (콤마 뒤 띄어쓰기 x)
        @Query("goal") endLngLat: String, // ex. 123.45678,34.56789 (콤마 뒤 띄어쓰기 x)
    ): NaverDirectionsResponse
}