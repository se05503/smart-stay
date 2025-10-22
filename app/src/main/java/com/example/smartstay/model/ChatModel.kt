package com.example.smartstay.model

import com.example.smartstay.model.accommodation.AccommodationInfo

sealed class ChatModel {

    data class UserMessage(
        val profile: String?,
        val nickname: String?,
        val message: String,
        val keywords: List<String>
    ) : ChatModel()

    data class ChatBotMessage(
        val type: Int,
        val message: String,
        val keywords: List<String>? = null,
        val accommodationInfo: List<AccommodationInfo>? = null
    ) : ChatModel()

    object ChatBotLoading: ChatModel()

}