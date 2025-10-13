package com.example.smartstay.model

data class TMapTravelPopularCommercialDistrictNearbyResponse(
    val status: CommonStatus,
    val contents: PopularCommercialDistrictNearbyContents
)

data class PopularCommercialDistrictNearbyContents(
    val poiId: String,
    val poiName: String,
    val category: String,
    val lat: Double,
    val lng: Double,
    val stat: List<PopularCommercialDistrictNearbyStat>,
    val statStartDate: String,
    val statEndDate: String,
    val yearMonth: String
)

data class PopularCommercialDistrictNearbyStat(
    val areaId: String, // 상권 코드
    val areaName: String, // 상권명
    val lat: Double,
    val lng: Double,
    val count: Int, // 추정 방문 여행객수
    val distance: Double // 숙소로부터의 거리
)
