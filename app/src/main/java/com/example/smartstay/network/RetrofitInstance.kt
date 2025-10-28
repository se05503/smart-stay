package com.example.smartstay.network

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitInstance {

    const val BACKEND_BASE_URL = ""
    const val NAVER_MAP_BASE_URL = "https://maps.apigw.ntruss.com/"
    const val SK_TMAP_BASE_URL = "https://apis.openapi.sk.com/"

    val gson = GsonBuilder().setLenient().create()

    val backendNetworkService: BackendNetworkService by lazy {
        Retrofit.Builder()
            .baseUrl(BACKEND_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(BackendNetworkService::class.java)
    }

    val naverMapNetworkService: NaverMapNetworkService by lazy {
        Retrofit.Builder()
            .baseUrl(NAVER_MAP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(NaverMapNetworkService::class.java)
    }

    val skTMapNetworkService: SkTMapNetworkService by lazy {
        Retrofit.Builder()
            .baseUrl(SK_TMAP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(SkTMapNetworkService::class.java)
    }
}