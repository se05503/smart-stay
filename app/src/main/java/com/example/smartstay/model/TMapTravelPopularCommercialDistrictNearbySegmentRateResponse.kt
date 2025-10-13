package com.example.smartstay.model

data class TMapTravelPopularCommercialDistrictNearbySegmentRateResponse(
    val status: CommonStatus,
    val contents: PopularCommercialDistrictNearbySegmentRateContents
)

data class PopularCommercialDistrictNearbySegmentRateContents(
    val poiId: String,
    val poiName: String,
    val category: String,
    val lat: Double,
    val lng: Double,
    val ageGrp: String,
    val gender: String,
    val stat: List<PopularCommercialDistrictNearbySegmentRateStat>,
    val statStartDate: String,
    val statEndDate: String,
    val yearMonth: String
)

data class PopularCommercialDistrictNearbySegmentRateStat(
    val areaId: String, // 상권 코드
    val areaName: String, // 상권명
    val lat: Double,
    val lng: Double,
    val rate: Double, // 방문 비율
    val distance: Double, // 숙소로부터의 거리
)
