package com.example.smartstay

import com.example.smartstay.model.accommodation.Destination

interface ItemClickListener {
    fun onNavigateToDetail(item: Destination)
}