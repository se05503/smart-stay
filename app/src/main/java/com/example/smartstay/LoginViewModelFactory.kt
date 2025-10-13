package com.example.smartstay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.smartstay.network.NetworkService

class LoginViewModelFactory(private val networkService: NetworkService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(networkService) as T
    }
}