package com.example.smartstay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smartstay.network.BackendNetworkService

class ChatViewModelFactory(private val networkService: BackendNetworkService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatViewModel(networkService) as T
    }
}