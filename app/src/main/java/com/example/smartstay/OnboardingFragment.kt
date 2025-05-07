package com.example.smartstay

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.smartstay.databinding.FragmentOnboardingBinding

class OnboardingFragment: Fragment(R.layout.fragment_onboarding) {

    private lateinit var binding: FragmentOnboardingBinding
    private lateinit var adapter: OnboardingPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOnboardingBinding.bind(view)
        adapter = OnboardingPagerAdapter(requireActivity())
        binding.viewpager2.adapter = adapter
    }
}