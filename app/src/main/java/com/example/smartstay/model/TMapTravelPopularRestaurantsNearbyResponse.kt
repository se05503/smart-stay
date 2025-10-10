package com.example.smartstay.model

data class TMapTravelPopularRestaurantsNearbyResponse(
    val status: CommonStatus,
    val contents: PopularRestaurantsNearbyContents
)

data class PopularRestaurantsNearbyContents(
    val poiId: String,
    val poiName: String,
    val category: String,
    val lat: Double,
    val lng: Double,
    val stat: List<PopularRestaurantsNearbyStat>,
    val statStartDate: String,
    val statEndDate: String,
    val yearMonth: String
)

data class PopularRestaurantsNearbyStat(
    val ypId: String, // 음식점 코드
    val ypName: String, // 음식점명
    val category: String, // 음식점 분류
    val lat: Double,
    val lng: Double,
    val count: Int, // 추정 방문 여행객수
    val distance: Double // 숙소로부터의 거리
)
