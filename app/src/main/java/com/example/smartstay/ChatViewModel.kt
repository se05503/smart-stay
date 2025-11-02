package com.example.smartstay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartstay.model.ChatModel
import com.example.smartstay.model.ChatRequest
import com.example.smartstay.network.BackendNetworkService
import com.example.smartstay.network.OpenAINetworkService
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ChatViewModel(
    private val backendNetworkService: BackendNetworkService,
    private val openAINetworkService: OpenAINetworkService
): ViewModel() {

    private val _chatbotResponse: MutableLiveData<Result<ChatModel.ChatBotMessage>> = MutableLiveData()
    val chatbotResponse: LiveData<Result<ChatModel.ChatBotMessage>> get() = _chatbotResponse

    fun postSocialChat(chatRequest: ChatRequest) {
        viewModelScope.launch {
            try {
                val response = backendNetworkService.postSocialChat(chatRequest = chatRequest)
                _chatbotResponse.value = Result.success(response)
            } catch (e: Exception) {
                _chatbotResponse.value = Result.failure(e)
            }
        }
    }

    private val _convertedVoiceRecord: MutableLiveData<Result<String>> = MutableLiveData()
    val convertedVoiceRecord: LiveData<Result<String>> get() = _convertedVoiceRecord

    fun createTranscription(filePart: MultipartBody.Part, modelPart: RequestBody) {
        viewModelScope.launch {
            try {
                val response = openAINetworkService.createTranscription(file = filePart, model = modelPart)
                _convertedVoiceRecord.value = Result.success(response.text)
            } catch (e: Exception) {
                _convertedVoiceRecord.value = Result.failure(e)
            }
        }
    }
}