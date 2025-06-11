package com.example.smartstay

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.smartstay.model.QuestionItem

class OnboardingPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {

    private val fragments = listOf<QuestionItem>(
        QuestionItem("성별이 어떻게 되세요?", 1),
        QuestionItem("나이가 어떻게 되세요?", 2),
        QuestionItem("직업이 어떻게 되세요?", 3),
        QuestionItem("결혼 여부가 어떻게 되세요?", 4),
        QuestionItem("자녀가 있으신가요?", 5),
        QuestionItem("가족 인원은 몇명이세요?", 6),
        QuestionItem("동행자가 있으신가요?", 7),
        QuestionItem("구성원 1인당 평균 월수익이\n대략 얼마인가요?", 8)
    )

    override fun createFragment(position: Int): Fragment {
        val fragment = fragments[position]
        return OnboardingPageFragment.newInstance(fragment)
    }

    override fun getItemCount() = fragments.size

}