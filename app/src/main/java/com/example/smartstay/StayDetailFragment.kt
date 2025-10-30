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
import com.example.smartstay.model.accommodation.Accommodation
import com.example.smartstay.model.accommodation.Attraction
import com.example.smartstay.model.accommodation.Destination
import com.example.smartstay.network.RetrofitInstance

class StayDetailFragment : Fragment(R.layout.fragment_stay_detail) {

    private lateinit var binding: FragmentStayDetailBinding
    private val mapViewModel: MapViewModel by activityViewModels {
        MapViewModelFactory(RetrofitInstance.skTMapNetworkService)
    }

    private val destination: Destination by lazy {
        val args: StayDetailFragmentArgs by navArgs()
        args.destinationInfo
    }
    private val accommodation: Accommodation by lazy {
        destination.accommodation
    }
    private val attractions: List<Attraction> by lazy {
        destination.attractions
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStayDetailBinding.bind(view)
        initViews()
        initListeners()
        initObservers()
    }

    private fun initViews() = with(binding) {
        viewpagerDetailStayImage.adapter = ImageSliderAdapter(accommodation.image)
        imageDotsIndicator.attachTo(viewpagerDetailStayImage)
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
        // TODO: 근처 관광지 관련 정보 UI 추가로 만들어서 표시하기
    }

    private fun initListeners() = with(binding) {
        ivDetailBack.setOnClickListener { findNavController().popBackStack() }
        cvDetailMapThumbnail.setOnClickListener {
            val action = StayDetailFragmentDirections.actionStayDetailFragmentToTMapVectorFragment(destination)
            findNavController().navigate(action)
        }
    }

    private fun initObservers() = with(binding) {
        mapViewModel.tMapThumbnailImage.observe(viewLifecycleOwner) { response ->
            val inputStream = response.byteStream() // ResponseBody → InputStream
            val bitmap = BitmapFactory.decodeStream(inputStream) // InputStream → Bitmap
            ivDetailMapThumbnail.setImageBitmap(bitmap)
        }
        mapViewModel.getTMapThumbnailImage(longitude = accommodation.longitude, latitude = accommodation.latitude, markers = "${accommodation.longitude}%2C${accommodation.latitude}", zoom = 14, context = requireContext())
    }

}