package com.example.smartstay.presentation.map

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.smartstay.MapViewModel
import com.example.smartstay.MapViewModelFactory
import com.example.smartstay.R
import com.example.smartstay.databinding.FragmentDepartureFocusBinding
import com.example.smartstay.model.tmap.Poi
import com.example.smartstay.network.RetrofitInstance

class DepartureFocusFragment : Fragment(R.layout.fragment_departure_focus) {

    private lateinit var binding: FragmentDepartureFocusBinding
    private val mapViewModel: MapViewModel by activityViewModels {
        MapViewModelFactory(RetrofitInstance.skTMapNetworkService)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDepartureFocusBinding.bind(view)
        initListeners()
        initObservers()
    }

    private fun initListeners() = with(binding) {
        ivDepartureFocusSearch.setOnClickListener {
            if(etDepartureFocusPoint.text.isBlank()) {
                Toast.makeText(context, "출발지를 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                mapViewModel.searchIntegratedPlaces(
                    context = requireContext(),
                    searchKeyword = etDepartureFocusPoint.text.toString()
                )
            }
        }
        ivDepartureFocusBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initObservers() = with(binding) {
        mapViewModel.tMapIntegratedPlaces.observe(viewLifecycleOwner) { result ->
            result.onSuccess { integratedPlaces ->
                val filterData: List<Poi> = integratedPlaces.searchPoiInfo.pois.poi
                Log.e("TTEST", "" + filterData)
                val searchPointAdapter = SearchPointAdapter()
                recyclerviewDepartureFocusContent.adapter = searchPointAdapter
                searchPointAdapter.submitList(filterData)
            }.onFailure { error ->
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}