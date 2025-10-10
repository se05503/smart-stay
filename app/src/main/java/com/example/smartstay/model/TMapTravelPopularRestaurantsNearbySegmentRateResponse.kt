package com.example.smartstay.model

data class TMapTravelPopularRestaurantsNearbySegmentRateResponse(
    val status: CommonStatus,
    val contents: PopularRestaurantsNearbySegmentRateContents
)

data class PopularRestaurantsNearbySegmentRateContents(
    val poiId: String,
    val poiName: String,
    val category: String,
    val lat: Double,
    val lng: Double,
    val ageGrp: String, // 연령대 구분
    val gender: String,
    val stat: List<PopularRestaurantsNearbySegmentRateStat>,
    val statStartDate: String,
    val statEndDate: String,
    val yearMonth: String
)

data class PopularRestaurantsNearbySegmentRateStat(
    val ypId: String, // 음식점 코드
    val ypName: String, // 음식점명
    val category: String,
    val lat: Double,
    val lng: Double,
    val rate: Double, // 방문 비율
    val distance: Double // 숙소로부터의 거리
)
