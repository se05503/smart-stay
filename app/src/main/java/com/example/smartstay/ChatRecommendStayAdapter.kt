package com.example.smartstay

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartstay.databinding.LayoutSimpleRecommendStayBinding
import com.example.smartstay.model.accommodation.Destination
import java.text.DecimalFormat

class ChatRecommendStayAdapter(
    private val recommendItems: List<Destination>,
    private val clickListener: ItemClickListener,
): RecyclerView.Adapter<ChatRecommendStayAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: LayoutSimpleRecommendStayBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Destination) {
            val accommodation = item.accommodation
            binding.root.setOnClickListener {
                clickListener.onNavigateToDetail(item)
            }
            Glide.with(binding.ivStayImage).load(accommodation.image).into(binding.ivStayImage)
            binding.tvStayName.text = accommodation.name
            binding.tvStayAddress.text = accommodation.address
            val formatter = DecimalFormat("#,###")
            binding.tvPriceMinimum.text = "${formatter.format(accommodation.minimumPrice)}원~"
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