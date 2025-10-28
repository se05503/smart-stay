package com.example.smartstay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartstay.model.ChatModel
import com.example.smartstay.model.ChatRequest
import com.example.smartstay.network.BackendNetworkService
import kotlinx.coroutines.launch

class ChatViewModel(private val networkService: BackendNetworkService): ViewModel() {

    private val _chatbotResponse: MutableLiveData<Result<ChatModel.ChatBotMessage>> = MutableLiveData()
    val chatbotResponse: LiveData<Result<ChatModel.ChatBotMessage>> get() = _chatbotResponse

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

    private val _convertedVoiceRecord: MutableLiveData<Result<String>> = MutableLiveData()
    val convertedVoiceRecord: LiveData<Result<String>> get() = _convertedVoiceRecord

    fun postVoiceRecord(fileName: String) {
        viewModelScope.launch {
            try {
                val response = networkService.postVoiceRecord(fileName = fileName)
                _convertedVoiceRecord.value = Result.success(response)
            } catch (e: Exception) {
                _convertedVoiceRecord.value = Result.failure(e)
            }
        }
    }
}