package com.example.smartstay.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

sealed class ChatModel {

    data class UserMessage(
        val profile: String?,
        val nickname: String?,
        val message: String,
    ) : ChatModel()

    data class ChatBotMessage(
        val type: Int,
        val message: String,
        val keywords: List<String>? = null,
        val accommodationInfo: List<AccommodationInfo>? = null
    ) : ChatModel()

    object ChatBotLoading: ChatModel()

}

data class UserInput(
    val sexCode: String,
    val age: Int,
    val job: String,
    val marriage: String,
    val children: String,
    val familyCount: String,
    val income: Float,
    val isCompanionExist: String
): Serializable

data class AccommodationInfo(
    @SerializedName("숙박업명")
    val name: String, // 숙박업명
    @SerializedName("숙박유형명")
    val type: String, // 숙박유형명
    val image: Int = 0, // 숙박업 이미지(서버x)
    val address: String, // 숙박업도로명주소
    val latitude: Float = 0f, // 위도 (y)
    val longitude: Float = 0f, // 경도 (x)
    val minimumPrice: Int, // 숙박업최저가격
    @SerializedName("숙박업평균가격")
    val averagePrice: Int, // 숙박업평균가격
    val maximumPrice: Int, // 숙박업최대가격
    val starRating: String, // 숙박업등급값(3성, 4성)
    val finalRating: Int, // 최종평점
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
): Serializable