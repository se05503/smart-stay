package com.example.smartstay

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.smartstay.databinding.FragmentOnboardingBinding

class OnboardingFragment: Fragment(R.layout.fragment_onboarding), OnboardingPageCallback {

    private lateinit var binding: FragmentOnboardingBinding
    private lateinit var adapter: OnboardingPagerAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOnboardingBinding.bind(view)
        adapter = OnboardingPagerAdapter(this)
        binding.viewpager2.adapter = adapter
        binding.viewpager2.isUserInputEnabled = false // 스와이프 막기
    }

    override fun onNextPage() = with(binding) {
        val nextItem  = viewpager2.currentItem + 1
        Log.d("ttest","current: ${viewpager2.currentItem}, total: ${viewpager2.adapter?.itemCount}")
        if(nextItem < viewpager2.adapter!!.itemCount) {
            viewpager2.currentItem = nextItem
        } else {
            findNavController().navigate(R.id.action_navigation_onboarding_to_navigation_initial_setting_end)
        }
    }
}