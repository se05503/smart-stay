package com.example.smartstay.model

data class TMapTravelDistrictsAccommodationVisitorSegmentsResponse(
    val status: CommonStatus,
    val contents: DistrictsAccommodationVisitorSegmentsContents
)

data class DistrictsAccommodationVisitorSegmentsContents(
    val districtCode: String, // 시군구 법정동 코드
    val districtName: String, // 지역명
    val stat: List<VisitorStats>,
    val statStartDate: String, // 통계를 산출한 데이터의 시작일
    val statEndDate: String, // 통계를 산출한 데이터의 종료일
    val yearMonth: String
)
