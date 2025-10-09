package com.example.smartstay.model

data class TMapTravelSpecificAccommodationFeatureResponse(
    val status: CommonStatus,
    val contents: SpecificAccommodationFeatureContents
)

data class SpecificAccommodationFeatureContents(
    val poiId: String, // 조회한 숙소 코드
    val poiName: String, // 숙소명
    val category: String, // 숙소 분류 → 5성급, 4성급, ... 1성급
    val lat: Double, // 위도
    val lng: Double, // 경도
    val stat: List<FeatureStat>, // 숙소 특징 정보 배열
    val yearMonth: String, // 통계 기준 년월 ex) 202301
)

data class FeatureStat(
    val feature: String, // 특징 유형 → hot: 요즘 뜨는, longstay: 장기 숙박(3박 이상), family: 가족여행, family_w_child: 아이와 함께, newly_weds: 신혼부부, popular: 많이 찾는(percentile만 제공), male: 남성이 선호하는(percentile만 제공), young: 젊은층이 선호하는(percentile만 제공)
    val ctpPercentile: Double? = null, // 시도 내 숙소들의 해당 특징 빈도수 기준 백분위입니다. percentile로 조회했을 때 응답하는 값입니다.
    val lift: Double? = null, // 시도 내 숙소들의 해당 특징 빈도수 대비 향상도입니다. lift로 조회했을 때 응답하는 값입니다.
    val statStartDate: String, // 통계를 산출한 데이터의 시작일
    val statEndDate: String, // 통계를 산출한 데이터의 종료일
)
