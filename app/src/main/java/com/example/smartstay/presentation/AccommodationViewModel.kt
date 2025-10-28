package com.example.smartstay.presentation

import androidx.lifecycle.ViewModel
import com.example.smartstay.model.accommodation.AccommodationInfo

class AccommodationViewModel: ViewModel() {
    var recommendAccommodationList: List<AccommodationInfo> = emptyList()
}