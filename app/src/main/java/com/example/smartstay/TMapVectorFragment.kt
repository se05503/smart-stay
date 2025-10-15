package com.example.smartstay

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.graphics.scale
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.smartstay.databinding.FragmentTMapVectorBinding
import com.example.smartstay.model.AccommodationInfo
import com.example.smartstay.network.RetrofitInstance
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.overlay.TMapOverlay
import com.skt.tmap.poi.TMapPOIItem
import java.util.ArrayList

class TMapVectorFragment : Fragment(R.layout.fragment_t_map_vector) {

    private lateinit var binding: FragmentTMapVectorBinding
    private val mapViewModel: MapViewModel by activityViewModels {
        MapViewModelFactory(RetrofitInstance.networkService)
    }
    private lateinit var tmapView: TMapView
    private val testAccommodationList: List<AccommodationInfo> = listOf(
        AccommodationInfo(
            name = "서울 센트럴 호텔",
            type = "호텔",
            image = R.drawable.img_stay_1,
            address = "서울특별시 중구 세종대로 110",
            latitude = 37.5665,
            longitude = 126.9780,
            minimumPrice = 85000,
            averagePrice = 100000,
            maximumPrice = 120000,
            starRating = "3성",
            finalRating = 4,
            isPetAvailable = "N",
            isRestaurantExist = "Y",
            isBarExist = "Y",
            isCafeExist = "Y",
            isFitnessCenterExist = "N",
            isSwimmingPoolExist = "N",
            isSpaExist = "N",
            isSaunaExist = "N",
            isReceptionCenterExist = "Y",
            isBusinessCenterExist = "Y",
            isOceanViewExist = "N"
        ),
        AccommodationInfo(
            name = "강남 프리미엄 레지던스",
            type = "레지던스",
            image = R.drawable.img_stay_2,
            address = "서울특별시 강남구 테헤란로 212",
            latitude = 37.5013,
            longitude = 127.0396,
            minimumPrice = 110000,
            averagePrice = 130000,
            maximumPrice = 160000,
            starRating = "4성",
            finalRating = 5,
            isPetAvailable = "Y",
            isRestaurantExist = "Y",
            isBarExist = "N",
            isCafeExist = "Y",
            isFitnessCenterExist = "Y",
            isSwimmingPoolExist = "N",
            isSpaExist = "N",
            isSaunaExist = "N",
            isReceptionCenterExist = "N",
            isBusinessCenterExist = "Y",
            isOceanViewExist = "N"
        ),
        AccommodationInfo(
            name = "홍대 스타일 게스트하우스",
            type = "게스트하우스",
            image = R.drawable.img_stay_3,
            address = "서울특별시 마포구 와우산로 29",
            latitude = 37.5561,
            longitude = 126.9229,
            minimumPrice = 35000,
            averagePrice = 50000,
            maximumPrice = 60000,
            starRating = "2성",
            finalRating = 3,
            isPetAvailable = "N",
            isRestaurantExist = "N",
            isBarExist = "Y",
            isCafeExist = "Y",
            isFitnessCenterExist = "N",
            isSwimmingPoolExist = "N",
            isSpaExist = "N",
            isSaunaExist = "N",
            isReceptionCenterExist = "N",
            isBusinessCenterExist = "N",
            isOceanViewExist = "N"
        ),
        AccommodationInfo(
            name = "이태원 뷰 호텔",
            type = "호텔",
            image = R.drawable.img_stay_4,
            address = "서울특별시 용산구 이태원로 188",
            latitude = 37.5349,
            longitude = 126.9948,
            minimumPrice = 90000,
            averagePrice = 110000,
            maximumPrice = 140000,
            starRating = "3성",
            finalRating = 4,
            isPetAvailable = "Y",
            isRestaurantExist = "Y",
            isBarExist = "Y",
            isCafeExist = "Y",
            isFitnessCenterExist = "Y",
            isSwimmingPoolExist = "N",
            isSpaExist = "N",
            isSaunaExist = "N",
            isReceptionCenterExist = "Y",
            isBusinessCenterExist = "N",
            isOceanViewExist = "N"
        ),
        AccommodationInfo(
            name = "한강 리버뷰 호텔",
            type = "호텔",
            image = R.drawable.img_stay_5,
            address = "서울특별시 영등포구 여의대로 24",
            latitude = 37.5219,
            longitude = 126.9246,
            minimumPrice = 130000,
            averagePrice = 150000,
            maximumPrice = 180000,
            starRating = "5성",
            finalRating = 5,
            isPetAvailable = "N",
            isRestaurantExist = "Y",
            isBarExist = "Y",
            isCafeExist = "Y",
            isFitnessCenterExist = "Y",
            isSwimmingPoolExist = "Y",
            isSpaExist = "Y",
            isSaunaExist = "Y",
            isReceptionCenterExist = "Y",
            isBusinessCenterExist = "Y",
            isOceanViewExist = "Y"
        )
    )
    private lateinit var endPoint: AccommodationInfo

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTMapVectorBinding.bind(view)
        initViews()
        initListeners()
        initObservers()
    }

    private fun initViews() = with(binding) {

        /**
         * 초기 도착지 첫번째 숙소로 설정하기
         */
        etMapEndPoint.setText(testAccommodationList[0].address)
        endPoint = testAccommodationList[0]

        /**
         * 지도 생성하기
         */
        tmapView = TMapView(requireContext())
        tmapVectorView.addView(tmapView)
        tmapView.setSKTMapApiKey(getString(R.string.sk_telecom_open_api_app_key))
        tmapView.setOnMapReadyListener(object : TMapView.OnMapReadyListener {
            override fun onMapReady() {
                /**
                 * 마커 추가하기
                 */
                val originalBitmap =
                    BitmapFactory.decodeResource(context?.resources, R.drawable.ic_tmap_marker_blue)
                val resizedBitmap = originalBitmap.scale(70, 70, false)

                for (accommodationInfo in testAccommodationList) {
                    val marker = TMapMarkerItem().apply {
                        id = accommodationInfo.name
                        icon = resizedBitmap
                        setTMapPoint(accommodationInfo.latitude, accommodationInfo.longitude)
                        canShowCallout = false
                        name = accommodationInfo.minimumPrice.toString()
                    }
                    tmapView.addTMapMarkerItem(marker)
                }

                /**
                 * 마커 말풍선 커스터마이징
                 */
                tmapView.setOnMapGestureListener(object: TMapView.OnMapGestureListenerCallback() {
                    override fun onSingleTapMapObject(
                        markerList: ArrayList<TMapMarkerItem?>?, // 클릭된 마커 리스트
                        poiList: ArrayList<TMapPOIItem?>?, // 클릭된 POI 리스트
                        point: TMapPoint?, // 클릭된 지점의 좌표
                        screenPoint: PointF? // 클릭된 지점의 화면 좌표
                    ): Boolean {
                        if(markerList != null) {
                            val clickedMarker = markerList[0]
                            if(clickedMarker != null) {
                                Log.e(TAG, ""+clickedMarker)
                                val customCallOutLayout = LayoutInflater.from(tmapView.context).inflate(R.layout.custom_tmap_callout, tmapView, false)
                                customCallOutLayout.measure(
                                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                                )
                                Log.e(TAG, "width: ${customCallOutLayout.measuredWidth}, height: ${customCallOutLayout.measuredHeight}")
                                customCallOutLayout.findViewById<TextView>(R.id.tvCallOutAccommodationName).text = clickedMarker.id
                                customCallOutLayout.findViewById<TextView>(R.id.tvCallOutAccommodationPrice).text = "${clickedMarker.name}원"
                                val x = (customCallOutLayout.measuredWidth/2).toDouble()
                                val y = (customCallOutLayout.measuredHeight/2).toDouble()
                                val tMapOverlay = TMapOverlay().apply {
                                    id = clickedMarker.id + "_CallOut"
                                    setOverlayImage(customCallOutLayout)
                                    leftTopPoint = TMapPoint(clickedMarker.tMapPoint.latitude - x, clickedMarker.tMapPoint.longitude - y)
                                    rightBottomPoint = TMapPoint(clickedMarker.tMapPoint.latitude + x, clickedMarker.tMapPoint.longitude + y)
                                }
                                tmapView.addTMapOverlay(tMapOverlay)
                                return true
                            }
                        }
                        return false
                    }
                })

                /**
                 * 자동차 경로안내
                 * 처음 작성할 때 onMapReady 바깥에 둠 → 지도에 안보임
                 * tmapView가 뷰에 attach된 직후 호출해야 한다.
                 * 만약 아직 맵이 준비되기 전에 findPathData()를 호출하면 PolyLine이 화면에 안 뜰 수 있다.
                 */
//                val startPoint = TMapPoint(37.570841, 126.985302) // 출발지 = SKT타워
//                val endPoint = TMapPoint(37.551135, 126.988205)
//                val tmapData = TMapData()
//                tmapData.findPathData(startPoint, endPoint, object: TMapData.OnFindPathDataListener {
//                    override fun onFindPathData(tmapPolyLine: TMapPolyLine?) {
//                        tmapPolyLine?.let {
//                            it.lineWidth = 3f
//                            it.lineColor = Color.BLUE
//                            it.lineAlpha = 255
//
//                            it.outLineWidth = 5f
//                            it.outLineColor = Color.RED
//                            it.outLineAlpha = 255
//
//                            tmapView.addTMapPolyLine(it)
//
//                            val info: TMapInfo = tmapView.getDisplayTMapInfo(it.linePointList)
//                            tmapView.zoomLevel = info.zoom
//                            tmapView.setCenterPoint(info.point.latitude, info.point.longitude)
//                        }
//                    }
//                })

                /**
                 * 리버스 지오코딩
                 * 좌표(위도, 경도) → 주소로 변환
                 * TMapData 객체의 convertGpsToAddress 메소드 사용
                 */
//                val handler = Handler(Looper.getMainLooper()) { message ->
//                    if(message.what == 1) {
//                        val address = message.data.getString("address")
//                        Toast.makeText(context, address, Toast.LENGTH_SHORT).show()
//                    }
//                    false
//                }
//
//                TMapData().convertGpsToAddress(37.570841, 126.985302, object: TMapData.OnConvertGPSToAddressListener {
//                    override fun onConverGPSToAddress(address: String?) {
//                        val message = Message()
//                        val bundle = Bundle()
//                        bundle.putString("address", address)
//                        message.data = bundle
//                        message.what = 1
//                        handler.sendMessage(message)
//                    }
//                })

                /**
                 * 명칭(POI) 통합 검색 (필요 O)
                 * POI = Point of Interest
                 * TMapData 객체의 findAllPOI 함수를 사용하면 쉽게 POI 정보를 검색할 수 있다.
                 * 도로명 주소를 입력하지 않아도 된다는 점에서 중요하게 활용될 것 같다.
                 */
//                val tmapData = TMapData()
//                val strData = ""
//                tmapData.findAllPOI(strData, object: TMapData.OnFindAllPOIListener {
//                    override fun onFindAllPOI(poiItemList: ArrayList<TMapPOIItem>) {
//                        for(poiItem in poiItemList) {
//                            Log.e("Poi Item", "name: ${poiItem.poiName}, address: ${poiItem.poiAddress}")
//                        }
//                        tmapView.addTMapPOIItem(poiItemList)
//                    }
//                })

            }
        })

        /**
         * 하단 숙소 목록 보여주기 (recyclerview)
         */
        val tMapAdapter = TMapAdapter(onClicked = { accommodationInfo ->
            val intent = Intent(context, StayDetailActivity::class.java).apply {
                putExtra(StayDetailActivity.BUNDLE_KEY, accommodationInfo)
            }
            startActivity(intent)
        })
        recyclerViewTMap.adapter = tMapAdapter
        recyclerViewTMap.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        tMapAdapter.submitList(testAccommodationList)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerViewTMap)

        /**
         * 하단 가운데 숙소 아이템 감지하기
         */
        recyclerViewTMap.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val layoutManager = recyclerView.layoutManager ?: return
                    val snappedView = snapHelper.findSnapView(layoutManager)
                    snappedView?.let { view ->
                        val snappedViewPosition = layoutManager.getPosition(view)
                        val snappedAccommodationInfo = testAccommodationList[snappedViewPosition]
                        tmapView.setCenterPoint(snappedAccommodationInfo.latitude, snappedAccommodationInfo.longitude)
                        tmapView.zoomLevel = 14
                        etMapEndPoint.setText(snappedAccommodationInfo.address)
                        endPoint = snappedAccommodationInfo
                    }
                }
            }
        })




        /**
         * 지도 이벤트 설정하기 (필요 X)
         */
