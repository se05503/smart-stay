package com.example.smartstay.model

import java.sql.Date

data class TMapTravelPopularSpotsNearbyResponse(
    val status: CommonStatus,
    val contents: PopularSpotsNearbyContents
)

data class PopularSpotsNearbyContents(
    val poiId: String,
    val poiName: String,
    val category: String,
    val lat: Double,
    val lng: Double,
    val stat: List<PopularSpotsNearbyStat>,
    val statStartDate: String,
    val statEndDate: String,
    val yearMonth: String
)

data class PopularSpotsNearbyStat(
    val poiId: String, // 장소 코드
    val poiName: String, // 장소명
    val category: String, // 장소 분류 → 쇼핑, 레저/스포츠, 관광명소
    val lat: Double,
    val lng: Double,
    val congestionYn: String, // 장소 혼잡도 유무 → Y, N
    val count: Int, // 추정 방문 여행객수
    val distance: Double // 숙소로부터의 거리
)
