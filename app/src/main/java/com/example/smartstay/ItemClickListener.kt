package com.example.smartstay

import com.example.smartstay.model.accommodation.AccommodationInfo

interface ItemClickListener {
    fun onNavigateToDetail(item: AccommodationInfo)
}