//        tmapView.setOnClickListenerCallback(object: TMapView.OnClickListenerCallback {
//            override fun onPressDown(
//                p0: ArrayList<TMapMarkerItem?>?,
//                p1: ArrayList<TMapPOIItem?>?,
//                p2: TMapPoint?,
//                p3: PointF?
//            ) {
//                Toast.makeText(context, "onPressDown", Toast.LENGTH_SHORT).show()
//            }
//
//            override fun onPressUp(
//                p0: ArrayList<TMapMarkerItem?>?,
//                p1: ArrayList<TMapPOIItem?>?,
//                p2: TMapPoint?,
//                p3: PointF?
//            ) {
//                Toast.makeText(context, "onPressUp", Toast.LENGTH_SHORT).show()
//            }
//        })

        /**
         * 지도 중심점 및 레벨 변경하기 (필요 O)
         * 동적으로 사용할 듯.. 중심점은 사용자의 현재 위치, 줌 레벨은 상황에 따라 동적 변경
         */
//        tmapView.setCenterPoint(37.468954, 126.4544153)
        tmapView.zoomLevel = 10

        /**
         * 지도 타입 변경하기 (필요 X)
         * DEFAULT, PUBLIC 비슷해 보이는데 뭔 차이지?
         */
//        tmapView.mapType = TMapView.MapType.DEFAULT
//        tmapView.mapType = TMapView.MapType.PUBLIC

        /**
         * 마커 생성하기 (필요 O)
         * BitmapFactory.decodeResource()는 비트맵(PNG/JPG)만 읽을 수 있음, SVG 파일 사용 못함
         * Bitmap = pixel 단위, png/jpg, 확대하면 깨짐
         * png 단위인데 크기 조절 필요해보임
         */
