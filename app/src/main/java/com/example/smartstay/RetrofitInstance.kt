package com.example.smartstay

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitInstance {
    const val BASE_URL = "https://adbb-223-194-21-240.ngrok-free.app/"
    val gson = GsonBuilder().setLenient().create()

    val networkService: NetworkService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(NetworkService::class.java)
    }
}