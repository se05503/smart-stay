package com.example.smartstay

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartstay.databinding.ItemRecommendStayBinding
import com.naver.maps.geometry.LatLng

class RecommendStaysAdapter(private val onClick: (LatLng) -> Unit): RecyclerView.Adapter<RecommendStaysAdapter.ViewHolder>() {

    private var recommendStayList = emptyList<AccommodationInfo>()

    inner class ViewHolder(val binding: ItemRecommendStayBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AccommodationInfo) {
            binding.ivStayImage.setImageResource(item.image)
            binding.tvStayName.text = item.name
            binding.tvStayAddress.text = item.address
            binding.tvStayPrice.text = "${item.pricePerNight}원 / 박"
            binding.root.setOnClickListener {
                onClick(LatLng(item.latitude.toDouble(), item.longitude.toDouble())) // LatLng(위도, 경도) = LatLng(latitude, longitude)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(ItemRecommendStayBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = recommendStayList.size

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(recommendStayList[position])
    }

    fun setData(recommendStayList: List<AccommodationInfo>) {
        this.recommendStayList = recommendStayList
    }
}