package com.example.smartstay

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartstay.databinding.ItemRecommendStayBinding

class RecommendStaysAdapter: RecyclerView.Adapter<RecommendStaysAdapter.ViewHolder>() {

    private var recommendStayList = emptyList<AccommodationInfo>()

    inner class ViewHolder(val binding: ItemRecommendStayBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AccommodationInfo) {
            binding.ivStayImage.setImageResource(item.image)
            binding.tvStayName.text = item.name
            binding.tvStayAddress.text = item.address
            binding.tvStayPrice.text = "${item.pricePerNight}원 / 박"
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