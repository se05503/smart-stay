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
        Glide.with(ivDetailStayImage).load(accommodation.image).into(ivDetailStayImage)
        tvDetailStayName.text = accommodation.name
        tvDetailStayType.text = accommodation.type
        tvDetailStayLocation.text = accommodation.address
        tvDetailRatingStar.text = accommodation.starRating
        tvPriceAverage.text = Utils.formatPrice(accommodation.averagePrice)
        tvPriceMinimum.text = Utils.formatPrice(accommodation.minimumPrice)
        tvPriceMaximum.text = Utils.formatPrice(accommodation.maximumPrice)
        if(accommodation.isPetAvailable == "N") ivAmenityPet.alpha = 0.3f
        if(accommodation.isRestaurantExist == "N") ivAmenityRestaurant.alpha = 0.3f
        if(accommodation.isBarExist == "N") ivAmenityBar.alpha = 0.3f
        if(accommodation.isCafeExist == "N") ivAmenityCafe.alpha = 0.3f
        if(accommodation.isFitnessCenterExist == "N") ivAmenityFitness.alpha = 0.3f
        if(accommodation.isSwimmingPoolExist == "N") ivAmenitySwimmingPool.alpha = 0.3f
        if(accommodation.isSpaExist == "N") ivAmenitySpa.alpha = 0.3f
        if(accommodation.isSaunaExist == "N") ivAmenitySauna.alpha = 0.3f
        if(accommodation.isReceptionCenterExist == "N") ivAmenityReceptionHall.alpha = 0.3f
        if(accommodation.isBusinessCenterExist == "N") ivAmenityBusiness.alpha = 0.3f
        if(accommodation.isOceanViewExist == "N") ivAmenityOceanView.alpha = 0.3f
        tvDetailMapLocation.text = accommodation.address
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