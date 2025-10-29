package com.example.smartstay.presentation

import androidx.lifecycle.ViewModel
import com.example.smartstay.model.accommodation.Destination

class AccommodationViewModel: ViewModel() {
    var recommendDestinationList: List<Destination> = emptyList()
}