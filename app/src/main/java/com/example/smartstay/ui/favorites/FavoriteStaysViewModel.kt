package com.example.smartstay.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoriteStaysViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is favorite stays Fragment"
    }
    val text: LiveData<String> = _text
}