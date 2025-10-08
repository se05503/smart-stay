package com.example.smartstay.model

data class TMapTravelDistrictAccommodationRankingResponse(
    val status: CommonStatus,
    val contents: DistrictAccommodationRankingContents
)

data class DistrictAccommodationRankingContents(
    val districtCode: String, // 순위를 조회한 시도 법정동 코드
    val districtName: String, // 지역명
    val stat: List<DistrictStat>, // 통화 통계, 위치 정보 기반 시도 내 숙소 순위 정보 리스트 (인기 많은 순 → 인기 적은 순)
    val statStartDate: String, // 통계를 산출한 데이터의 시작일
    val statEndDate: String, // 통계를 산출한 데이터의 종료일
    val yearMonth: String // 통계 기준 년월 ex) 202301
)

data class DistrictStat(
    val poiId: String, // 숙소 코드
    val poiName: String, // 숙소명
    val category: String, // 숙소 분류
    val lat: Float, // 위도
    val lng: Float, // 경도
    val count: Int, // 숙소 방문자 수 추정값
)
