package com.example.smartstay.model

data class NaverDirectionsResponse(
    val code: Int,
    val message: String,
    val currentDateTime: String,
    val route: Route
)

data class Route(
    val traoptimal: RouteDetail
)

data class RouteDetail(
    val guide: ArrayList<GuideDetail>,
    val path: ArrayList<String>,
    val section: ArrayList<SectionDetail>,
    val summary: SummaryDetail
)

data class GuideDetail(
    val distance: Int, // 단위: m
    val duration: Int, // 단위: millisecond
    val instructions: String, // 경로 안내 문구
    val pointIndex: Int,
    val type: Int
)

data class SectionDetail(
    val congestion: Int,
    val distance: Int, // 단위: m
    val name: String,
    val pointCount: Int,
    val pointIndex: Int,
    val speed: Int // 단위: km/h
)

data class SummaryDetail(
    val distance: Int, // 단위: m
    val duration: Int, // 전체 경로 소요 시간(밀리초)
    val tollFare: Int, // 통행(톨게이트) 요금
    val taxiFare: Int, // 택시 요금
    val fuelPrice: Int, // 유류비
)
