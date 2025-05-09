package com.example.smartstay

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingPagerAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf<QuestionItem>(
        QuestionItem("1", 1),
        QuestionItem("2", 2),
        QuestionItem("3", 3),
        QuestionItem("4", 4),
        QuestionItem("5", 5),
        QuestionItem("6", 6),
        QuestionItem("7", 7)
    )

    override fun createFragment(position: Int): Fragment {
        val fragment = fragments[position]
        return OnboardingPageFragment.newInstance(fragment)
    }

    override fun getItemCount() = fragments.size

}