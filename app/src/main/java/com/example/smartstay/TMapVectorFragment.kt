package com.example.smartstay

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.smartstay.databinding.FragmentTMapVectorBinding
import com.skt.tmap.TMapPoint
import com.skt.tmap.TMapView
import com.skt.tmap.overlay.TMapMarkerCluster
import com.skt.tmap.overlay.TMapMarkerItem
import com.skt.tmap.poi.TMapPOIItem
import androidx.core.graphics.scale
import com.skt.tmap.TMapData
import com.skt.tmap.TMapInfo
import com.skt.tmap.overlay.TMapPolyLine
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import androidx.fragment.app.activityViewModels
import com.example.smartstay.model.TMapRouteRequest
import com.example.smartstay.network.RetrofitInstance

class TMapVectorFragment : Fragment(R.layout.fragment_t_map_vector) {

    private lateinit var binding: FragmentTMapVectorBinding
    private val mapViewModel: MapViewModel by activityViewModels {
        MapViewModelFactory(RetrofitInstance.networkService)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTMapVectorBinding.bind(view)
        initViews()
        initObservers()
    }

    private fun initViews() = with(binding) {

        /**
         * 지도 생성하기 (필요 O)
         */
        val tmapView = TMapView(requireContext())
        tmapVectorView.addView(tmapView)
        tmapView.setSKTMapApiKey(getString(R.string.sk_telecom_open_api_app_key))
        tmapView.setOnMapReadyListener(object: TMapView.OnMapReadyListener {
            override fun onMapReady() {
                /**
                 * 자동차 경로안내
                 * 처음 작성할 때 onMapReady 바깥에 둠 → 지도에 안보임
                 * tmapView가 뷰에 attach된 직후 호출해야 한다.
                 * 만약 아직 맵이 준비되기 전에 findPathData()를 호출하면 PolyLine이 화면에 안 뜰 수 있다.
                 */
                val startPoint = TMapPoint(37.570841, 126.985302) // 출발지 = SKT타워
                val endPoint = TMapPoint(37.551135, 126.988205)
                val tmapData = TMapData()
                tmapData.findPathData(startPoint, endPoint, object: TMapData.OnFindPathDataListener {
                    override fun onFindPathData(tmapPolyLine: TMapPolyLine?) {
                        tmapPolyLine?.let {
                            it.lineWidth = 3f
                            it.lineColor = Color.BLUE
                            it.lineAlpha = 255

                            it.outLineWidth = 5f
                            it.outLineColor = Color.RED
                            it.outLineAlpha = 255

                            tmapView.addTMapPolyLine(it)

                            val info: TMapInfo = tmapView.getDisplayTMapInfo(it.linePointList)
                            tmapView.zoomLevel = info.zoom
                            tmapView.setCenterPoint(info.point.latitude, info.point.longitude)
                        }
                    }
                })

                /**
                 * 리버스 지오코딩
                 * 좌표(위도, 경도) → 주소로 변환
                 * TMapData 객체의 convertGpsToAddress 메소드 사용
                 */
                val handler = Handler(Looper.getMainLooper()) { message ->
                    if(message.what == 1) {
                        val address = message.data.getString("address")
                        Toast.makeText(context, address, Toast.LENGTH_SHORT).show()
                    }
                    false
                }

                TMapData().convertGpsToAddress(37.570841, 126.985302, object: TMapData.OnConvertGPSToAddressListener {
                    override fun onConverGPSToAddress(address: String?) {
                        val message = Message()
                        val bundle = Bundle()
                        bundle.putString("address", address)
                        message.data = bundle
                        message.what = 1
                        handler.sendMessage(message)
                    }
                })
            }
        })

        /**
         * 지도 이벤트 설정하기 (필요 X)
         */
        tmapView.setOnClickListenerCallback(object: TMapView.OnClickListenerCallback {
            override fun onPressDown(
                p0: ArrayList<TMapMarkerItem?>?,
                p1: ArrayList<TMapPOIItem?>?,
                p2: TMapPoint?,
                p3: PointF?
            ) {
                Toast.makeText(context, "onPressDown", Toast.LENGTH_SHORT).show()
            }

            override fun onPressUp(
                p0: ArrayList<TMapMarkerItem?>?,
                p1: ArrayList<TMapPOIItem?>?,
                p2: TMapPoint?,
                p3: PointF?
            ) {
                Toast.makeText(context, "onPressUp", Toast.LENGTH_SHORT).show()
            }
        })

        /**
         * 지도 중심점 및 레벨 변경하기 (필요 O)
         * 동적으로 사용할 듯.. 중심점은 사용자의 현재 위치, 줌 레벨은 상황에 따라 동적 변경
         */
        tmapView.setCenterPoint(37.468954, 126.4544153)
        tmapView.zoomLevel = 10

        /**
         * 지도 타입 변경하기 (필요 X)
         * DEFAULT, PUBLIC 비슷해 보이는데 뭔 차이지?
         */
        tmapView.mapType = TMapView.MapType.DEFAULT
        tmapView.mapType = TMapView.MapType.PUBLIC

        /**
         * 마커 생성하기 (필요 O)
         * BitmapFactory.decodeResource()는 비트맵(PNG/JPG)만 읽을 수 있음, SVG 파일 사용 못함
         * Bitmap = pixel 단위, png/jpg, 확대하면 깨짐
         * png 단위인데 크기 조절 필요해보임
         */
        val marker = TMapMarkerItem().apply {
            id = "marker1"
            setTMapPoint(37.468954, 126.4544153)
            icon = BitmapFactory.decodeResource(resources, R.drawable.ic_sample_marker)
        }
        tmapView.addTMapMarkerItem(marker)

        /**
         * 마커 클러스터 이용하기 (필요 X)
         * clusterMarkerList 에다가 marker 넣어야 지도에 표시됨
         * clusteringDistance = 100.0 → 지도 상에서 100px 이내에 있는 마커들은 한 클러스터로 묶임
         * clusterMaxSize = 100 → 한 클러스터에는 최대 100개의 마커가 들어감
         */
        val markerCluster = TMapMarkerCluster(tmapView)
        val clusterMarkerList: List<TMapMarkerItem> = emptyList()
        markerCluster.clusteringDistance = 100.0
        markerCluster.clusterMaxSize = 100
        markerCluster.markers = clusterMarkerList

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
        val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_sample_marker)
        val resizedBitmap = originalBitmap.scale(150, 150, false)
        val marker = TMapMarkerItem().apply {
            id = "marker1"
            icon = resizedBitmap
            setTMapPoint(37.5665, 126.9780)
            calloutTitle = "서울시청"
            calloutSubTitle = "서울특별시 중구 세종대로 110"
            canShowCallout = true
            isAnimation = true
        }
        tmapView.addTMapMarkerItem(marker)

