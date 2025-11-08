package com.example.smartstay.model.tmap

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class IntegratedPlaceResponse(
    val searchPoiInfo: SearchPoiInfo
)

data class SearchPoiInfo(
    val totalCount: Int, // 조회 결과의 총 개수
    val count: Int, // 페이지당 조회 결과 수
    val page: Int, // 조회한 페이지 번호
    val pois: Pois // 관심 장소(POI) 목록
)

data class Pois(
    val poi: List<Poi> // 검색 결과의 관심 장소(POI) 정보
)

@Parcelize
data class Poi(
    val id: String, // 관심 장소(POI) ID. 이 정보로 [장소 상세 정보 검색] API를 이용하여 장소를 상세 검색할 수 있습니다.
    val name: String, // 장소명(시설물 등) 및 업체명
    val telNo: String, // 전화번호
    val noorLat: Double, // 중심점 위도 좌표
    val noorLon: Double, // 중심점 경도 좌표

    val upperAddrName: String, // 표출 주소 대분류명(시/도). 예) 경기
    val middleAddrName: String, // 표출 주소 중분류명(시/군/구). 예) 원미구

    val roadName: String, // 도로명. 예) 신림로
    val firstBuildNo: String, // 도로명 본번. 예) '서울특별시 강서구 국회대로 59-5'의 경우 59
    val secondBuildNo: String, // 도로명 부번. 표기 안하는 경우가 많아서 0으로 리턴되는 경우 표기 안함 처리 필요. 예) '서울특별시 강서구 국회대로 59-5'의 경우 5

    val lowerBizName: String, // 업종 소분류명

    val lowerAddrName: String, // (도로명은 표기 X, 지번은 표기 O) 표출 주소 소분류명(읍/면/동). 예) 소사동
    val firstNo: String, // 지번 본번. '서울 종로구 세종로 77-6'의 경우 77
    val secondNo: String, // 지번 부번. '서울 종로구 세종로 77-6'의 경우 6

    val zipCode: String, // 우편번호. 예) 16491
): Parcelable {

    val addressType: AddressType
        get() = if (roadName.isNotBlank()) AddressType.ROAD else AddressType.LOT

    val integratedRoadNo: String
        get() = if (secondBuildNo == "0" || secondBuildNo == "") {
            firstBuildNo
        } else {
            "$firstBuildNo-$secondBuildNo"
        }

    val fullRoadNameAddress: String
        get() = "$upperAddrName $middleAddrName $roadName $integratedRoadNo"

    val fullLotNumberAddress: String
        get() = if(firstNo == "0") {
            "$upperAddrName $middleAddrName $lowerAddrName"
        } else if(secondNo == "0") {
            "$upperAddrName $middleAddrName $lowerAddrName $firstNo"
        } else {
            "$upperAddrName $middleAddrName $lowerAddrName $firstNo-$secondNo"
        }
}

enum class AddressType {
    ROAD,
    LOT
}