package com.example.smartstay

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.smartstay.databinding.FragmentTMapVectorBinding
import com.example.smartstay.model.accommodation.Accommodation
import com.example.smartstay.model.accommodation.Destination
import com.example.smartstay.model.tmap.LocationInfo
import com.example.smartstay.model.tmap.RoutesInfo
import com.example.smartstay.model.tmap.TMapRoutesPredictionRequest
import com.example.smartstay.network.RetrofitInstance
import com.example.smartstay.presentation.AccommodationViewModel
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.skt.tmap.TMapData
import com.skt.tmap.TMapInfo
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.overlay.TMapPolyLine
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TMapVectorFragment : Fragment(R.layout.fragment_t_map_vector) {

    private lateinit var binding: FragmentTMapVectorBinding
    private val mapViewModel: MapViewModel by activityViewModels {
        MapViewModelFactory(RetrofitInstance.skTMapNetworkService)
    }
    private val accommodationViewModel: AccommodationViewModel by activityViewModels()
    private lateinit var tmapView: TMapView
    private val initialDestination: Destination by lazy {
        val args: TMapVectorFragmentArgs by navArgs()
        args.destinationInfo
    }
    private val destinationList: List<Destination> by lazy {
        val originalList = accommodationViewModel.recommendDestinationList.toMutableList()
        originalList.remove(initialDestination)
        originalList.add(0, initialDestination)
        originalList.toList()
    }
    private lateinit var endPoint: Accommodation
    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>
    private lateinit var userCurrentLocation: Location

    private val accommodationBitmap: Bitmap by lazy {
        BitmapFactory.decodeResource(context?.resources, R.drawable.ic_accommodation)
    }
    private val attractionTourBitmap: Bitmap by lazy {
        BitmapFactory.decodeResource(context?.resources, R.drawable.ic_attraction_tour)
    }
    private val attractionShoppingBitmap: Bitmap by lazy {
        BitmapFactory.decodeResource(context?.resources, R.drawable.ic_attraction_shopping)
    }
    private val attractionLeisureBitmap: Bitmap by lazy {
        BitmapFactory.decodeResource(context?.resources, R.drawable.ic_attraction_leisure)
    }
    private val attractionDefaultBitmap: Bitmap by lazy {
        BitmapFactory.decodeResource(context?.resources, R.drawable.ic_attraction_default)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /**
         * 1) 위치 요청을 위해 **최대 기다릴 시간(Timeout)**을 밀리초(ms) 단위로 설정
         * 이 시간 내에 위치 정보가 성공적으로 도착하지 않으면, 시스템은 위치 요청을 실패 처리하고 작업을 중단함
         * 2) null을 전달했다는 것은 이 위치 요청을 수동으로 취소할 수 있는 메커니즘을 사용하지 않겠다는 의미
         */
        locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    lifecycleScope.launch {
                        userCurrentLocation = getUserFineLocation()
                        Log.e(TAG, "" + userCurrentLocation)
                    }

                }

                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    lifecycleScope.launch {
                        userCurrentLocation = getUserCoarseLocation()
                        Log.e(TAG, "" + userCurrentLocation)
                    }
                }
                else -> {
                    Toast.makeText(context, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTMapVectorBinding.bind(view)
        initViews()
        initListeners()
        initObservers()
    }

    private fun initViews() = with(binding) {

        /**
         * 지도 생성하기
         */
        tmapView = TMapView(requireContext())
        tmapVectorView.addView(tmapView)
        tmapView.setSKTMapApiKey(getString(R.string.sk_telecom_open_api_app_key))
        tmapView.zoomLevel = 11
        tmapView.setCenterPoint(initialDestination.accommodation.latitude, initialDestination.accommodation.longitude)
        tmapView.setOnMapReadyListener(object : TMapView.OnMapReadyListener {
            override fun onMapReady() {

                /**
                 * 마커 추가하기
                 */
                // TODO: TMapMarkerItem vs TMapMarkerItem2 비교해서 더 나은 것 사용하기
                addMarkerOnMap(initialDestination)

                /**
                 * 마커 말풍선 오버레이 (구현 미완성)
                 */
//                tmapView.setOnMapGestureListener(object: TMapView.OnMapGestureListenerCallback() {
//                    override fun onSingleTapMapObject(
//                        markerList: ArrayList<TMapMarkerItem?>?, // 클릭된 마커 리스트
//                        poiList: ArrayList<TMapPOIItem?>?, // 클릭된 POI 리스트
//                        point: TMapPoint?, // 클릭된 지점의 좌표
//                        screenPoint: PointF? // 클릭된 지점의 화면 좌표
//                    ): Boolean {
//                        if(markerList != null) {
//                            val clickedMarker = markerList[0]
//                            if(clickedMarker != null) {
//
//                                val customCallOutLayout = LayoutInflater.from(tmapView.context).inflate(R.layout.custom_tmap_callout, null, false) // 인자 정리하기
//
//                                // 정리하기
//                                customCallOutLayout.measure(
//                                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
//                                )
//
//                                // 인자 정리하기
//                                customCallOutLayout.layout(0, 0, customCallOutLayout.measuredWidth, customCallOutLayout.measuredHeight)
//                                Log.e(TAG, "width: ${customCallOutLayout.measuredWidth}, height: ${customCallOutLayout.measuredHeight}")
//
//                                customCallOutLayout.findViewById<TextView>(R.id.tvCallOutAccommodationName).text = clickedMarker.id
//                                customCallOutLayout.findViewById<TextView>(R.id.tvCallOutAccommodationPrice).text = "${clickedMarker.name}원"
//
//                                val tMapOverlay = TMapOverlay().apply {
//                                    id = clickedMarker.id + "_CallOut"
//                                    setOverlayImage(customCallOutLayout)
//                                    leftTopPoint = TMapPoint(clickedMarker.tMapPoint.latitude + 0.0003, clickedMarker.tMapPoint.longitude - 0.0003) // 왜 이렇게?
//                                    rightBottomPoint = TMapPoint(clickedMarker.tMapPoint.latitude, clickedMarker.tMapPoint.longitude + 0.0003) // 왜 이렇게?
//                                }
//                                tmapView.addTMapOverlay(tMapOverlay)
//                                return true
//                            }
//                        }
//                        return false
//                    }
//                })

                /**
                 * 선 그리기
                 * 대중교통/차 네비게이션 표시할 때 사용할 수 있어보임
                 * 단, 중심 좌표 설정은 따로 해야할 듯 → "자동차 경로안내" 에 잘 나와있음
                 */
//                val pointList = arrayListOf<TMapPoint>()
//                pointList.add(TMapPoint(37.472678, 126.920928))
//                pointList.add(TMapPoint(37.494473, 127.120742))
//                pointList.add(TMapPoint(37.405619, 127.091903))
//                val line = TMapPolyLine("line1", pointList)
//                tmapView.addTMapPolyLine(line)

                /**
                 * 자동차 경로안내
                 * 처음 작성할 때 onMapReady 바깥에 둠 → 지도에 안보임
                 * tmapView가 뷰에 attach된 직후 호출해야 한다.
                 * 아직 맵이 준비되기 전에 findPathData()를 호출하면 PolyLine이 화면에 안 뜰 수 있다.
                 */
//                val startPoint = TMapPoint(37.570841, 126.985302)
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
         * 초기 도착지 설정하기
         */
        etMapEndPoint.setText(initialDestination.accommodation.address)
        endPoint = initialDestination.accommodation

        /**
         * 하단 숙소 목록 보여주기
         */
        val tMapAdapter = TMapAdapter(onClicked = { accommodationInfo ->

        })
        recyclerViewTMap.adapter = tMapAdapter
        recyclerViewTMap.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        tMapAdapter.submitList(destinationList)
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
                        val snappedDestination = destinationList[snappedViewPosition]
                        tmapView.removeAllTMapMarkerItem()
                        addMarkerOnMap(snappedDestination)
                        etMapEndPoint.setText(snappedDestination.accommodation.address)
                        tmapView.setCenterPoint(snappedDestination.accommodation.latitude, snappedDestination.accommodation.longitude)
                        endPoint = snappedDestination.accommodation
                    }
                    tmapView.removeAllTMapPolyLine()
                    cvRouteCarDetailInfoOpen.root.isVisible = false
                    cvRouteCarDetailInfoClose.root.isVisible = false
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
//        tmapView.zoomLevel = 10

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

    }

    private fun initListeners() = with(binding) {
        chipHome.setOnClickListener {

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
                return@setOnClickListener
            }

            lifecycleScope.launch {
                userCurrentLocation = getUserFineLocation()

                val handler = Handler(Looper.getMainLooper()) { message ->
                    if(message.what == 1) {
                        val address = message.data.getString(KEY_GPS)
                        etMapStartPoint.setText(address)
                    }
                    true
                }

                TMapData().convertGpsToAddress(userCurrentLocation.latitude, userCurrentLocation.longitude, object: TMapData.OnConvertGPSToAddressListener {
                    override fun onConverGPSToAddress(address: String?) {
                        val bundle = Bundle().apply {
                            putString(KEY_GPS, address)
                        }
                        val message = Message().apply {
                            data = bundle
                            what = 1
                        }
                        handler.sendMessage(message)
                    }
                })
            }
        }
        chipNavigate.setOnClickListener {
            if(etMapStartPoint.text.isNotBlank()) {
                Toast.makeText(context, "자동차 경로를 검색합니다.", Toast.LENGTH_SHORT).show()

                val departurePoint = TMapPoint(userCurrentLocation.latitude, userCurrentLocation.longitude)
                val arrivalPoint = TMapPoint(endPoint.latitude, endPoint.longitude)

                // 지도 간단한 UI 그리기
                val mapData = TMapData()
                mapData.findPathData(departurePoint, arrivalPoint, object: TMapData.OnFindPathDataListener {
                    override fun onFindPathData(tmapPolyLine: TMapPolyLine?) {
                        tmapPolyLine?.let {
                            it.lineWidth = 5f
                            it.lineColor = ContextCompat.getColor(requireContext(), R.color.navigate_inner)
                            it.lineAlpha = 255

                            it.outLineWidth = 8f
                            it.outLineColor = ContextCompat.getColor(requireContext(), R.color.navigate_outer)
                            it.outLineAlpha = 255

                            tmapView.addTMapPolyLine(it)

                            val info: TMapInfo = tmapView.getDisplayTMapInfo(it.linePointList)
                            tmapView.zoomLevel = info.zoom
                            tmapView.setCenterPoint(info.point.latitude, info.point.longitude)
                        }
                    }
                })

                // 상세 정보 요청하기
                mapViewModel.getTMapRoutesPrediction(context = requireContext(), tMapRoutesPredictionRequest = TMapRoutesPredictionRequest(
                    routesInfo = RoutesInfo(
                        departure = LocationInfo(
                            name = etMapStartPoint.text.toString(),
                            lon = userCurrentLocation.longitude,
                            lat = userCurrentLocation.latitude
                        ),
                        destination = LocationInfo(
                            name = endPoint.name,
                            lon = endPoint.longitude,
                            lat = endPoint.latitude
                        ),
                        predictionType = "arrival",
                        predictionTime = getCurrentTimeFormatted(),
                        searchOption = "00",
                        trafficInfo = "N"
                    )
                ))
            } else {
                Toast.makeText(context, "출발지를 설정해주세요.", Toast.LENGTH_SHORT).show()
            }
//            if(etMapStartPoint.text.isNotBlank()) {
//                mapViewModel.findTMapMultiModalRoute(context = requireContext(), tMapRouteRequest = TMapRouteRequest(
//                    startX = userCurrentLocation.longitude.toString(),
//                    startY = userCurrentLocation.latitude.toString(),
//                    endX = endPoint.longitude.toString(),
//                    endY = endPoint.latitude.toString(),
//                    count = 1
//                ))
//            } else {
//                Toast.makeText(context, "출발지를 설정해주세요.", Toast.LENGTH_SHORT).show()
//            }
        }
        cvRouteCarDetailInfoOpen.root.setOnClickListener {
            cvRouteCarDetailInfoOpen.root.isVisible = false
            cvRouteCarDetailInfoClose.root.isVisible = true
        }
        cvRouteCarDetailInfoClose.root.setOnClickListener {
            cvRouteCarDetailInfoClose.root.isVisible = false
            cvRouteCarDetailInfoOpen.root.isVisible = true
        }
    }

    private fun initObservers() = with(binding) {

        mapViewModel.tMapRoutesPredictionInfo.observe(viewLifecycleOwner) { result ->
            result.onSuccess { prediction ->
                val detailInfo = prediction.features[0].properties
                val hour = detailInfo.totalTime/3600
                val minutes = (detailInfo.totalTime/60)%60
                val formatter = SimpleDateFormat("a h:mm", Locale.getDefault())
                val formattedTotalDistance = String.format("%.1f", detailInfo.totalDistance.toInt()/1000f)
                cvRouteCarDetailInfoOpen.root.isVisible = true
                cvRouteCarDetailInfoOpen.tvCarRouteDetailTotalDistance.text = "${formattedTotalDistance}km"
                cvRouteCarDetailInfoOpen.tvCarRouteDetailHourValue.text = "$hour"
                cvRouteCarDetailInfoOpen.tvCarRouteDetailMinuteValue.text = "$minutes"
                cvRouteCarDetailInfoOpen.tvCarRouteDetailTaxiFareValue.text = Utils.formatPrice(detailInfo.taxiFare)
                cvRouteCarDetailInfoOpen.tvCarRouteDetailFuelCostValue.text = Utils.formatPrice(detailInfo.totalFare.toInt())
                cvRouteCarDetailInfoOpen.tvCarRouteDetailDeparturePredictionTimeValue.text = formatter.format(detailInfo.departureTime)
                cvRouteCarDetailInfoOpen.tvCarRouteDetailArrivalPredictionTimeValue.text = formatter.format(detailInfo.arrivalTime)
            }.onFailure { error ->
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        }

        mapViewModel.tMapMultiModalRouteInfo.observe(viewLifecycleOwner) { result ->
            result.onSuccess { route ->
                Log.e(TAG, "" + route)
            }.onFailure { error ->
                Toast.makeText(context, "" + error.message, Toast.LENGTH_SHORT).show()
            }
        }

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
//        mapViewModel.tMapTravelAccommodationsInfo.observe(viewLifecycleOwner) { accommodation ->
//            Log.e(TAG, ""+ accommodation)
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

    private fun getAttractionIconBitmap(category: String): Bitmap {
        return when(category) {
            "쇼핑" -> attractionShoppingBitmap
            "관광명소" -> attractionTourBitmap
            "레저/스포츠" -> attractionLeisureBitmap
            else -> attractionDefaultBitmap
        }
    }

    private fun addMarkerOnMap(destination: Destination) {
        val accommodationMarker = TMapMarkerItem().apply {
            id = destination.accommodation.name
            icon = accommodationBitmap
            setTMapPoint(destination.accommodation.latitude, destination.accommodation.longitude)
            canShowCallout = true
            name = destination.accommodation.minimumPrice.toString()
        }

        tmapView.addTMapMarkerItem(accommodationMarker)

        for(initialAttraction in destination.attractions) {
            val attractionMarker = TMapMarkerItem().apply {
                id = initialAttraction.poiId
                icon = getAttractionIconBitmap(initialAttraction.category)
                setTMapPoint(initialAttraction.lat, initialAttraction.lng)
                canShowCallout = true
                name = initialAttraction.poiName
            }
            tmapView.addTMapMarkerItem(attractionMarker)
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private suspend fun getUserCoarseLocation(): Location {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        return suspendCoroutine { continuation ->
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                val zonedDateTime = Instant.ofEpochMilli(location.time).atZone(ZoneId.systemDefault())
                Log.e(
                    TAG,
                    "time: $zonedDateTime, lat: ${location.latitude}, lng: ${location.longitude}, accuracy: ${location.accuracy}"
                )
                continuation.resume(location)
            }
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    private suspend fun getUserFineLocation(): Location {

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        val currentLocationRequest = CurrentLocationRequest.Builder()
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setDurationMillis(5000) // DOCS 1
            .build()

        return suspendCoroutine { continuation ->
            fusedLocationClient.getCurrentLocation(currentLocationRequest, null)
                .addOnSuccessListener { location -> // DOCS 2
                    val zonedDateTime =
                        Instant.ofEpochMilli(location.time).atZone(ZoneId.systemDefault())
                    Log.e(
                        TAG,
                        "time: $zonedDateTime, lat: ${location.latitude}, lng: ${location.longitude}, accuracy: ${location.accuracy}"
                    )
                    continuation.resume(location)
                }
        }
    }

    private fun getCurrentTimeFormatted(): String {
        val now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")
        return now.format(formatter)
    }

    companion object {
        const val TAG = "TMAP"
        const val KEY_GPS = "GPS"
    }

}