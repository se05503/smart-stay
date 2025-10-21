package com.example.smartstay.model.tmap

import java.util.Date

data class TMapRoutesPredictionResponse(
    val type: String,
    val features: List<Features>
)

data class Features(
    val type: String,
    val properties: Properties
)

data class Properties(
    val totalDistance: String, // 경로 총 길이(단위: m). 안내 지점 유형이 출발지(pointType=S)일 때 응답되는 정보
    val totalTime: Int, // 경로 총 소요시간(단위: 초). 안내 지점 유형이 출발지(pointType=S)일 때 응답되는 정보
    val totalFare: String, // 경로 총 요금(단위: 원). 안내 지점 유형이 출발지(pointType=S)일 때 응답되는 정보
    val taxiFare: Int, // 택시 예상 요금(단위: 원). 안내 지점 유형이 출발지(pointType=S)일 때 응답되는 정보
    val departureTime: Date, // 출발 시간. 안내 지점 유형이 출발지(pointType=S)일 때 응답되는 정보. 형식: ISO-8601 표준(UTC 외 시간대의 경우 YYYY-MM-DDThh:mm:ss+hhmm)
    val arrivalTime: Date, // 도착 시간. 안내 지점 유형이 출발지(pointType=S)일 때 응답되는 정보. 형식: ISO-8601 표준(UTC 외 시간대의 경우 YYYY-MM-DDThh:mm:ss+hhmm)
)
