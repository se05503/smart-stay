package com.example.smartstay

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.smartstay.databinding.FragmentTmapBinding
import com.example.smartstay.network.RetrofitInstance
import java.io.InputStream

class TMapFragment : Fragment(R.layout.fragment_tmap) {

    private lateinit var binding: FragmentTmapBinding
    private val mapViewModel: MapViewModel by activityViewModels {
        MapViewModelFactory(RetrofitInstance.networkService)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTmapBinding.bind(view)
        fetchRemoteSource()
        observeRemoteSource()
    }

    private fun fetchRemoteSource() {
        mapViewModel.getTMapThumbnailImage(
            longitude = 126.98452047f,
            latitude = 37.56656541f,
            zoom = 15,
            context = requireContext()
        )
    }

    private fun observeRemoteSource() = with(binding) {
        mapViewModel.tMapThumbnailImage.observe(viewLifecycleOwner) { response ->
            val inputStream: InputStream = response.byteStream()
            val bitMap: Bitmap = BitmapFactory.decodeStream(inputStream)
            ivTmapSampleTestThumbnail.setImageBitmap(bitMap)
        }
    }

}