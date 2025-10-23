package com.example.smartstay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartstay.model.ChatRequest
import com.example.smartstay.network.NetworkService
import kotlinx.coroutines.launch

class ChatViewModel(private val networkService: NetworkService): ViewModel() {

    private val _chatbotResponse: MutableLiveData<Result<String>> = MutableLiveData()
    val chatbotResponse: LiveData<Result<String>> get() = _chatbotResponse

    fun postSocialChat(chatRequest: ChatRequest) {
        viewModelScope.launch {
            try {
                val response = networkService.postSocialChat(chatRequest = chatRequest)
                _chatbotResponse.value = Result.success(response)
            } catch (e: Exception) {
                _chatbotResponse.value = Result.failure(e)
            }
        }
    }
}