package com.example.smartstay

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.smartstay.databinding.ItemTmapAccomodationBinding
import com.example.smartstay.model.accommodation.Destination

class TMapAdapter(private val onClicked: (Destination) -> Unit): ListAdapter<Destination, TMapAdapter.TMapViewHolder>(differ) {

    inner class TMapViewHolder(private val binding: ItemTmapAccomodationBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Destination) = with(binding) {
            val accommodation = item.accommodation
            Glide.with(ivAccommodationPhoto).load(accommodation.image.first()).into(ivAccommodationPhoto)
            tvTMapAccommodationType.text = accommodation.type
            tvTMapAccommodationName.text = accommodation.name
            tvTMapAccommodationAddress.text = accommodation.address
            tvTMapAccommodationMinimumPrice.text = "${Utils.formatPrice(accommodation.minimumPrice)}~"
            root.setOnClickListener {
                onClicked(item)
            }
        }
    }

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

    companion object {
        val differ = object : DiffUtil.ItemCallback<Destination>() {
            override fun areItemsTheSame(
                oldItem: Destination,
                newItem: Destination
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: Destination,
                newItem: Destination
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}