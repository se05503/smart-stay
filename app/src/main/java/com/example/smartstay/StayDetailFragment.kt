package com.example.smartstay

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.smartstay.databinding.FragmentStayDetailBinding
import com.example.smartstay.model.accommodation.AccommodationInfo
import com.example.smartstay.network.RetrofitInstance

class StayDetailFragment : Fragment(R.layout.fragment_stay_detail) {

    private lateinit var binding: FragmentStayDetailBinding
    private val mapViewModel: MapViewModel by activityViewModels {
        MapViewModelFactory(RetrofitInstance.skTMapNetworkService)
    }
    private lateinit var accommodationInfo: AccommodationInfo

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStayDetailBinding.bind(view)
        val args: StayDetailFragmentArgs by navArgs()
        accommodationInfo = args.accommodationInfo
        initViews()
        initListeners()
        initObservers()
    }

    private fun initViews() = with(binding) {
        Glide.with(ivDetailStayImage).load(accommodationInfo.image).into(ivDetailStayImage)
        tvDetailStayName.text = accommodationInfo.name
        tvDetailStayType.text = accommodationInfo.type
        tvDetailStayLocation.text = accommodationInfo.address
        tvDetailRatingFinal.text = "${accommodationInfo.finalRating}.0"
        tvDetailRatingStar.text = accommodationInfo.starRating
        tvPriceAverage.text = Utils.formatPrice(accommodationInfo.averagePrice)
        tvPriceMinimum.text = Utils.formatPrice(accommodationInfo.minimumPrice)
        tvPriceMaximum.text = Utils.formatPrice(accommodationInfo.maximumPrice)
        if(accommodationInfo.isPetAvailable == "N") ivAmenityPet.alpha = 0.3f
        if(accommodationInfo.isRestaurantExist == "N") ivAmenityRestaurant.alpha = 0.3f
        if(accommodationInfo.isBarExist == "N") ivAmenityBar.alpha = 0.3f
        if(accommodationInfo.isCafeExist == "N") ivAmenityCafe.alpha = 0.3f
        if(accommodationInfo.isFitnessCenterExist == "N") ivAmenityFitness.alpha = 0.3f
        if(accommodationInfo.isSwimmingPoolExist == "N") ivAmenitySwimmingPool.alpha = 0.3f
        if(accommodationInfo.isSpaExist == "N") ivAmenitySpa.alpha = 0.3f
        if(accommodationInfo.isSaunaExist == "N") ivAmenitySauna.alpha = 0.3f
        if(accommodationInfo.isReceptionCenterExist == "N") ivAmenityReceptionHall.alpha = 0.3f
        if(accommodationInfo.isBusinessCenterExist == "N") ivAmenityBusiness.alpha = 0.3f
        if(accommodationInfo.isOceanViewExist == "N") ivAmenityOceanView.alpha = 0.3f
        tvDetailMapLocation.text = accommodationInfo.address
    }

    private fun initListeners() = with(binding) {
        ivDetailBack.setOnClickListener { findNavController().popBackStack() }
        cvDetailMapThumbnail.setOnClickListener {
            val action = StayDetailFragmentDirections.actionStayDetailFragmentToTMapVectorFragment(accommodationInfo)
            findNavController().navigate(action)
        }
    }

    private fun initObservers() = with(binding) {
        mapViewModel.tMapThumbnailImage.observe(viewLifecycleOwner) { response ->
            val inputStream = response.byteStream() // ResponseBody → InputStream
            val bitmap = BitmapFactory.decodeStream(inputStream) // InputStream → Bitmap
            ivDetailMapThumbnail.setImageBitmap(bitmap)
        }
        mapViewModel.getTMapThumbnailImage(longitude = accommodationInfo.longitude, latitude = accommodationInfo.latitude, markers = "${accommodationInfo.longitude}%2C${accommodationInfo.latitude}", zoom = 14, context = requireContext())
    }

}