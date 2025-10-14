package com.example.smartstay

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.smartstay.databinding.ActivityStayDetailBinding
import com.example.smartstay.model.AccommodationInfo
import java.text.DecimalFormat

class StayDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStayDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStayDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val accommodationInfo = intent.getSerializableExtra(BUNDLE_KEY) as AccommodationInfo
        with(binding) {
            ivDetailBack.setOnClickListener { finish() }
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
        }
    }

    companion object {
        const val BUNDLE_KEY = "ACCOMMODATION_DETAIL_KEY"
    }
}