package com.example.smartstay.model

data class TMapTravelDistrictDurationResponse(
    val status: CommonStatus,
    val contents: MonthlyDurationContents
)

data class MonthlyDurationContents(
    val districtCode: String, // 검색 여행지의 법정동 코드(10자리 숫자)
    val districtName: String, // 검색 여행지의 법정동 이름
    val raw: MonthlyDurationRaw // 월별 평균 체류시간 정보
)

data class MonthlyDurationRaw(
    val avgVisitDuration: Int, // 월별 평균 체류시간(단위: 초)
    val yearMonth: String // 검색 기준 월(YYYYMM)
)
