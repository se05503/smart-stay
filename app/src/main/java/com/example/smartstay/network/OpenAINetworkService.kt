package com.example.smartstay.network

import com.example.smartstay.model.TranscriptionResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface OpenAINetworkService {
    @Multipart
    @POST("v1/audio/transcriptions")
    suspend fun createTranscription(
        @Part("file") file: MultipartBody.Part,
        @Part("model") model: RequestBody
    ): TranscriptionResponse
}