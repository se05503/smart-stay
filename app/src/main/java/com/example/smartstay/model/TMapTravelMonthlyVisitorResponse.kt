package com.example.smartstay.model

data class TMapTravelMonthlyVisitorResponse(
    val status: CommonStatus,
    val contents: MonthlyVisitorContents
)

data class MonthlyVisitorContents(
    val districtCode: String, // 검색 여행지의 법정동 코드(10자리 숫자)
    val districtName: String, // 검색 여행지의 법정동 이름 ex) 제주특별자치도 제주시
    val gender: String, // 여행자의 성별 → 요청 시 성별을 입력하지 않으면 모든 성별에 대한 데이터를 반환
    val ageGrp: String, // 여행자의 연령대 → 요청 시 연령대를 입력하지 않으면 모든 연령대에 대한 데이터를 반환
    val companionType: String, // 동반자 유형 → 요청 시 동반자 유형을 입력하지 않으면 모든 동반자 유형에 대한 데이터를 반환
    val raw: MonthlyRaw // 지정한 검색 조건에 따른 추정 여행자 수 정보
)

data class MonthlyRaw(
    val travelerCount: Int, // 추정 여행자 수
    val yearMonth: String // 추정 여행자 수가 검색된 날짜(형식: YYYYMM)
)