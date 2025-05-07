package com.example.smartstay

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf<String>("One", "Two", "Three", "Four", "Five")

    override fun createFragment(position: Int): Fragment {
        val fragment = fragments[position]
        return OnboardingPageFragment.newInstance(fragment)
    }

    override fun getItemCount() = fragments.size

}