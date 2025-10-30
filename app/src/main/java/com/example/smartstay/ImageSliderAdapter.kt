package com.example.smartstay

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartstay.databinding.ItemImageSliderBinding

class ImageSliderAdapter(private val imageList: List<Int>): RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>() {

    inner class ImageSliderViewHolder(private val binding: ItemImageSliderBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(image: Int) = with(binding) {
            Glide.with(ivSliderStayImage).load(image).into(ivSliderStayImage)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ImageSliderViewHolder {
        return ImageSliderViewHolder(ItemImageSliderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: ImageSliderViewHolder,
        position: Int
    ) {
        holder.bind(imageList[position])
    }

    override fun getItemCount(): Int {
        return imageList.size
    }
}