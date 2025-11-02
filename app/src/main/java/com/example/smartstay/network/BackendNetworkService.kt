package com.example.smartstay.network

import com.example.smartstay.model.ChatModel
import com.example.smartstay.model.ChatRequest
import com.example.smartstay.model.SocialLoginRequest
import com.example.smartstay.model.SocialLoginResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface BackendNetworkService {

    @POST("social-login")
    suspend fun postSocialLogin(@Body request: SocialLoginRequest): SocialLoginResponse

    @POST("receive")
    suspend fun postSocialChat(@Body chatRequest: ChatRequest): ChatModel.ChatBotMessage
}