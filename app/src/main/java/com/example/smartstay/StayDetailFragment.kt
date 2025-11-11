package com.example.smartstay

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
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
import com.google.android.material.card.MaterialCardView
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
        tvDetailRatingStar.text = "${accommodation.starRating}급"
        val today = LocalDate.now()
        val tomorrow = today.plusDays(1)
        val formatter = DateTimeFormatter.ofPattern("MM.dd")
        val todayFormatted = today.format(formatter)
        val tomorrowFormatted = tomorrow.format(formatter)
        tvStayDetailSpecificDuration.text = "$todayFormatted - $tomorrowFormatted 숙박 시"
        tvStayDetailMaximumPrice.text = "${Utils.formatPrice(accommodation.maximumPrice)} / 박"
        tvStayDetailCurrentPrice.text = "${Utils.formatPrice(accommodation.averagePrice)} / 박"
        tvStayDetailMinimumPrice.text = "${Utils.formatPrice(accommodation.minimumPrice)} / 박"
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

        llStayDetailAttractionContainer.removeAllViews()
        attractions.forEachIndexed { index, attraction ->
            val attractionView = LayoutInflater.from(context).inflate(R.layout.layout_attraction_introduction, llStayDetailAttractionContainer, false)
            attractionView.findViewById<TextView>(R.id.tvAttractionIntroductionNumber).text = "${index+1}"
            attractionView.findViewById<TextView>(R.id.tvAttractionIntroductionName).text = attraction.poiName
            if(attraction.category == SHOPPING) {
                attractionView.findViewById<MaterialCardView>(R.id.cvAttractionIntroductionCategory).setCardBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.shopping_fill))
                attractionView.findViewById<TextView>(R.id.tvAttractionIntroductionCategory).text = SHOPPING
            } else if(attraction.category == TOUR) {
                attractionView.findViewById<MaterialCardView>(R.id.cvAttractionIntroductionCategory).setCardBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.tour_fill))
                attractionView.findViewById<TextView>(R.id.tvAttractionIntroductionCategory).text = TOUR
            } else {
                attractionView.findViewById<MaterialCardView>(R.id.cvAttractionIntroductionCategory).setCardBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.leisure_fill))
                attractionView.findViewById<TextView>(R.id.tvAttractionIntroductionCategory).text = LEISURE
            }
            attractionView.findViewById<TextView>(R.id.tvAttractionIntroductionSub).text = getSubDescription()
            llStayDetailAttractionContainer.addView(attractionView)
        }
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

    private fun getSubDescription(): String {
        val subDescriptionList = listOf(
            "#여행객", "#힐링여행", "#이색체험", "#많이찾는", "#현지인", "#인생샷", "#추천"
        )
        return subDescriptionList.random()
    }

    companion object {
        const val SHOPPING = "쇼핑"
        const val LEISURE = "레저/스포츠"
        const val TOUR = "관광명소"
    }

}