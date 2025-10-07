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
import com.example.smartstay.model.TMapTravelAccommodationResponse
import com.example.smartstay.model.TMapTravelDistrictResponse
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

    private val _tMapMultiModalRouteInfo: MutableLiveData<TMapRouteResponse> = MutableLiveData()
    val tMapMultiModalRouteInfo: LiveData<TMapRouteResponse> get() = _tMapMultiModalRouteInfo

    fun findTMapMultiModalRoute(tMapRouteRequest: TMapRouteRequest, context: Context) {
        viewModelScope.launch {
            try {
                _tMapMultiModalRouteInfo.value = networkService.findTMapMultiModalRoute(
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
                _tMapMultiModalRouteInfo.value = networkService.findTMapMultiModalRouteSummary(
                    appKey = context.getString(R.string.sk_telecom_open_api_app_key),
                    tMapRouteRequest = tMapRouteRequest
                )
            } catch (e: Exception) {
                Log.e(TMapVectorFragment.TAG, "findTMapMultiModalRouteSummary: ${e.message}")
            }
        }
    }

    private val _tMapTravelDestinationInfo: MutableLiveData<TMapTravelResponse> = MutableLiveData()
    val tMapTravelDestinationInfo: LiveData<TMapTravelResponse> get() = _tMapTravelDestinationInfo
    private val _tMapTravelDistrictsInfo: MutableLiveData<TMapTravelDistrictResponse> = MutableLiveData()
    val tMapTravelDistrictsInfo: LiveData<TMapTravelDistrictResponse> get() = _tMapTravelDistrictsInfo

    fun getTravelDistrictsCode(context: Context, type: String = "sig", offset: Int = 0, limit: Int = 100) {
        viewModelScope.launch {
            try {
                _tMapTravelDistrictsInfo.value = networkService.getTravelDistrictsCode(
                    appKey = context.getString(R.string.sk_telecom_open_api_app_key),
                    type = type,
                    offset = offset,
                    limit = limit
                )
            } catch (e: Exception) {
                Log.e(TMapVectorFragment.TAG, "getTravelDistrictsCode: ${e.message}")
            }
        }
    }

    private val _tMapTravelAccommodationsInfo: MutableLiveData<TMapTravelAccommodationResponse> = MutableLiveData()
    val tMapTravelAccommodationsInfo: LiveData<TMapTravelAccommodationResponse> get() = _tMapTravelAccommodationsInfo

    fun getTravelAccommodationInfo(context: Context, districtCode: String, offset: Int = 0, limit: Int = 100) {
        viewModelScope.launch {
            try {
                _tMapTravelAccommodationsInfo.value = networkService.getTravelAccommodationInfo(
                    appKey = context.getString(R.string.sk_telecom_open_api_app_key),
                    districtCode = districtCode,
                    offset = offset,
                    limit = limit
                )
            } catch (e: Exception) {
                Log.e(TMapVectorFragment.TAG, "getTravelAccommodationInfo: ${e.message}")
            }
        }
    }
}