package com.example.smartstay.model.accommodation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Destination(
    val accommodation: Accommodation,
    val attractions: List<Attraction>
): Parcelable

@Parcelize
data class Accommodation(
    val name: String, // 숙박업명
    val type: String, // 숙박유형명
    val image: List<Int>, // 숙박업 이미지(서버x)
    val address: String, // 숙박업도로명주소
    val latitude: Double, // 위도 (y)
    val longitude: Double, // 경도 (x)
    val minimumPrice: Int, // 숙박업최저가격
    val averagePrice: Int, // 숙박업평균가격
    val maximumPrice: Int, // 숙박업최대가격
    val starRating: String, // 숙박업등급값(3성, 4성)
    val isPetAvailable: String, // 반려동물가능여부
    val isRestaurantExist: String, // 레스토랑
    val isBarExist: String, // 바
    val isCafeExist: String, // 카페
    val isFitnessCenterExist: String, // 피트니스
    val isSwimmingPoolExist: String, // 수영장
    val isSpaExist: String, // 스파
    val isSaunaExist: String, // 사우나
    val isReceptionCenterExist: String, // 연회장
    val isBusinessCenterExist: String, // 비즈니스
    val isOceanViewExist: String // 오션뷰
): Parcelable

@Parcelize
data class Attraction(
    val poiId: String, // 장소 코드
    val poiName: String, // 장소명
    val category: String, // 장소 분류 → 쇼핑(shopping), 레저/스포츠(sports), 관광명소(tour)
    val lat: Double,
    val lng: Double,
    val distance: Double // 숙소로부터의 거리
): Parcelable