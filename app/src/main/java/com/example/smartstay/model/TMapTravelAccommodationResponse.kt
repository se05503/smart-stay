package com.example.smartstay.model

data class TMapTravelAccommodationResponse(
    val status: CommonStatus,
    val contents: List<AccommodationContents>
)

data class AccommodationContents(
    val poiId: String, // point of interest ID = 관심 지점 ID = 숙소 분석 데이터를 조회할 때 사용하는 숙소 코드 ex) 6453368
    val poiName: String, // 숙소명 ex) 디아넥스
    val category: String, // 숙소 분류 ex) 3성급
    val lat: Float, // 위도 ex) 33.285972969111235
    val lng: Float, // 경도 ex) 126.42742701939217
    val districtCode: String, // 숙소 소재지 법정동 코드. "행정표준코드관리시스템"에서 코드를 검색할 수 있음
    val addr: String, // 도로명 주소 ex) 제주특별자치도 서귀포시 산록남로762번길 71
    val phoneNumber: String // 전화번호 ex) 064-793-6005
)




