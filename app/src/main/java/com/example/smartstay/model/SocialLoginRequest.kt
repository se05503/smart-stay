package com.example.smartstay.model

data class SocialLoginRequest(
    val provider: String, // 카카오, 네이버, 구글 식별
    val user_id: String,
    val email: String,
    val nickname: String,
    val refreshToken: String,
    val accessToken: String
)

data class SocialLoginResponse(
    val message: String, // 회원가입이 되었는가 안되었는가?
    val user: UserInfo
)

data class UserInfo(
    val provider: String = "",
    val user_id:String = "",
    val email: String? = "",
    val nickname: String? = "",
    val imageUrl: String? = ""
)

data class ChatRequest(
    val user_id: String,
    val message: String
)


