package com.example.smartstay

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartstay.databinding.ActivityNaverMapBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker

class NaverMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityNaverMapBinding
    private lateinit var mapView: MapView // 지도에 대한 뷰 역할만 담당
    private lateinit var naverMap: NaverMap // 실제 API 호출하는데 필요 + 인터페이스 역할을 하는 객체
    private lateinit var recommendStaysAdapter: RecommendStaysAdapter
    private var isMapInit = false
    private val dummyData = listOf(
        AccommodationInfo(
            name = "시청 프리미엄 호텔",
            pricePerNight = 120000,
            address = "서울 중구 세종대로 110",
            image = R.drawable.img_stay_1,
            latitude = 37.5665f,
            longitude = 126.9780f
        ),
        AccommodationInfo(
            name = "명동 비즈니스 호텔",
            pricePerNight = 95000,
            address = "서울 중구 명동길 14",
            image = R.drawable.img_stay_2,
            latitude = 37.5658f,
            longitude = 126.9830f
        ),
        AccommodationInfo(
            name = "경복궁 스테이",
            pricePerNight = 135000,
            address = "서울 종로구 사직로 161",
            image = R.drawable.img_stay_3,
            latitude = 37.5700f,
            longitude = 126.9769f
        ),
        AccommodationInfo(
            name = "을지로 게스트하우스",
            pricePerNight = 55000,
            address = "서울 중구 을지로3가 67-1",
            image = R.drawable.img_stay_4,
            latitude = 37.5611f,
            longitude = 126.9827f
        ),
        AccommodationInfo(
            name = "서울역 럭셔리 숙소",
            pricePerNight = 150000,
            address = "서울 중구 한강대로 405",
            image = R.drawable.img_stay_5,
            latitude = 37.5642f,
            longitude = 126.9758f
        ),
        AccommodationInfo(
            name = "덕수궁 레지던스",
            pricePerNight = 88000,
            address = "서울 중구 정동길 21",
            image = R.drawable.img_stay_6,
            latitude = 37.5662f,
            longitude = 126.9745f
        ),
        AccommodationInfo(
            name = "청계천 뷰 호텔",
            pricePerNight = 110000,
            address = "서울 종로구 청계천로 67",
            image = R.drawable.img_stay_7,
            latitude = 37.5687f,
            longitude = 126.9810f
        ),
        AccommodationInfo(
            name = "광화문 리빙텔",
            pricePerNight = 60000,
            address = "서울 종로구 종로1길 50",
            image = R.drawable.img_stay_8,
            latitude = 37.5725f,
            longitude = 126.9765f
        ),
        AccommodationInfo(
            name = "남대문 모텔",
            pricePerNight = 45000,
            address = "서울 중구 남대문시장4길 18",
            image = R.drawable.img_stay_9,
            latitude = 37.5599f,
            longitude = 126.9770f
        ),
        AccommodationInfo(
            name = "충무로 패밀리 하우스",
            pricePerNight = 70000,
            address = "서울 중구 충무로2가 65",
            image = R.drawable.img_stay_10,
            latitude = 37.5615f,
            longitude = 126.9930f
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNaverMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mapView = binding.naverMapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        recommendStaysAdapter = RecommendStaysAdapter(onClick = { latlng ->
            if(isMapInit) {
                val cameraUpdate = CameraUpdate.scrollTo(latlng).animate(CameraAnimation.Easing)
                naverMap.moveCamera(cameraUpdate)
            }
        })

        recommendStaysAdapter.setData(dummyData)
        binding.bottomSheetLayout.recyclerviewStayList.apply {
            layoutManager = LinearLayoutManager(this@NaverMapActivity)
            adapter = recommendStaysAdapter
        }

    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(naverMap: NaverMap) {
        // NaverMap 객체가 준비되면 호출됨
        this.naverMap = naverMap
        isMapInit = true
        // 마커 표시
        val markers = dummyData.map {
            Marker(LatLng(it.latitude.toDouble(), it.longitude.toDouble())).apply {
                captionText = it.name
                map = naverMap
            }
        }
        val cameraUpdate = CameraUpdate.scrollTo(markers.first().position).animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)
    }
}