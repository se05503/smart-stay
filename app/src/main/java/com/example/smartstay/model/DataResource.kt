package com.example.smartstay.model

sealed class DataResource<out T> {
    data class Success<out T>(val data: T): DataResource<T>()
    data class Error(val throwable: Throwable): DataResource<Nothing>()
    object Loading: DataResource<Nothing>()
}
