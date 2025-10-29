package com.example.smartstay.model

import com.example.smartstay.model.accommodation.Destinations

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
        val destinations: List<Destinations>? = null
    ) : ChatModel()

    object ChatBotLoading: ChatModel()

}