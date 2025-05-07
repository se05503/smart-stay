package com.example.smartstay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class OnboardingPageFragment: Fragment() {

    companion object {
        fun newInstance(text: String): OnboardingPageFragment {
            val fragment = OnboardingPageFragment()
            val args = Bundle().apply {
                putString("text", text)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_onboarding_page, container, false)
        val text = arguments?.getString("text")
        view.findViewById<TextView>(R.id.textView).text = text
        return view
    }

}