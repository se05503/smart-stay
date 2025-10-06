package com.example.smartstay.network

import com.example.smartstay.model.ChatRequest
import com.example.smartstay.model.NaverDirectionsResponse
import com.example.smartstay.model.TMapRouteRequest
import com.example.smartstay.model.SocialLoginRequest
import com.example.smartstay.model.SocialLoginResponse
import com.example.smartstay.model.TMapRouteResponse
import com.example.smartstay.model.UserRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface NetworkService {

    @POST("/receive")
    fun postUserInfo(
        @Body userRequest: UserRequest
    ): Call<Any>

    @POST("/social-login")
    fun postSocialLogin(@Body request: SocialLoginRequest): Call<SocialLoginResponse>

    @POST("/social-chat")
    fun postChat(@Body request: ChatRequest): Call<String>

    /**
     * Naver Directions 5 API
     * reference: https://api.ncloud-docs.com/docs/application-maps-directions5
     */
    @GET("map-direction/v1/driving")
    suspend fun getNaverMapDrivingDirections(
        @Header("x-ncp-apigw-api-key-id") apiKeyId: String,
        @Header("x-ncp-apigw-api-key") apiKey: String,
        @Query("start") departurePoint: String, // ex. 127.12345,37.12345
        @Query("goal") arrivalPoint: String, // ex. 123.45678,34.56789
    ): NaverDirectionsResponse

    /**
     * SK Telecom's TMAP API
     * getTMapThumbnailImage = 위도, 경도 좌표에 대한 지도 썸네일 이미지 제공
     * findTMapMultiModalRoute = 출발지/목적지에 대한 대중교통 경로탐색 정보 및 전체 보행자 이동 경로 제공
     */
    @GET("tmap/staticMap")
    suspend fun getTMapThumbnailImage(
        @Header("appKey") appKey: String,
        @Header("Accept") accept: String = "application/json",
        @Query("version") version: String,
        @Query("longitude") longitude: Float,
        @Query("latitude") latitude: Float,
        @Query("width") width: Int = 512,
        @Query("height") height: Int = 512,
        @Query("zoom") zoom: Int // min 6 ~  max 19
    ): ResponseBody

    @POST("transit/routes")
    suspend fun findTMapMultiModalRoute(
        @Header("Accept") accept: String = "application/json",
        @Header("Content-Type") contentType: String = "application/json",
        @Header("appKey") appKey: String,
        @Body tMapRouteRequest: TMapRouteRequest
    ): TMapRouteResponse
}