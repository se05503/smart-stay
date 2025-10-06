package com.example.smartstay

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartstay.model.NaverDirectionsResponse
import com.example.smartstay.model.TMapRouteRequest
import com.example.smartstay.model.TMapRouteResponse
import com.example.smartstay.network.NetworkService
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class MapViewModel(private val networkService: NetworkService): ViewModel() {

    private val _tMapThumbnailImage: MutableLiveData<ResponseBody> = MutableLiveData()
    val tMapThumbnailImage: LiveData<ResponseBody> get() = _tMapThumbnailImage

    fun getTMapThumbnailImage(longitude: Float, latitude: Float, zoom: Int, context: Context) {
        viewModelScope.launch {
            try {
                _tMapThumbnailImage.value = networkService.getTMapThumbnailImage(
                    appKey = context.getString(R.string.sk_telecom_open_api_app_key),
                    version = "1",
                    longitude = longitude,
                    latitude = latitude,
                    zoom = zoom
                )
            } catch (e: Exception) {
                Log.e(NaverMapActivity.MAP_TAG, e.message ?: "")
            }

        }
    }

    private val _tMapMultiModelRouteInfo: MutableLiveData<TMapRouteResponse> = MutableLiveData()
    val tMapMultiModelRouteInfo: LiveData<TMapRouteResponse> get() = _tMapMultiModelRouteInfo

    fun findTMapMultiModalRoute(tMapRouteRequest: TMapRouteRequest, context: Context) {
        viewModelScope.launch {
            try {
                _tMapMultiModelRouteInfo.value = networkService.findTMapMultiModalRoute(
                    appKey = context.getString(R.string.sk_telecom_open_api_app_key),
                    tMapRouteRequest = tMapRouteRequest
                )
            } catch (e: Exception) {
                Log.e(TMapVectorFragment.TAG, "findTMapMultiModalRoute: ${e.message}")
            }
        }
    }

    fun findTMapMultiModalRouteSummary(tMapRouteRequest: TMapRouteRequest, context: Context) {
        viewModelScope.launch {
            try {
                _tMapMultiModelRouteInfo.value = networkService.findTMapMultiModalRouteSummary(
                    appKey = context.getString(R.string.sk_telecom_open_api_app_key),
                    tMapRouteRequest = tMapRouteRequest
                )
            } catch (e: Exception) {
                Log.e(TMapVectorFragment.TAG, "findTMapMultiModalRouteSummary: ${e.message}")
            }
        }
    }
}