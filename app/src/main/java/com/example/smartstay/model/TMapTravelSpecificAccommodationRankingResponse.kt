package com.example.smartstay.model

data class TMapTravelSpecificAccommodationRankingResponse(
    val status: CommonStatus,
    val contents: SpecificAccommodationRankingContents // 숙소 순위 정보 객체
)

data class SpecificAccommodationRankingContents(
    val poiId: String, // 순위를 조회한 숙소의 코드
    val poiName: String, // 숙소명
    val category: String, // 숙소 분류 ex) 3성급
    val lat: Float, // 위도
    val lng: Float, // 경도
    val stat: SpecificStat, // 숙소 순위 정보 객체
    val statStartDate: String, // 통계를 산출한 데이터의 시작일
    val statEndDate: String, // 통계를 산출한 데이터의 종료일
    val yearMonth: String, // 통계 기준 년월 ex) 202301
)

data class SpecificStat(
    val ctpRanking: Int, // 통화 통계, 위치 정보 기반 "시도" 내 숙소 인기 순위
    val sigRanking: Int, // 통화 통계, 위치 정보 기반 "시군구" 내 숙소 인기 순위
    val ctpRankingChange: Int, // "시도" 숙소 인기 순위의 전월 대비 변동값
    val sigRankingChange: Int, // "시군구" 숙소 인기 순위의 전월 대비 변동값
)
