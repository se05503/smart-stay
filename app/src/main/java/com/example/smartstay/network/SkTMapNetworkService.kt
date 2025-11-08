package com.example.smartstay.network

import com.example.smartstay.model.TMapRouteRequest
import com.example.smartstay.model.TMapRouteResponse
import com.example.smartstay.model.TMapTravelAccommodationResponse
import com.example.smartstay.model.TMapTravelDailyVisitorResponse
import com.example.smartstay.model.TMapTravelDistrictAccommodationRankingResponse
import com.example.smartstay.model.TMapTravelDistrictAccommodationThemeRankingResponse
import com.example.smartstay.model.TMapTravelDistrictDurationResponse
import com.example.smartstay.model.TMapTravelDistrictResponse
import com.example.smartstay.model.TMapTravelDistrictsAccommodationVisitorSegmentsResponse
import com.example.smartstay.model.TMapTravelMonthlyVisitorResponse
import com.example.smartstay.model.TMapTravelPopularCommercialDistrictNearbyResponse
import com.example.smartstay.model.TMapTravelPopularCommercialDistrictNearbySegmentRateResponse
import com.example.smartstay.model.TMapTravelPopularRestaurantsNearbyResponse
import com.example.smartstay.model.TMapTravelPopularRestaurantsNearbySegmentRateResponse
import com.example.smartstay.model.TMapTravelPopularSpotsNearbyResponse
import com.example.smartstay.model.TMapTravelPopularSpotsNearbySegmentRateResponse
import com.example.smartstay.model.TMapTravelSimilarAccommodationResponse
import com.example.smartstay.model.TMapTravelSpecificAccommodationFeatureResponse
import com.example.smartstay.model.TMapTravelSpecificAccommodationRankingResponse
import com.example.smartstay.model.TMapTravelSpecificAccommodationVisitorSegmentsResponse
import com.example.smartstay.model.tmap.IntegratedPlaceResponse
import com.example.smartstay.model.tmap.TMapRoutesPredictionRequest
import com.example.smartstay.model.tmap.TMapRoutesPredictionResponse
import com.example.smartstay.model.tmap.TMapRoutesRequest
import com.example.smartstay.model.tmap.TMapRoutesResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SkTMapNetworkService {
    /**
     * TMAP API 입니다.
     */
    /**
     * (사용 완료) 지도 썸네일 이미지 제공
     */
    @GET("tmap/staticMap")
    suspend fun getTMapThumbnailImage(
        @Header("appKey") appKey: String,
        @Header("Accept") accept: String = "application/json",
        @Query("version") version: String,
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("markers") markers: String, // 마커를 표시할 좌표를 지정합니다. 경위도 좌푯값은 쉼표(,)로 구분하는데, UTF-8 기반의 URL 인코딩 처리가 필요합니다.
        @Query("width") width: Int = 512,
        @Query("height") height: Int = 512,
        @Query("zoom") zoom: Int // min 6 ~  max 19
    ): ResponseBody

    /**
     * 타임머신 자동차 길 안내 API
     * 출발지와 목적지를 등록하고 미래의 특정 시간을 입력하면 경로의 다양한 길 안내 정보를 파악할 수 있다.
     */
    @POST("tmap/routes/prediction")
    suspend fun getTMapRoutesPrediction(
        @Header("appKey") appKey: String,
        @Query("version") version: Int, // (예: 1) API 서비스의 지원 오퍼레이션 버전. 현재 버전 1을 지원하며, 버전에 따라 응답 결과로 표출되는 내용이 다를 수 있다.
        @Query("totalValue") totalValue: Int, // 검색 결과를 반환하는 방법 지정. 1: 모든 정보 반환, 2: 다음 정보만 반환 → 총 거리, 총 소요 시간, 총 요금, 예상 택시 요금, 출발 시간, 도착 시간
        @Body tMapRoutesPredictionRequest: TMapRoutesPredictionRequest
    ): TMapRoutesPredictionResponse

    /**
     * TMAP 대중교통 입니다.
     */
    /**
     * 출발지/목적지에 대한 대중교통 경로탐색 정보 및 전체 보행자 이동 경로 제공
     */
    @POST("transit/routes")
    suspend fun findTMapMultiModalRoute(
        @Header("Accept") accept: String = "application/json",
        @Header("Content-Type") contentType: String = "application/json",
        @Header("appKey") appKey: String,
        @Body tMapRouteRequest: TMapRouteRequest
    ): TMapRouteResponse

    /**
     * 출발지/목적지에 대한 대중교통 경로탐색 요약정보 제공
     */
    @POST("transit/routes/sub")
    suspend fun findTMapMultiModalRouteSummary(
        @Header("Accept") accept: String = "application/json",
        @Header("Content-Type") contentType: String = "application/json",
        @Header("appKey") appKey: String,
        @Body tMapRouteRequest: TMapRouteRequest
    ): TMapRouteResponse

    /**
     * 지오버전 퍼즐 - 국내 여행입니다.
     */
    @GET("puzzle/travel/meta/districts")
    suspend fun getTravelDistrictsCode(
        @Header("appKey") appKey: String,
        @Query("type") type: String, // 검색할 법정동 유형, 지정하지 않으면 시, 군, 구 단위 여행지 정보를 반환함. ri = 동, 리 단위 / sig = 시, 군, 구 단위
        @Query("offset") offset: Int, // 여행지 목록을 조회하는 시작점 (n번째 여행지부터 조회)
        @Query("limit") limit: Int // 여행지 목록을 offset부터 시작해서 얼만큼 가져올 것인지 한도를 지정함
    ): TMapTravelDistrictResponse

    @GET("puzzle/travel/meta/accommodation/districts/{districtCode}")
    suspend fun getTravelAccommodationInfo(
        @Header("appKey") appKey: String,
        @Path("districtCode") districtCode: String, // 숙소 목록을 조회할 시도의 법정동 코드 ex) 5000000000 = 제주특별자치도
        @Query("offset") offset: Int, // 숙소 목록을 조회하는 시작점. 예를 들면, 숙소가 총 100개 있을 때, offset을 50으로 지정하면 50번째 숙소부터 목록을 조회한다.
        @Query("limit") limit: Int // 숙소 목록을 offset부터 시작해서 얼마나 가져올 것인지 한도 지정
    ): TMapTravelAccommodationResponse

    /**
     * 특정 여행지(시, 군, 구)의 월별 추정 여행자 수 정보를 제공합니다.
     * 데이터 제공 가능 여행지 검색에서 반환된 시, 군, 구 지역을 대상으로 추정 여행자 수를 확인할 수 있습니다.
     * 추정 여행자 수는 매 기준월 1일에서 말일 사이 여행을 마친 여행자를 대상으로 계산됩니다.
     * 기준 월(yearMonth)을 입력하지 않으면 최근 월을 기준으로 정보를 반환합니다.
     * 요청 쿼리에 성별, 연령대, 동반자 유형 등 여행자 특성 파라미터를 지정하면 지정된 여행자 특성에 따라 추정된 여행자 수를 반환합니다.
     * 상세 여행자 특성 파라미터를 지정하지 않으면 전체 여행자 수를 반환합니다.
     */
    @GET("puzzle/travel/visit/count/raw/monthly/districts/{districtCode}")
    suspend fun getTravelMonthlyVisitorsCount(
        @Header("appKey") appKey: String,
        @Path("districtCode") districtCode: String, // 데이터 제공 가능 여행지 검색에서 반환된 시, 군, 구 지역 중 검색할 여행지의 법정동 코드(10자리 숫자)를 지정합니다.
        @Query("yearMonth") yearMonth: String, // 검색 기준 월(연도+월)입니다. 기준 월을 입력하지 않으면 최근 월(latest)에 대한 데이터를 반환합니다. 날짜 입력: 검색할 월을 YYYYMM(연도+월) 형식으로 입력. latest(기본값): 최근 월 검색
        @Query("gender") gender: String, // 여행자의 성별을 지정합니다. 성별을 입력하지 않으면 모든 성별에 대한 데이터를 반환합니다. male: 남성, female: 여성, all(기본값): 모든 성별
        @Query("ageGrp") ageGrp: String, // 여행자의 연령대를 지정합니다. 연령대를 입력하지 않으면 모든 연령대에 대한 데이터를 반환합니다. 10: 10대, 20: 20대, ... 50: 50대, 60_over: 60대 이상, all(기본값): 모든 연령대
        @Query("companionType") companionType: String // 동반자 유형을 지정합니다. 동반자 유형을 입력하지 않으면 모든 동반자 유형에 대한 데이터를 반환합니다. family: 가족 동반, family_w_child: 자녀 동반, not_family: 가족 동반자 확인되지 않음, all(기본값): 모든 동반자 유형
    ): TMapTravelMonthlyVisitorResponse

    /**
     * 특정 여행지(시, 군, 구)의 일별 추정 여행자 수 정보 제공합니다.
     * 요청일 기준 33일 전부터 4일 전까지 총 30일의 기간에 대해 일자별로 추정된 여행자 수를 제공합니다(해당일에 여행을 시작하거나 마친 여행자 및 여행 중인 여행자 수를 추정)
     */
    @GET("puzzle/travel/visit/count/raw/daily/districts/{districtCode}")
    suspend fun getTravelDailyVisitorsCount(
        @Header("appKey") appKey: String,
        @Path("districtCode") districtCode: String, // ex) "5011000000" = 제주특별자치도 제주시, "all" = 국내 전 지역의 여행자 수 제공
        @Query("gender") gender: String, // [option] male: 남성, female: 여성, all: 모든 성별
        @Query("ageGrp") ageGrp: String, // [option] 10: 10대, 20: 20대, ... 50: 50대, 60_over: 60대 이상, all(default): 모든 연령대
        @Query("companionType") companionType: String // [option] family: 가족 동반, family_w_child: 자녀 동반, not_family: 가족 동반자 확인되지 않음, all: 모든 동반자 유형
    ): TMapTravelDailyVisitorResponse

    /**
     * 특정 여행지(동, 리)를 여행하는 여행자의 월별 평균 체류시간 정보를 제공합니다.
     * 데이터 제공 가능 여행지 검색에서 반환된 동, 리 지역을 대상으로 월별 여행자 체류 시간을 확인할 수 있습니다.
     * 매 기준월 1일에서 말일 사이 해당 지역(동, 리)을 여행한 여행자의 워렬 평균 체류시간을 초 단위로 제공합니다.
     * 데이터가 충분하지 않아 통계 산출이 어려운 경우 체류시간 값으로 null을 반환합니다.
     * 기준 월(yearMonth)을 입력하지 않으면 최근 월을 기준으로 정보를 반환합니다.
     */
    @GET("puzzle/travel/visit/duration/raw/monthly/districts/{districtCode}")
    suspend fun getTravelMonthlyDistrictDuration(
        @Header("appKey") appKey: String,
        @Path("districtCode") districtCode: String,
        @Query("yearMonth") yearMonth: String, // [option] 검색 기준 월(연도+월)입니다. 기준 월을 입력하지 않으면 최근 월(latest)에 대한 데이터를 반환합니다. 날짜 입력: 검색할 월을 YYYYMM(연도+월) 형식으로 입력. latest(기본값): 최근 월 검색
    ): TMapTravelDistrictDurationResponse

    /**
     * 통화 통계, 기지국 기반 위치 정보를 바탕으로 한 숙소 인기 순위를 조회합니다
     */
    @GET("puzzle/travel/accommodation/ranking/pois/{poiId}")
    suspend fun getTravelSpecificAccommodationRanking(
        @Header("appKey") appKey: String,
        @Path("poiId") poiId: String // 순위를 조회할 숙소의 코드입니다. "데이터 제공 가능한 숙소"에서 숙소 코드를 검색할 수 있습니다. ex) 6453368
    ): TMapTravelSpecificAccommodationRankingResponse

    /**
     * 시도 내 지역 숙소 순위를 조회합니다.
     */
    @GET("puzzle/travel/accommodation/ranking/districts/{districtCode}")
    suspend fun getTravelDistrictAccommodationRanking(
        @Header("appKey") appKey: String,
        @Path("districtCode") districtCode: String // 순위를 조회할 시도 법정동 코드 ex) 500000000 = 제주특별자치도
    ): TMapTravelDistrictAccommodationRankingResponse

    /**
     * 시도 내 지역 숙소 순위를 테마별로 조회합니다.
     */
    @GET("puzzle/travel/accommodation/ranking/{theme}/districts/{districtCode}")
    suspend fun getTravelDistrictAccommodationThemeRanking(
        @Header("appKey") appKey: String,
        @Path("theme") theme: String, // 순위를 조회할 숙소의 테마입니다. ex) hot-rate: 요즘 뜨는, companion-rate: 동반자 유형(신혼부부, 아이와 함께, 가족여행), seg-rate 성별, 연령대별 (*segment: 부분, 단편, 분할)
        @Path("districtCode") districtCode: String, // 순위를 조회할 시도 법정동 코드를 지정합니다. ex) 5000000000 = 제주특별자치도
        @Query("companionType") companionType: String, // 동반자 유형별 순위를 조회할 때 필수값입니다. newly_weds: 신혼부부, family_w_child: 아이와 함께, family: 가족여행
        @Query("gender") gender: String, // [option] 성별 구분입니다. male: 남성, female: 여성, all(default): 전체
        @Query("ageGrp") ageGrp: String, // [option] 연령대 구분입니다. 0: 10세 미만, 10: 10대, ... 90: 90대, 100_over: 100세 이상, all(default): 전체
    ): TMapTravelDistrictAccommodationThemeRankingResponse

    /**
     * 소재한 시도 지역 내에서 특정 숙소의 두드러지는 특징을 조회합니다.
     */
    @GET("puzzle/travel/accommodation/analytics/feature/pois/{poiId}")
    suspend fun getTravelSpecificAccommodationFeature(
        @Header("appKey") appKey: String,
        @Path("poiId") poiId: String, // 조회할 숙소 코드
        @Query("type") type: String, // 조회할 숙소의 특징값 유형. percentile: 백분위, lift: 지역 평균 대비 빈도수의 향상도
    ): TMapTravelSpecificAccommodationFeatureResponse

    /**
     * 숙소를 방문한 고객의 성별, 연령별 비율을 조회합니다.
     */
    @GET("puzzle/travel/accommodation/analytics/visit-seg/pois/{poiId}")
    suspend fun getTravelSpecificAccommodationVisitorSegmentsRate(
        @Header("appKey") appKey: String,
        @Path("poiId") poiId: String // 조회할 숙소 코드
    ): TMapTravelSpecificAccommodationVisitorSegmentsResponse

    /**
     * 시군구 지역 숙소를 방문한 고객의 성별, 연령별 비율을 조회합니다.
     */
    @GET("puzzle/travel/accommodation/analytics/visit-seg/districts/{districtCode}")
    suspend fun getTravelDistrictsAccommodationVisitorSegmentsRate(
        @Header("appKey") appKey: String,
        @Path("districtCode") districtCode: String // 조회할 시군구 법정동 코드를 지정합니다.
    ): TMapTravelDistrictsAccommodationVisitorSegmentsResponse

    /**
     * 특정 숙소와 함께 찾는 숙소를 조회합니다.
     */
    @GET("puzzle/travel/accommodation/analytics/similar/pois/{poiId}")
    suspend fun getTravelSimilarAccommodation(
        @Header("appKey") appKey: String,
        @Path("poiId") poiId: String, // 조회할 숙소 코드
        @Query("type") type: String // 숙소 조회 범위 → all: 전국, ctp: 시도 내, sig: 시군구 내
    ): TMapTravelSimilarAccommodationResponse

    /**
     * 여행객이 선호하는 숙소 주변 인기 음식점 Top 5를 음식점 분류별로 제공합니다.
     */
    @GET("puzzle/travel/accommodation/nearby/restaurant/pois/{poiId}")
    suspend fun getTravelPopularRestaurantsNearby(
        @Header("appKey") appKey: String,
        @Path("poiId") poiId: String, // 조회할 숙소 코드
        @Query("category") category: String // 음식점 분류 → kor: 한식, chn: 중식, jpn: 일식, wes: 양식, asi: 아시아음식, etc: 기타, all: 전체
    ): TMapTravelPopularRestaurantsNearbyResponse

    /**
     * 여행객이 선호하는 숙소 주변 인기 음식점 Top 5를 성·연령별로 제공합니다.
     */
    @GET("puzzle/travel/accommodation/nearby/restaurant/seg-rate/pois/{poiId}")
    suspend fun getTravelPopularRestaurantsNearBySegmentRate(
        @Header("appKey") appKey: String,
        @Path("poiId") poiId: String, // 조회할 숙소 코드
        @Query("gender") gender: String, // 성별 → male: 남성, female: 여성, all: 전체
        @Query("ageGrp") ageGrp: String // 연령대 구분 → 0: 10세 미만, 10: 10대, ... 90: 90대, 100_over: 100세 이상, all: 전체
    ): TMapTravelPopularRestaurantsNearbySegmentRateResponse

    /**
     * 여행객이 선호하는 숙소 주변 인기 장소 Top 5를 장소 분류별로 제공합니다.
     */
    @GET("puzzle/travel/accommodation/nearby/poi/pois/{poiId}")
    suspend fun getTravelPopularSpotsNearby(
        @Header("appKey") appKey: String,
        @Path("poiId") poiId: String, // 조회할 숙소 코드
        @Query("category") category: String // 장소 분류 → shopping: 쇼핑, sports: 레저·스포츠, tour: 관광명소
    ): TMapTravelPopularSpotsNearbyResponse

    /**
     * 여행객이 선호하는 숙소 주변 인기 장소 Top 5를 성·연령별로 제공합니다.
     */
    @GET("puzzle/travel/accommodation/nearby/poi/seg-rate/pois/{poiId}")
    suspend fun getTravelPopularSpotsNearbySegmentRate(
        @Header("appKey") appKey: String,
        @Path("poiId") poiId: String, // 조회할 숙소 코드
        @Query("gender") gender: String, // 성별 → male: 남성, female: 여성, all: 전체
        @Query("ageGrp") ageGrp: String // 연령대 구분 → 0: 10세 미만, 10: 10대, ... 90: 90대, 100_over: 100세 이상, all: 전체
    ): TMapTravelPopularSpotsNearbySegmentRateResponse

    /**
     * 여행객이 선호하는 숙소 주변 인기 상권 Top 5를 제공합니다.
     */
    @GET("puzzle/travel/accommodation/nearby/area/pois/{poiId}")
    suspend fun getTravelPopularCommercialDistrictNearby(
        @Header("appKey") appKey: String,
        @Path("poiId") poiId: String, // 조회할 숙소 코
    ): TMapTravelPopularCommercialDistrictNearbyResponse

    @GET("puzzle/travel/accommodation/nearby/area/seg-rate/pois/{poiId}")
    suspend fun getTravelPopularCommercialDistrictNearbySegmentRate(
        @Header("appKey") appKey: String,
        @Path("poiId") poiId: String, // 조회할 숙소 코드
        @Query("gender") gender: String,
        @Query("ageGrp") ageGrp: String
    ): TMapTravelPopularCommercialDistrictNearbySegmentRateResponse

    /**
     * 장소 통합 검색
     */
    @GET("tmap/pois")
    suspend fun searchIntegratedPlaces(
        @Header("appKey") appKey: String,
        @Query("version") version: Int,
        @Query("searchKeyword") searchKeyword: String, // 시설물명, 상호, 시설 유형, 주소, 전화번호를 검색어로 지정합니다. 명칭 검색 시 UTF-8 기반의 [URL 인코딩] 처리 필수
        @Query("count") count: Int, // 페이지 당 검색 결과 수를 지정합니다. 최소: 1 ~ 최대: 150, 기본값: 20
        @Query("multiPoint") multiPoint: String // 검색할 관심 장소(POI)가 정문, 후문 등 입구가 여러 개인 건물인 경우 기본 건물에 대한 결괏값만 반환할지, 모든 결괏값을 반환할지 지정합니다. Y: 기본 결괏값만 반환. N(기본값): 모든 결괏값을 반환
    ): IntegratedPlaceResponse
}