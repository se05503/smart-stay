package com.example.smartstay.model.tmap

data class RoutesInfo(
    val departure: LocationInfo, // 출발지 정보
    val destination: LocationInfo, // 도착지 정보
    val predictionType: String, // [required] 안내 받을 길 안내 서비스. departure: 출발시간 예측 길 안내, arrival: 도착시간 예측 길 안내
    val predictionTime: String, // [required] 길 안내 서비스(predictionType)에 따라 반환 받을 출발 또는 도착 시간 지정. departure 인 경우 출발 시간, arrival 인 경우 도착 시간 입력. 형식: ISO-8601 표준(UTC 외 시간대의 경우 YYYY-MM-DDThh:mm:ss+hhmm) 예) 2022-09-10T09:00:00+0900
    val wayPoints: WayPoints, // [conditional] 경유지 정보
    val searchOption: String, // 경로 탐색 옵션 지정. 00(default): 교통최적+추천, 01: 교통최적+무료우선, 02: 교통최적+최소시간, ...
    val trafficInfo: String, // 교통 정보 포함 여부 지정. Y: 교통 정보 포함, N: 교통 정보를 포함하지 않음
)

data class LocationInfo(
    val name: String, // 출발지 명칭
    val lon: Double, // 경도
    val lat: Double, // 위도
    val depSearchFlag: String, // [optional] 출발지의 좌표를 얻는 방법. 03: 경위도검색(기본값), 04: 명칭검색, 05: 주소검색
)

data class WayPoints(
    val wayPoint: List<WayPoint> // [conditional] 경유지 정보
)

data class WayPoint(
    val lon: Double,
    val lat: Double,
    val poiId: String, // [optional] 경유지의 POI ID 지정. [장소 통합 검색] API 응답 메세지 중 id 항목에서 확인할 수 있습니다. 'SK T 타워'의 경우 1128603
)
