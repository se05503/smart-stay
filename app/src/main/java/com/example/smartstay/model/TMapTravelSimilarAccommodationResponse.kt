package com.example.smartstay.model

data class TMapTravelSimilarAccommodationResponse(
    val status: CommonStatus,
    val contents: SimilarAccommodationContents
)

data class SimilarAccommodationContents(
    val poiId: String, // 숙소 코드
    val poiName: String, // 숙소명
    val category: String, // 숙소 분류
    val lat: Double, // 위도
    val lng: Double, // 경도
    val stat: List<SimilarAccommodationStat>,
    val statStartDate: String, // 통계를 산출한 데이터의 시작일
    val statEndDate: String, // 통계를 산출한 데이터의 종료일
    val yearMonth: String, // 통계 기준 년월 ex) 202301
)

data class SimilarAccommodationStat(
    val poiId: String, // 숙소 코드
    val poiName: String, // 숙소명
    val category: String, // 숙소 분류
    val lat: Double, // 위도
    val lng: Double, // 경도
    val similarity: Double // 유사도
)
