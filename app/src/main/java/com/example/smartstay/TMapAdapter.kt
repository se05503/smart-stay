package com.example.smartstay

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartstay.databinding.ItemTmapAccomodationBinding
import com.example.smartstay.model.AccommodationInfo

class TMapAdapter(private val onClicked: (AccommodationInfo) -> Unit): ListAdapter<AccommodationInfo, TMapAdapter.TMapViewHolder>(differ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TMapViewHolder {
        val binding = ItemTmapAccomodationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TMapViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TMapViewHolder,
        position: Int
    ) {
        holder.bind(currentList[position])
    }

    inner class TMapViewHolder(private val binding: ItemTmapAccomodationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AccommodationInfo) = with(binding) {
            Glide.with(ivAccommodationPhoto).load(item.image).into(ivAccommodationPhoto)
            tvTMapAccommodationName.text = item.name
            tvTMapAccommodationAddress.text = item.address
            tvTMapAccommodationMinimumPrice.text = "${Utils.formatPrice(item.minimumPrice)}~"
            root.setOnClickListener {
                onClicked(item)
            }
        }
    }

    companion object {
        val differ = object : DiffUtil.ItemCallback<AccommodationInfo>() {
            override fun areItemsTheSame(
                oldItem: AccommodationInfo,
                newItem: AccommodationInfo
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: AccommodationInfo,
                newItem: AccommodationInfo
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}