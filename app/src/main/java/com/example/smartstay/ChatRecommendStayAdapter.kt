package com.example.smartstay

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.smartstay.databinding.LayoutSimpleRecommendStayBinding
import com.example.smartstay.model.AccommodationInfo

class ChatRecommendStayAdapter(private val recommendItems: List<AccommodationInfo>): RecyclerView.Adapter<ChatRecommendStayAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: LayoutSimpleRecommendStayBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AccommodationInfo) {
            binding.root.setOnClickListener {
                // 상세 페이지로 이동하기
            }
            binding.ivStayImage.setImageResource(item.image)
            binding.tvStayName.text = item.name
            binding.tvStayAddress.text = item.address
            binding.tvPriceMinimum.text = "${item.minimumPrice}원~"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutSimpleRecommendStayBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recommendItems[position])
    }

    override fun getItemCount(): Int = recommendItems.size
}