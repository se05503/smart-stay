package com.example.smartstay.model

sealed class ChatModel {
    data class UserMessage(
        val profile: Int,
        val nickname: String,
        val message: String
    ): ChatModel()

    data class ChatBotMessage(
        val type: Int,
        val message: String,
        val accommodationInfo: AccommodationInfo? = null
    ): ChatModel()
}

data class AccommodationInfo(
    val name: String,
    val image: Int = 0,
    val pricePerNight: Int,
    val address: String,
    val latitude: Float = 0f, // 위도 (y)
    val longitude: Float = 0f // 경도 (x)
)