//        val marker = TMapMarkerItem().apply {
//            id = "marker1"
//            setTMapPoint(37.468954, 126.4544153)
//            icon = BitmapFactory.decodeResource(resources, R.drawable.ic_sample_marker)
//        }
//        tmapView.addTMapMarkerItem(marker)

        /**
         * 마커 클러스터 이용하기 (필요 X)
         * clusterMarkerList 에다가 marker 넣어야 지도에 표시됨
         * clusteringDistance = 100.0 → 지도 상에서 100px 이내에 있는 마커들은 한 클러스터로 묶임
         * clusterMaxSize = 100 → 한 클러스터에는 최대 100개의 마커가 들어감
         */
//        val markerCluster = TMapMarkerCluster(tmapView)
//        val clusterMarkerList: List<TMapMarkerItem> = emptyList()
//        markerCluster.clusteringDistance = 100.0
//        markerCluster.clusterMaxSize = 100
//        markerCluster.markers = clusterMarkerList

        /**
         * 마커 이동(Tracking) 하기라곤 나와있는데 마커 설명 표시하기 같음
         * 실행했을 때 실제 이동하는 기능은 찾기 어려움
         * 말풍선도 사용하려면 따로 커스터마이징 필요할 듯
         * calloutTitle = 말풍선의 제목
         * calloutSubTitle = 말풍선의 부제목
         * canShowCallout = 마커 클릭 시 Callout(말풍선)을 표시할지 여부
         * isAnimation = 마커가 지도에 추가될 때 부드럽게 나타나는 애니메이션을 적용할지 여부
         * 마커 “이동” 애니메이션이 아니라, “마커가 처음 지도에 표시될 때” 살짝 튀어오르듯 나타나는 효과
         */
