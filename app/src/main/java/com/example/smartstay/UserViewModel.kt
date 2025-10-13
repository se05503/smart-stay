package com.example.smartstay

import androidx.lifecycle.ViewModel
import com.example.smartstay.model.UserModel

class UserViewModel: ViewModel() {
    var userInfo: UserModel = UserModel()
    val userInitialInfoMap: MutableMap<String, Any> = mutableMapOf()
}