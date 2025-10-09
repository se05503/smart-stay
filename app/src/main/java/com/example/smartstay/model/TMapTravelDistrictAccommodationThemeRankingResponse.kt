package com.example.smartstay.model

data class TMapTravelDistrictAccommodationThemeRankingResponse(
    val status: CommonStatus,
    val contents: DistrictAccommodationThemeRankingContents
)

data class DistrictAccommodationThemeRankingContents(
    val districtCode: String, // 순위를 조회한 시도 법정동 코드
    val districtName: String, // 지역명 ex) 제주특별자치도
    val companionType: String, // 동반자 유형 - 신혼부부, 아이와함께, 가족여행
    val stat: List<DistrictThemeStat>,
    val statStartDate: String, // 통계를 산출한 데이터의 시작일 ex) 20230101
    val statEndDate: String, // 통계를 산출한 데이터의 종료일
    val yearMonth: String, // 통계 기준 년월 ex) 202301
)

data class DistrictThemeStat(
    val poiId: String, // 숙소 코드 ex) 6453368
    val poiName: String, // 숙소명 ex) 디아넥스
    val category: String, // 숙소 분류 ex) 5성급
    val lat: Float, // 위도
    val lng: Float, // 경도
    val rate: Float, // 테마별 방문자 빈도수
)
