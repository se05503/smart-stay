package com.example.smartstay.network

import com.example.smartstay.model.ChatRequest
import com.example.smartstay.model.NaverDirectionsResponse
import com.example.smartstay.model.TMapRouteRequest
import com.example.smartstay.model.SocialLoginRequest
import com.example.smartstay.model.SocialLoginResponse
import com.example.smartstay.model.TMapRouteResponse
import com.example.smartstay.model.TMapTravelAccommodationResponse
import com.example.smartstay.model.TMapTravelDistrictResponse
import com.example.smartstay.model.UserRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
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
     * findTMapMultiModalRouteSummary = 출발지/목적지에 대한 대중교통 경로탐색 요약정보 제공
     * getTravelDestination = 국내 여행 데이터를 제공할 수 있는 지역의 목록 제공
     * getTravelAccommodationInfo = 숙소 순위, 주변 관광지 등 분석 데이터를 제공하는 숙소의 코드와 숙소명 조회
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

    @POST("transit/routes/sub")
    suspend fun findTMapMultiModalRouteSummary(
        @Header("Accept") accept: String = "application/json",
        @Header("Content-Type") contentType: String = "application/json",
        @Header("appKey") appKey: String,
        @Body tMapRouteRequest: TMapRouteRequest
    ): TMapRouteResponse


    @GET("puzzle/travel/meta/districts")
    suspend fun getTravelDistrictsCode(
        @Header("appKey") appKey: String,
        @Query("type") type: String, // 검색할 법정동 유형, 지정하지 않으면 시, 군, 구 단위 여행지 정보를 반환함. ri = 동, 리 단위 / sig = 시, 군, 구 단위
        @Query("offset") offset: Int, // 여행지 목록을 조회하는 시작점 (n번째 여행지부터 조회)
        @Query("limit") limit: Int // 여행지 목록을 offset부터 시작해서 얼만큼 가져올 것인지 한도를 지정함
    ): TMapTravelResponse
    ): TMapTravelDistrictResponse

    @GET("puzzle/travel/meta/accommodation/districts/{districtCode}")
    suspend fun getTravelAccommodationInfo(
        @Header("appKey") appKey: String,
        @Path("districtCode") districtCode: String, // 숙소 목록을 조회할 시도의 법정동 코드 ex) 5000000000 = 제주특별자치도
        @Query("offset") offset: Int, // 숙소 목록을 조회하는 시작점. 예를 들면, 숙소가 총 100개 있을 때, offset을 50으로 지정하면 50번째 숙소부터 목록을 조회한다.
        @Query("limit") limit: Int // 숙소 목록을 offset부터 시작해서 얼마나 가져올 것인지 한도 지정
    ): TMapTravelAccommodationResponse

}