package com.example.smartstay

import com.example.smartstay.model.ChatRequest
import com.example.smartstay.model.SocialLoginRequest
import com.example.smartstay.model.SocialLoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NetworkService {

    @POST("/social-login")
    fun postSocialLogin(@Body request: SocialLoginRequest):Call<SocialLoginResponse>

    @POST("/social-chat")
    fun postChat(@Body request: ChatRequest): Call<String>
}