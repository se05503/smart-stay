package com.example.smartstay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartstay.model.SocialLoginRequest
import com.example.smartstay.model.SocialLoginResponse
import com.example.smartstay.network.NetworkService
import kotlinx.coroutines.launch

class LoginViewModel(private val networkService: NetworkService): ViewModel() {

    private val _socialLoginInfo: MutableLiveData<Result<SocialLoginResponse>> = MutableLiveData()
    val socialLoginInfo: LiveData<Result<SocialLoginResponse>> get() = _socialLoginInfo

    fun postSocialLogin(socialLoginRequest: SocialLoginRequest) {
        viewModelScope.launch {
            try {
                val response = networkService.postSocialLogin(socialLoginRequest)
                _socialLoginInfo.value = Result.success(response)
            } catch (e: Exception) {
                _socialLoginInfo.value = Result.failure(e)
            }

        }
    }
}