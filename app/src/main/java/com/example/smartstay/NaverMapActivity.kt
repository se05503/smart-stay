package com.example.smartstay

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smartstay.databinding.ActivityNaverMapBinding
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import android.util.Log
import com.naver.maps.map.CameraAnimation

class NaverMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityNaverMapBinding
    private lateinit var mapView: MapView // 지도에 대한 뷰 역할만 담당
    private lateinit var naverMap: NaverMap // 실제 API 호출하는데 필요 + 인터페이스 역할을 하는 객체
    private var isMapInit = false

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

        // LatLng(위도, 경도) = LatLng(latitude, longitude)

        val positions = listOf(
            LatLng(37.5665, 126.9780), // 시청
            LatLng(37.5658, 126.9830), // 명동
            LatLng(37.5700, 126.9769), // 경복궁
            LatLng(37.5611, 126.9827), // 을지로
            LatLng(37.5642, 126.9758)  // 서울역
        )

        val names = listOf(
            "시청 프리미엄 호텔",
            "명동 비즈니스 호텔",
            "경복궁 스테이",
            "을지로 게스트하우스",
            "서울역 럭셔리 숙소"
        )

        for(i in positions.indices) {
            val marker = Marker().apply {
                position = positions[i]
                captionText = names[i]
                map = naverMap
                val cameraUpdate = CameraUpdate.scrollTo(position).animate(CameraAnimation.Easing)
                naverMap.moveCamera(cameraUpdate)
            }
        }


    }


}