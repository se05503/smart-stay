package com.example.smartstay

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smartstay.databinding.ActivityNaverMapBinding
import com.example.smartstay.model.AccommodationInfo
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
    private lateinit var accommodationList: ArrayList<AccommodationInfo>
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

        accommodationList = intent.getSerializableExtra("accommodation_list") as? ArrayList<AccommodationInfo> ?: return

        mapView = binding.naverMapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        recommendStaysAdapter = RecommendStaysAdapter(onClick = { latlng ->
            if(isMapInit) {
                val cameraUpdate = CameraUpdate.scrollTo(latlng).animate(CameraAnimation.Easing)
                naverMap.moveCamera(cameraUpdate)
            }
        })

        recommendStaysAdapter.setData(accommodationList.toList())
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
        val markers = accommodationList.map {
            Marker(LatLng(it.latitude.toDouble(), it.longitude.toDouble())).apply {
                captionText = it.name
                map = naverMap
            }
        }
        val cameraUpdate = CameraUpdate.scrollTo(markers.first().position).animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)
    }
}