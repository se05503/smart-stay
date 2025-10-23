package com.example.smartstay.model

import com.example.smartstay.model.user.UserInfo

data class ChatRequest(
    val userId: String,
    val message: String,
    val keywords: List<String>,
    val userInfo: UserInfo
)
