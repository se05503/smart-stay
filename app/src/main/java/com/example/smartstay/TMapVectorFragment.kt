package com.example.smartstay

import android.graphics.BitmapFactory
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

class TMapVectorFragment : Fragment(R.layout.fragment_t_map_vector) {

    private lateinit var binding: FragmentTMapVectorBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTMapVectorBinding.bind(view)
        initViews()
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
                // TODO: 맵 로딩 완료 후 구현
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
    }

}