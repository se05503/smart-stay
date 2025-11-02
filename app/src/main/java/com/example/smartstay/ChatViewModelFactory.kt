package com.example.smartstay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smartstay.network.BackendNetworkService
import com.example.smartstay.network.OpenAINetworkService

class ChatViewModelFactory(
    private val backendNetworkService: BackendNetworkService,
    private val openAINetworkService: OpenAINetworkService
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatViewModel(
            backendNetworkService = backendNetworkService,
            openAINetworkService = openAINetworkService
        ) as T
    }
}