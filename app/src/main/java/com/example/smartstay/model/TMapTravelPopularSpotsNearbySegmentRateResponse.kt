package com.example.smartstay.model

data class TMapTravelPopularSpotsNearbySegmentRateResponse(
    val status: CommonStatus,
    val contents: PopularSpotsNearbySegmentRateContents
)

data class PopularSpotsNearbySegmentRateContents(
    val poiId: String,
    val poiName: String,
    val category: String,
    val lat: Double,
    val lng: Double,
    val ageGrp: String,
    val gender: String,
    val stat: List<PopularSpotsNearbySegmentRateStat>,
    val statStartDate: String,
    val statEndDate: String,
    val yearMonth: String
)

data class PopularSpotsNearbySegmentRateStat(
    val poiId: String,
    val poiName: String,
    val category: String,
    val lat: Double,
    val lng: Double,
    val congestionYn: String, // 장소 혼잡도 유무
    val rate: Double, // 방문 비율
    val distance: Double, // 숙소로부터의 거리
    val repCompanion: String? = null // 대표 동반자(representative companion) 유형 → 대표 동반자 유형이 해당 장소에 있을 때만 값을 제공합니다.
)
