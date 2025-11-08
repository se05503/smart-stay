package com.example.smartstay.presentation.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smartstay.databinding.ItemSearchPointBinding
import com.example.smartstay.model.tmap.Poi

class SearchPointAdapter : ListAdapter<Poi, SearchPointAdapter.SearchPointViewHolder>(differ) {

    inner class SearchPointViewHolder(private val binding: ItemSearchPointBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Poi) = with(binding) {
            tvSearchPointName.text = item.name
            tvSearchPointRoadNameAddress.text = item.fullRoadNameAddress
            tvSeachPointBusinessName.text = item.lowerBizName
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchPointViewHolder {
        return SearchPointViewHolder(ItemSearchPointBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: SearchPointViewHolder,
        position: Int
    ) {
        holder.bind(currentList[position])
    }

    companion object {
        val differ = object : DiffUtil.ItemCallback<Poi>() {
            override fun areItemsTheSame(
                oldItem: Poi,
                newItem: Poi
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Poi,
                newItem: Poi
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}


