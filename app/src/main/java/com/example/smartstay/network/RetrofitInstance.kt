package com.example.smartstay.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitInstance {

    const val BACKEND_BASE_URL = "https://b49aadcc7d48.ngrok-free.app/"
    const val SK_TMAP_BASE_URL = "https://apis.openapi.sk.com/"
    const val OPEN_AI_BASE_URL = "https://api.openai.com/"

    val gson: Gson = GsonBuilder().setLenient().create()

    val openAIClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val original: Request = chain.request()
            val request: Request = original.newBuilder()
                .header("Authorization", "Bearer OPENAI_API_KEY")
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val backendNetworkService: BackendNetworkService by lazy {
        Retrofit.Builder()
            .baseUrl(BACKEND_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(BackendNetworkService::class.java)
    }

    val skTMapNetworkService: SkTMapNetworkService by lazy {
        Retrofit.Builder()
            .baseUrl(SK_TMAP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(SkTMapNetworkService::class.java)
    }

    val openAINetworkService: OpenAINetworkService by lazy {
        Retrofit.Builder()
            .baseUrl(OPEN_AI_BASE_URL)
            .client(openAIClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(OpenAINetworkService::class.java)
    }
}