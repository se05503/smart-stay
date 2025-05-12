package com.example.smartstay

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.google.android.material.slider.Slider

class OnboardingPageFragment: Fragment() {

    private var callback: OnboardingPageCallback? = null

    companion object {
        fun newInstance(item: QuestionItem): OnboardingPageFragment {
            val fragment = OnboardingPageFragment()
            val args = Bundle().apply {
                putString("text", item.pageText)
                putInt("progress", item.progress)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = parentFragment as? OnboardingPageCallback
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_onboarding_page, container, false)
        val text = arguments?.getString("text")
        val progress = arguments?.getInt("progress") ?: -1
        view.findViewById<TextView>(R.id.tv_question).text = text
        view.findViewById<ProgressBar>(R.id.progressBar).setProgress(progress, true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<AppCompatButton>(R.id.btn_next_page).setOnClickListener {
            Log.d("ttest",""+callback)
            callback?.onNextPage()
        }

        // 나이
        val sliderAge = view.findViewById<Slider>(R.id.slider_age)
        sliderAge.setLabelFormatter { value ->
            "${value.toInt()}살"
        }
        sliderAge.addOnSliderTouchListener(object: Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {

            }

            override fun onStopTrackingTouch(slider: Slider) {
                Toast.makeText(requireContext(), "value:${slider.value.toInt()}살", Toast.LENGTH_SHORT).show()
            }

        })

        // 가족 인원수
        val sliderFamilyCount = view.findViewById<Slider>(R.id.slider_family_count)
        sliderFamilyCount.setLabelFormatter { value ->
            "${value.toInt()}명"
        }
        sliderFamilyCount.addOnSliderTouchListener(object: Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {

            }

            override fun onStopTrackingTouch(slider: Slider) {
                Toast.makeText(requireContext(), "value:${slider.value.toInt()}명", Toast.LENGTH_SHORT).show()
            }

        })

        // 1인당 평균 월수익
        val sliderIncome = view.findViewById<Slider>(R.id.slider_income)
        sliderIncome.setLabelFormatter { value ->
            if(value.toInt()==1000) "${value.toInt()}만원~"
            else "${value.toInt()}만원"
        }
        sliderIncome.addOnSliderTouchListener(object: Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {

            }

            override fun onStopTrackingTouch(slider: Slider) {
                Toast.makeText(requireContext(), "value:${slider.value.toInt()}만원", Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }
}