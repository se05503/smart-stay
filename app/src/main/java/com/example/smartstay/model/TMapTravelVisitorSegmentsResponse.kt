package com.example.smartstay.model

data class TMapTravelVisitorSegmentsResponse(
    val status: CommonStatus,
    val contents: VisitorSegmentsContents
)

data class VisitorSegmentsContents(
    val poiId: String, // 조회한 숙소 코드
    val poiName: String, // 숙소명
    val category: String, // 숙소 분류
    val lat: Double, // 위도
    val lng: Double, // 경도
    val stat: List<VisitorStats>,
    val statStartDate: String, // 통계를 산출한 데이터의 시작일
    val statEndDate: String, // 통계를 산출한 데이터의 종료일
    val yearMonth: String, // 통계 기준 년월 ex) 202301
)

data class VisitorStats(
    val gender: String, // male: 남성, female: 여성
    val ageGrp: String, // 0: 10세 미만, 10: 10대, 100_over: 100세 이상
    val rate: Double // 방문자 성별, 연령대가 전체에서 차지하는 비율
)