//        val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_sample_marker)
//        val resizedBitmap = originalBitmap.scale(150, 150, false)
//        val marker = TMapMarkerItem().apply {
//            id = "marker1"
//            icon = resizedBitmap
//            setTMapPoint(37.5665, 126.9780)
//            calloutTitle = "서울시청"
//            calloutSubTitle = "서울특별시 중구 세종대로 110"
//            canShowCallout = true
//            isAnimation = true
//        }
//        tmapView.addTMapMarkerItem(marker)

        /**
         * 선 그리기
         * 대중교통/차 네비게이션 표시할 때 사용할 수 있어보임
         * 단, 중심 좌표 설정은 따로 해야할 듯 → "자동차 경로안내" 에 잘 나와있음
         */
//        val pointList = arrayListOf<TMapPoint>()
//        pointList.add(TMapPoint(37.472678, 126.920928))
//        pointList.add(TMapPoint(37.494473, 127.120742))
//        pointList.add(TMapPoint(37.405619, 127.091903))
//        val line = TMapPolyLine("line1", pointList)
//        tmapView.addTMapPolyLine(line)

    }

    private fun initListeners() = with(binding) {
        ivMapNavigate.setOnClickListener {
            Log.e(TAG, "name: ${endPoint.name}, lat: ${endPoint.latitude}, lng: ${endPoint.longitude}")
        }
    }

    private fun initObservers() = with(binding) {
//        mapViewModel.tMapMultiModalRouteInfo.observe(viewLifecycleOwner) { routeInfo ->
//            Log.e(TAG, ""+ routeInfo)
//        }
//        mapViewModel.findTMapMultiModalRouteSummary(
//            tMapRouteRequest = TMapRouteRequest(
//                startX = "126.936928",
//                startY = "37.555162",
//                endX = "127.029281",
//                endY = "37.564436",
//            ),
//            context = requireContext()
//        )

//        mapViewModel.getTravelDistrictsCode(context = requireContext())
//        mapViewModel.tMapTravelDistrictsInfo.observe(viewLifecycleOwner) { travelDestinationInfo ->
//            Log.e(TAG, ""+ travelDestinationInfo)
//        }

        // 테스트 시작점 (할당량 초과로 테스트 못함)
//        mapViewModel.getTravelAccommodationInfo(context = requireContext(), districtCode = "5013000000")
//        mapViewModel.tMapTravelAccommodationsInfo.observe(viewLifecycleOwner) { accommodationInfo ->
//            Log.e(TAG, ""+ accommodationInfo)
//        }

//        mapViewModel.getTravelMonthlyVisitorsCount(context = requireContext(), districtCode = "5011000000")
//        mapViewModel.tMapTravelMonthlyVisitorsInfo.observe(viewLifecycleOwner) { travelMonthlyVisitorInfo ->
//            Log.e(TAG, ""+travelMonthlyVisitorInfo)
//        }

//        mapViewModel.getTravelDailyVisitorsCount(context = requireContext(), districtCode = "5011000000")
//        mapViewModel.tMapTravelDailyVisitorsInfo.observe(viewLifecycleOwner) { travelDailyVisitorInfo ->
//            Log.e(TAG, ""+travelDailyVisitorInfo)
//        }

//        mapViewModel.getTravelMonthlyDistrictDuration(context = requireContext(), districtCode = "5013032023")
//        mapViewModel.tMapTravelDistrictDurationInfo.observe(viewLifecycleOwner) { travelDistrictDurationInfo ->
//            Log.e(TAG, ""+travelDistrictDurationInfo)
//        }

//        mapViewModel.getTravelSpecificAccommodationRanking(context = requireContext(), poiId = "6453368")
//        mapViewModel.tMapTravelSpecificAccommodationRankingInfo.observe(viewLifecycleOwner) { travelSpecificAccommodationRankingInfo ->
//            Log.e(TAG, ""+travelSpecificAccommodationRankingInfo)
//        }

//        mapViewModel.getTravelDistrictAccommodationRanking(context = requireContext(), districtCode = "5000000000")
//        mapViewModel.tMapTravelDistrictAccommodationRankingInfo.observe(viewLifecycleOwner) { travelDistrictAccommodationRankingInfo ->
//            Log.e(TAG, ""+travelDistrictAccommodationRankingInfo)
//        }

//        mapViewModel.getTravelDistrictAccommodationThemeRanking(context = requireContext(), theme = "companion-rate", companionType = "family_w_child", districtCode = "5000000000")
//        mapViewModel.tMapTravelDistrictAccommodationThemeRankingInfo.observe(viewLifecycleOwner) { travelDistrictAccommodationThemeRankingInfo ->
//            Log.e(TAG, ""+travelDistrictAccommodationThemeRankingInfo)
//        }

//        mapViewModel.getTravelSpecificAccommodationFeature(context = requireContext(), poiId = "6453368", type = "percentile")
//        mapViewModel.tMapTravelSpecificAccommodationFeatureInfo.observe(viewLifecycleOwner) { travelSpecificAccommodationFeatureInfo ->
//            Log.e(TAG, ""+travelSpecificAccommodationFeatureInfo)
//        }

//        mapViewModel.getTravelSpecificAccommodationVisitorSegmentsRate(context = requireContext(), poiId = "6453368")
//        mapViewModel.tMapTravelSpecificAccommodationVisitorSegmentsInfo.observe(viewLifecycleOwner) { travelSpecificAccommodationVisitorSegmentsInfo ->
//            Log.e(TAG, ""+travelSpecificAccommodationVisitorSegmentsInfo)
//        }

//        mapViewModel.getTravelDistrictsAccommodationVisitorSegmentsRate(context = requireContext(), districtCode = "5013000000")
//        mapViewModel.tMapTravelDistrictsAccommodationVisitorSegmentsInfo.observe(viewLifecycleOwner) { travelDistrictsAccommodationVisitorSegmentsInfo ->
//            Log.e(TAG, ""+travelDistrictsAccommodationVisitorSegmentsInfo)
//        }

//        mapViewModel.getTravelSimilarAccommodation(context = requireContext(), poiId = "6453368", type = "ctp")
//        mapViewModel.tMapTravelSimilarAccommodationInfo.observe(viewLifecycleOwner) { travelSimilarAccommodationInfo ->
//            Log.e(TAG, ""+travelSimilarAccommodationInfo)
//        }

//        mapViewModel.getTravelPopularRestaurantsNearby(context = requireContext(), poiId = "6453368", category = "kor")
//        mapViewModel.tMapTravelPopularRestaurantsNearbyInfo.observe(viewLifecycleOwner) { travelPopularRestaurantsNearbyInfo ->
//            Log.e(TAG, ""+travelPopularRestaurantsNearbyInfo)
//        }

//        mapViewModel.getTravelPopularRestaurantsNearBySegmentRate(context = requireContext(), poiId = "6453368", gender = "male", ageGrp = "30")
//        mapViewModel.tMapTravelPopularRestaurantsNearbySegmentRateInfo.observe(viewLifecycleOwner) { travelPopularRestaurantsNearbySegmentRateInfo ->
//            Log.e(TAG, ""+travelPopularRestaurantsNearbySegmentRateInfo)
//        }

//        mapViewModel.getTravelPopularSpotsNearby(context = requireContext(), poiId = "8368040", category = "shopping")
//        mapViewModel.tMapTravelPopularSpotsNearbyInfo.observe(viewLifecycleOwner) { travelPopularSpotsNearbyInfo ->
//            Log.e(TAG, ""+travelPopularSpotsNearbyInfo)
//        }

//        mapViewModel.getTravelPopularSpotsNearbySegmentRate(context = requireContext(), poiId = "6453368", gender = "male", ageGrp = "30")
//        mapViewModel.tMapTravelPopularSpotsNearbySegmentRateInfo.observe(viewLifecycleOwner) { travelPopularSpotsNearbySegmentRateInfo ->
//            Log.e(TAG, ""+travelPopularSpotsNearbySegmentRateInfo)
//        }

//        mapViewModel.getTravelPopularCommercialDistrictNearby(context = requireContext(), poiId = "544494")
//        mapViewModel.tMapTravelPopularCommercialDistrictNearbyInfo.observe(viewLifecycleOwner) { travelPopularDistrictNearbyInfo ->
//            Log.e(TAG, ""+travelPopularDistrictNearbyInfo)
//        }

//        mapViewModel.getTravelPopularCommercialDistrictNearbySegmentRate(context = requireContext(), poiId = "544494", gender = "male", ageGrp = "30")
//        mapViewModel.tMapTravelPopularCommercialDistrictNearbySegmentRateInfo.observe(viewLifecycleOwner) { travelPopularDistrictNearbySegmentRateInfo ->
//            Log.e(TAG, ""+travelPopularDistrictNearbySegmentRateInfo)
//        }
    }

    companion object {
        const val TAG = "TMAP"
    }

}