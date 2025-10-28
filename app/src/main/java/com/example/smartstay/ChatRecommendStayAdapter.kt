package com.example.smartstay

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartstay.databinding.LayoutSimpleRecommendStayBinding
import com.example.smartstay.model.accommodation.AccommodationInfo
import java.text.DecimalFormat

class ChatRecommendStayAdapter(
    private val recommendItems: List<AccommodationInfo>,
    private val clickListener: ItemClickListener,
): RecyclerView.Adapter<ChatRecommendStayAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: LayoutSimpleRecommendStayBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AccommodationInfo) {
            binding.root.setOnClickListener {
                clickListener.onNavigateToDetail(item)
            }
            Glide.with(binding.ivStayImage).load(item.image).into(binding.ivStayImage)
            binding.tvStayName.text = item.name
            binding.tvStayAddress.text = item.address
            val formatter = DecimalFormat("#,###")
            binding.tvPriceMinimum.text = "${formatter.format(item.minimumPrice)}원~"
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