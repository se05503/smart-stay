package com.example.smartstay

import androidx.lifecycle.ViewModel
import com.example.smartstay.model.UserInfo

class InitialSettingViewModel: ViewModel() {
    var userInfo: UserInfo = UserInfo()
    val userInitialInfoMap: MutableMap<String, Any> = mutableMapOf()
}