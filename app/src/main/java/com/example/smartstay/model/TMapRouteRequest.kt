package com.example.smartstay.model

data class TMapRouteRequest(
    val startX: String,
    val startY: String,
    val endX: String,
    val endY: String
)

data class TMapRouteResponse(
    val metaData: MetaData
)

data class MetaData(
    val requestParameters: RequestParameters,
    val plan: Plan
)

data class RequestParameters(
    val busCount: Int? = null, // 버스 결과 개수
    val expressbusCount: Int? = null, // 고속/시외 버스 결과 개수
    val subwayCount: Int? = null, // 지하철 결과 개수
    val airplaneCount: Int? = null, // 항공 결과 개수
    val wideareaRouteCount: Int? = null, // 광역 노선 결과 개수
    val subwayBusCount: Int? = null, // 지하철 + 버스 결과 개수
    val ferryCount: Int? = null, // 해운 결과 개수
    val trainCount: Int? = null, // 기차 결과 개수

    val locale: String? = null, // 언어코드(ko, en)
    val endY: String, // 도착지 좌표(위도)
    val endX: String, // 도착지 좌표(경도)
    val startY: String, // 출발지 좌표(위도)
    val startX: String, // 출발지 좌표(경도)
    val reqDttm: String // 요청 일시 (YYYYMMDDhhmmss)
)

data class Plan(
    val itineraries: List<Itinerary>
)

data class Itinerary(
    val fare: Fare, // 금액 최상위 노드
    val totalTime: Int, // 총 소요 시간(sec)
    val totalWalkTime: Int? = null, // 총 보행자 소요시간(sec)
    val pathType: Int? = null, // 경로 탐색 결과 종류 (1-지하철, 2-버스, 3-버스+지하철, 4-고속/시외버스, 5-기차, 6-항공, 7-해운)
    val transferCount: Int? = null, // 환승 횟수
    val totalDistance: Int? = null, // 총 이동거리(m)
    val totalWalkDistance: Int? = null, // 총 보행자 이동 거리(m)
    val legs: List<Leg>? = null // 대중교옽 정보 최상위 노드
)

data class Fare(
    val regular: Regular // 금액 상위 노드
)

data class Regular(
    val totalFare: Int, // 대중교통요금
    val currency: Currency // 금액 상위 노드
)

data class Currency(
    val symbol: String, // 금액 상징(￦)
    val currency: String, // 금액 단위(원)
    val currencyCode: String // 금액 단위 코드(KRW)
)

data class Leg(
    val mode: String, // 이동 수단(WALK, BUS, SUBWAY, EXPRESS BUS, TRAIN, AIRPLANE, FERRY)
    val sectionTime: Int, // 구간별 소요 시간 (sec)
    val distance: Int, // 구간별 이동 거리 (m)
    val start: Location, // 시작 위치
    val end: Location, // 도착 위치
    val steps: List<Step> // 도보 이동 시 상세 경로
)

data class Location(
    val name: String, // 출발 정류장 명칭
    val lon: Double, // 경도 (Longitude)
    val lat: Double // 위도 (Latitude)
)

data class Step(
    val streetName: String, // 도로명
    val distance: Int, // 도보 이동거리(m)
    val description: String, // 도로 구간 정보
    val linestring: String // 도보 구간 좌표
)
