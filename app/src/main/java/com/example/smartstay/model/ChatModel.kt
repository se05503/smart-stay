package com.example.smartstay.model

import com.example.smartstay.model.accommodation.Destination


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
        val destinations: List<Destination>? = null
    ) : ChatModel()

    object ChatBotLoading: ChatModel()

}