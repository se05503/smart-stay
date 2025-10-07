package com.example.smartstay.model

data class TMapTravelResponse(
    val status: Status, // 데이터 제공 가능 여행지 검색에 대한 결과
    val contents: List<Contents> // 데이터를 제공할 수 있는 여행지 정보
)

data class Status(
    val code: String, // 응답 코드 ex) 00 = 데이터 조회 성공
    val message: String, // 요청 성공 여부 ex) success
    val totalCount: Int, // 검색 결과 데이터 건수
    val offset: Int, // 여행지 목록을 조회하는 시작점
    val limit: Int, // 데이터 조회 한도 → 여행지 목록을 offset부터 시작해서 얼마나 가져올 것인지 한도 지정
)

data class Contents(
    val districtCode: String, // 행정표준코드관리시스템에 수록된 법정동 코드로, 10자리 숫자로 구성됨 ex) 1100000000 = 서울특별시, 1114010400 = 서울특별시 중구 을지로1가
    val districtName: String // 행정표준코드관리시스템에 수록된 법정동 이름 ex) 서울특별시, 서울특별시 중구 을지로1가
)