        /**
         * 선 그리기
         * 대중교통/차 네비게이션 표시할 때 사용할 수 있어보임
         * 단, 중심 좌표 설정은 따로 해야할 듯 → "자동차 경로안내" 에 잘 나와있음
         */
        val pointList = arrayListOf<TMapPoint>()
        pointList.add(TMapPoint(37.472678, 126.920928))
        pointList.add(TMapPoint(37.494473, 127.120742))
        pointList.add(TMapPoint(37.405619, 127.091903))
        val line = TMapPolyLine("line1", pointList)
        tmapView.addTMapPolyLine(line)


    }

    private fun initObservers() = with(binding) {
        mapViewModel.tMapMultiModelRouteInfo.observe(viewLifecycleOwner) { routeInfo ->
            Log.e(TAG, ""+ routeInfo)
        }
        mapViewModel.findTMapMultiModalRouteSummary(
            tMapRouteRequest = TMapRouteRequest(
                startX = "126.936928",
                startY = "37.555162",
                endX = "127.029281",
                endY = "37.564436",
            ),
            context = requireContext()
        )

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

        mapViewModel.getTravelMonthlyDistrictDuration(context = requireContext(), districtCode = "5013032023")
        mapViewModel.tMapTravelDistrictDurationInfo.observe(viewLifecycleOwner) { travelDistrictDurationInfo ->
            Log.e(TAG, ""+travelDistrictDurationInfo)
        mapViewModel.getTravelAccommodationRanking(context = requireContext(), poiId = "6453368")
        mapViewModel.tMapTravelAccommodationRankingInfo.observe(viewLifecycleOwner) { travelAccommodationRankingInfo ->
            Log.e(TAG, ""+travelAccommodationRankingInfo)
        mapViewModel.getTravelDistrictAccommodationRanking(context = requireContext(), districtCode = "5000000000")
        mapViewModel.tMapTravelDistrictAccommodationRankingInfo.observe(viewLifecycleOwner) { travelDistrictAccommodationRankingInfo ->
            Log.e(TAG, ""+travelDistrictAccommodationRankingInfo)
        }
    }

    companion object {
        const val TAG = "TMAP"
    }

}