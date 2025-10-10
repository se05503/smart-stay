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
import com.example.smartstay.model.TMapTravelSpecificAccommodationRankingResponse
import com.example.smartstay.model.TMapTravelAccommodationResponse
import com.example.smartstay.model.TMapTravelDailyVisitorResponse
import com.example.smartstay.model.TMapTravelDistrictAccommodationRankingResponse
import com.example.smartstay.model.TMapTravelDistrictAccommodationThemeRankingResponse
import com.example.smartstay.model.TMapTravelDistrictDurationResponse
import com.example.smartstay.model.TMapTravelDistrictResponse
import com.example.smartstay.model.TMapTravelDistrictsAccommodationVisitorSegmentsResponse
import com.example.smartstay.model.TMapTravelMonthlyVisitorResponse
import com.example.smartstay.model.TMapTravelPopularRestaurantsNearbyResponse
import com.example.smartstay.model.TMapTravelPopularRestaurantsNearbySegmentRateResponse
import com.example.smartstay.model.TMapTravelPopularSpotsNearbyResponse
import com.example.smartstay.model.TMapTravelPopularSpotsNearbySegmentRateResponse
import com.example.smartstay.model.TMapTravelSimilarAccommodationResponse
import com.example.smartstay.model.TMapTravelSpecificAccommodationFeatureResponse
import com.example.smartstay.model.TMapTravelSpecificAccommodationVisitorSegmentsResponse
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

    private val _tMapTravelDistrictsInfo: MutableLiveData<TMapTravelDistrictResponse> =
        MutableLiveData()
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

    private val _tMapTravelMonthlyVisitorsInfo: MutableLiveData<TMapTravelMonthlyVisitorResponse> = MutableLiveData()
    val tMapTravelMonthlyVisitorsInfo: LiveData<TMapTravelMonthlyVisitorResponse> get() = _tMapTravelMonthlyVisitorsInfo

    fun getTravelMonthlyVisitorsCount(
        context: Context,
        districtCode: String,
        yearMonth: String = "latest",
        gender: String = "all",
        ageGrp: String = "all",
        companionType: String = "all"
    ) {
        viewModelScope.launch {
            try {
                _tMapTravelMonthlyVisitorsInfo.value = networkService.getTravelMonthlyVisitorsCount(
                    appKey = context.getString(R.string.sk_telecom_open_api_app_key),
                    districtCode = districtCode,
                    yearMonth = yearMonth,
                    gender = gender,
                    ageGrp = ageGrp,
                    companionType = companionType
                )
            } catch (e: Exception) {
                Log.e(TMapVectorFragment.TAG, "getTravelVisitorsCountMonthly: ${e.message}")
            }
        }
    }

    private val _tMapTravelDailyVisitorsInfo: MutableLiveData<TMapTravelDailyVisitorResponse> = MutableLiveData()
    val tMapTravelDailyVisitorsInfo: LiveData<TMapTravelDailyVisitorResponse> get() = _tMapTravelDailyVisitorsInfo

    fun getTravelDailyVisitorsCount(
        context: Context,
        districtCode: String,
        gender: String = "all",
        ageGrp: String = "all",
        companionType: String = "all"
    ) {
        viewModelScope.launch {
            try {
                _tMapTravelDailyVisitorsInfo.value = networkService.getTravelDailyVisitorsCount(
                    appKey = context.getString(R.string.sk_telecom_open_api_app_key),
                    districtCode = districtCode,
                    gender = gender,
                    ageGrp = ageGrp,
                    companionType = companionType
                )
            } catch (e: Exception) {
                Log.e(TMapVectorFragment.TAG, "getTravelVisitorsCountDaily: ${e.message}")
            }
        }
    }

    private val _tMapTravelDistrictDurationInfo: MutableLiveData<TMapTravelDistrictDurationResponse> = MutableLiveData()
    val tMapTravelDistrictDurationInfo: LiveData<TMapTravelDistrictDurationResponse> get() = _tMapTravelDistrictDurationInfo

    fun getTravelMonthlyDistrictDuration(context: Context, districtCode: String, yearMonth: String = "latest") {
        viewModelScope.launch {
            try {
                _tMapTravelDistrictDurationInfo.value = networkService.getTravelMonthlyDistrictDuration(
                    appKey = context.getString(R.string.sk_telecom_open_api_app_key),
                    districtCode = districtCode,
                    yearMonth = yearMonth
                )
            } catch (e: Exception) {
                Log.e(TMapVectorFragment.TAG, "getTravelMonthlyDistrictDuration: ${e.message}")
            }
        }
    }

    private val _tMapTravelAccommodationRankingInfo: MutableLiveData<TMapTravelAccommodationRankingResponse> = MutableLiveData()
    val tMapTravelAccommodationRankingInfo: LiveData<TMapTravelAccommodationRankingResponse> get() = _tMapTravelAccommodationRankingInfo
    private val _tMapTravelSpecificAccommodationRankingInfo: MutableLiveData<TMapTravelSpecificAccommodationRankingResponse> = MutableLiveData()
    val tMapTravelSpecificAccommodationRankingInfo: LiveData<TMapTravelSpecificAccommodationRankingResponse> get() = _tMapTravelSpecificAccommodationRankingInfo

    fun getTravelSpecificAccommodationRanking(context: Context, poiId: String) {
        viewModelScope.launch {
            try {
                _tMapTravelSpecificAccommodationRankingInfo.value = networkService.getTravelSpecificAccommodationRanking(
                    appKey = context.getString(R.string.sk_telecom_open_api_app_key),
                    poiId = poiId
                )
            } catch (e: Exception) {
                Log.e(TMapVectorFragment.TAG, "getTravelSpecificAccommodationRanking: ${e.message}")
            }
        }
    }

    private val _tMapTravelDistrictAccommodationRankingInfo: MutableLiveData<TMapTravelDistrictAccommodationRankingResponse> = MutableLiveData()
    val tMapTravelDistrictAccommodationRankingInfo: LiveData<TMapTravelDistrictAccommodationRankingResponse> get() = _tMapTravelDistrictAccommodationRankingInfo

    fun getTravelDistrictAccommodationRanking(context: Context, districtCode: String) {
        viewModelScope.launch {
            try {
                _tMapTravelDistrictAccommodationRankingInfo.value = networkService.getTravelDistrictAccommodationRanking(
                    appKey = context.getString(R.string.sk_telecom_open_api_app_key),
                    districtCode = districtCode
                )
            } catch (e: Exception) {
                Log.e(TMapVectorFragment.TAG, "getTravelDistrictAccommodationRanking: ${e.message}")
            }
        }
    }

    private val _tMapTravelDistrictAccommodationThemeRankingInfo: MutableLiveData<TMapTravelDistrictAccommodationThemeRankingResponse> = MutableLiveData()
    val tMapTravelDistrictAccommodationThemeRankingInfo: LiveData<TMapTravelDistrictAccommodationThemeRankingResponse> get() = _tMapTravelDistrictAccommodationThemeRankingInfo

    fun getTravelDistrictAccommodationThemeRanking(context: Context, theme: String, districtCode: String, companionType: String, gender: String = "all", ageGrp: String = "all") {
        viewModelScope.launch {
            try {
                _tMapTravelDistrictAccommodationThemeRankingInfo.value = networkService.getTravelDistrictAccommodationThemeRanking(
                    appKey = context.getString(R.string.sk_telecom_open_api_app_key),
                    theme = theme,
                    districtCode = districtCode,
                    companionType = companionType,
                    gender = gender,
                    ageGrp = ageGrp
                )
            } catch (e: Exception) {
                Log.e(TMapVectorFragment.TAG, "getTravelDistrictAccommodationThemeRanking: ${e.message}")
            }
        }
    }

    private val _tMapTravelSpecificAccommodationFeatureInfo: MutableLiveData<TMapTravelSpecificAccommodationFeatureResponse> = MutableLiveData()
    val tMapTravelSpecificAccommodationFeatureInfo: LiveData<TMapTravelSpecificAccommodationFeatureResponse> get() = _tMapTravelSpecificAccommodationFeatureInfo

    fun getTravelSpecificAccommodationFeature(context: Context, poiId: String, type: String) {
        viewModelScope.launch {
            try {
                _tMapTravelSpecificAccommodationFeatureInfo.value = networkService.getTravelSpecificAccommodationFeature(
                    appKey = context.getString(R.string.sk_telecom_open_api_app_key),
                    poiId = poiId,
                    type = type
                )
            } catch (e: Exception) {
                Log.e(TMapVectorFragment.TAG, "getTravelSpecificAccommodationFeature: ${e.message}")
            }
        }
    }

    private val _tMapTravelSpecificAccommodationVisitorSegmentsInfo: MutableLiveData<TMapTravelSpecificAccommodationVisitorSegmentsResponse> = MutableLiveData()
    val tMapTravelSpecificAccommodationVisitorSegmentsInfo: LiveData<TMapTravelSpecificAccommodationVisitorSegmentsResponse> get() = _tMapTravelSpecificAccommodationVisitorSegmentsInfo

    fun getTravelSpecificAccommodationVisitorSegmentsRate(context: Context, poiId: String) {
        viewModelScope.launch {
            try {
                _tMapTravelSpecificAccommodationVisitorSegmentsInfo.value = networkService.getTravelSpecificAccommodationVisitorSegmentsRate(
                    appKey = context.getString(R.string.sk_telecom_open_api_app_key),
                    poiId = poiId,
                )
            } catch (e: Exception) {
                Log.e(TMapVectorFragment.TAG, "getTravelSpecificAccommodationVisitorSegmentsRate: ${e.message}")
            }
        }
    }

    private val _tMapTravelDistrictsAccommodationVisitorSegmentsInfo: MutableLiveData<TMapTravelDistrictsAccommodationVisitorSegmentsResponse> = MutableLiveData()
    val tMapTravelDistrictsAccommodationVisitorSegmentsInfo: LiveData<TMapTravelDistrictsAccommodationVisitorSegmentsResponse> get() = _tMapTravelDistrictsAccommodationVisitorSegmentsInfo

    fun getTravelDistrictsAccommodationVisitorSegmentsRate(context: Context, districtCode: String) {
        viewModelScope.launch {
            try {
                _tMapTravelDistrictsAccommodationVisitorSegmentsInfo.value = networkService.getTravelDistrictsAccommodationVisitorSegmentsRate(
                    appKey = context.getString(R.string.sk_telecom_open_api_app_key),
                    districtCode = districtCode
                )
            } catch (e: Exception) {
                Log.e(TMapVectorFragment.TAG, "getTravelDistrictsAccommodationVisitorSegmentsRate: ${e.message}")
            }
        }
    }

    private val _tMapTravelSimilarAccommodationInfo: MutableLiveData<TMapTravelSimilarAccommodationResponse> = MutableLiveData()
    val tMapTravelSimilarAccommodationInfo: LiveData<TMapTravelSimilarAccommodationResponse> get() = _tMapTravelSimilarAccommodationInfo

    fun getTravelSimilarAccommodation(context: Context, poiId: String, type: String) {
        viewModelScope.launch {
            try {
                _tMapTravelSimilarAccommodationInfo.value = networkService.getTravelSimilarAccommodation(
                    appKey = context.getString(R.string.sk_telecom_open_api_app_key),
                    poiId = poiId,
                    type = type
                )
            } catch (e: Exception) {
                Log.e(TMapVectorFragment.TAG, "getTravelSimilarAccommodation: ${e.message}")
            }
        }
    }

    private val _tMapTravelPopularRestaurantsNearbyInfo: MutableLiveData<TMapTravelPopularRestaurantsNearbyResponse> = MutableLiveData()
    val tMapTravelPopularRestaurantsNearbyInfo: LiveData<TMapTravelPopularRestaurantsNearbyResponse> get() = _tMapTravelPopularRestaurantsNearbyInfo

    fun getTravelPopularRestaurantsNearby(context: Context, poiId: String, category: String) {
        viewModelScope.launch {
            try {
                _tMapTravelPopularRestaurantsNearbyInfo.value = networkService.getTravelPopularRestaurantsNearby(
                    appKey = context.getString(R.string.sk_telecom_open_api_app_key),
                    poiId = poiId,
                    category = category
                )
            } catch (e: Exception) {
                Log.e(TMapVectorFragment.TAG, "getTravelPopularRestaurantsNearby: ${e.message}")
            }
        }
    }

    private val _tMapTravelPopularRestaurantsNearbySegmentRateInfo: MutableLiveData<TMapTravelPopularRestaurantsNearbySegmentRateResponse> = MutableLiveData()
    val tMapTravelPopularRestaurantsNearbySegmentRateInfo: LiveData<TMapTravelPopularRestaurantsNearbySegmentRateResponse> get() = _tMapTravelPopularRestaurantsNearbySegmentRateInfo

    fun getTravelPopularRestaurantsNearBySegmentRate(context: Context, poiId: String, gender: String, ageGrp: String) {
        viewModelScope.launch {
            try {
                _tMapTravelPopularRestaurantsNearbySegmentRateInfo.value = networkService.getTravelPopularRestaurantsNearBySegmentRate(
                    appKey = context.getString(R.string.sk_telecom_open_api_app_key),
                    poiId = poiId,
                    gender = gender,
                    ageGrp = ageGrp
                )
            } catch (e: Exception) {
                Log.e(TMapVectorFragment.TAG, "getTravelPopularRestaurantsNearBySegmentRate: ${e.message}")
            }
        }
    }

    private val _tMapTravelPopularSpotsNearbyInfo: MutableLiveData<TMapTravelPopularSpotsNearbyResponse> = MutableLiveData()
    val tMapTravelPopularSpotsNearbyInfo: LiveData<TMapTravelPopularSpotsNearbyResponse> get() = _tMapTravelPopularSpotsNearbyInfo

    fun getTravelPopularSpotsNearby(context: Context, poiId: String, category: String) {
        viewModelScope.launch {
            try {
                _tMapTravelPopularSpotsNearbyInfo.value = networkService.getTravelPopularSpotsNearby(
                    appKey = context.getString(R.string.sk_telecom_open_api_app_key),
                    poiId = poiId,
                    category = category
                )
            } catch (e: Exception) {
                Log.e(TMapVectorFragment.TAG, "getTravelPopularSpotsNearby: ${e.message}")
            }
        }
    }

    private val _tMapTravelPopularSpotsNearbySegmentRateInfo: MutableLiveData<TMapTravelPopularSpotsNearbySegmentRateResponse> = MutableLiveData()
    val tMapTravelPopularSpotsNearbySegmentRateInfo: LiveData<TMapTravelPopularSpotsNearbySegmentRateResponse> get() = _tMapTravelPopularSpotsNearbySegmentRateInfo

    fun getTravelPopularSpotsNearbySegmentRate(context: Context, poiId: String, gender: String, ageGrp: String) {
        viewModelScope.launch {
            try {
                _tMapTravelPopularSpotsNearbySegmentRateInfo.value = networkService.getTravelPopularSpotsNearbySegmentRate(
                    appKey = context.getString(R.string.sk_telecom_open_api_app_key),
                    poiId = poiId,
                    gender = gender,
                    ageGrp = ageGrp
                )
            } catch (e: Exception) {
                Log.e(TMapVectorFragment.TAG, "getTravelPopularSpotsNearbySegmentRate: ${e.message}")
            }
        }
    }
}