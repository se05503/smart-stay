package com.example.smartstay

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smartstay.databinding.FragmentInitialSettingStartBinding

class InitialSettingStartFragment: Fragment(R.layout.fragment_initial_setting_start) {

    private lateinit var binding: FragmentInitialSettingStartBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentInitialSettingStartBinding.bind(view)
        binding.btnInitialSettingStart.setOnClickListener { findNavController().navigate(R.id.action_navigation_initial_setting_start_to_navigation_onboarding) }
    }
}