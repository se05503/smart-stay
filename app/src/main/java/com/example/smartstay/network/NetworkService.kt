package com.example.smartstay.network

import com.example.smartstay.model.ChatRequest
import com.example.smartstay.model.SocialLoginRequest
import com.example.smartstay.model.SocialLoginResponse
import com.example.smartstay.model.UserRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface NetworkService {

    @POST("/receive")
    fun postUserInfo(
        @Body userRequest: UserRequest
    ): Call<Any>

    @POST("/social-login")
    fun postSocialLogin(@Body request: SocialLoginRequest): Call<SocialLoginResponse>

    @POST("/social-chat")
    fun postChat(@Body request: ChatRequest): Call<String>
}