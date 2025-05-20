package com.example.smartstay

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.google.android.material.slider.Slider

class OnboardingPageFragment : Fragment() {

    private var callback: OnboardingPageCallback? = null
    private var progress: Int = 0
    private val viewModel: InitialSettingViewModel by activityViewModels()

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
        progress = arguments?.getInt("progress") ?: -1
        view.findViewById<TextView>(R.id.tv_question).text = text
        view.findViewById<LinearProgressIndicator>(R.id.progressBar).setProgress(progress, true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewStub = view.findViewById<ViewStub>(R.id.view_stub)
        when(progress) {
            1,4,5 -> {
                // 이중 선택지
                viewStub.layoutResource = R.layout.layout_initial_setting_dual_button
                val inflatedView = viewStub.inflate()

                when(progress) {
                    1 -> {
                        inflatedView.findViewById<TextView>(R.id.tv_dual_option_1).text = "여자"
                        inflatedView.findViewById<TextView>(R.id.tv_dual_option_2).text = "남자"
                        inflatedView.findViewById<MaterialCardView>(R.id.cv_option_1).setOnClickListener {
                            it.isSelected = !it.isSelected
                            if(it.isSelected == true) viewModel.userInitialInfoList.add("F")
                        }
                        inflatedView.findViewById<MaterialCardView>(R.id.cv_option_2).setOnClickListener {
                            it.isSelected = !it.isSelected
                            if(it.isSelected == true) viewModel.userInitialInfoList.add("M")
                        }
                    }
                    4 -> {
                        inflatedView.findViewById<TextView>(R.id.tv_dual_option_1).text = "기혼"
                        inflatedView.findViewById<TextView>(R.id.tv_dual_option_2).text = "미혼"
                        inflatedView.findViewById<MaterialCardView>(R.id.cv_option_1).setOnClickListener {
                            it.isSelected = !it.isSelected
                            if(it.isSelected == true) viewModel.userInitialInfoList.add("Y")
                        }
                        inflatedView.findViewById<MaterialCardView>(R.id.cv_option_2).setOnClickListener {
                            it.isSelected = !it.isSelected
                            if(it.isSelected == true) viewModel.userInitialInfoList.add("N")
                        }
                    }
                    5 -> {
                        inflatedView.findViewById<TextView>(R.id.tv_dual_option_1).text = "있음"
                        inflatedView.findViewById<TextView>(R.id.tv_dual_option_2).text = "없음"
                        inflatedView.findViewById<MaterialCardView>(R.id.cv_option_1).setOnClickListener {
                            it.isSelected = !it.isSelected
                            if(it.isSelected == true) viewModel.userInitialInfoList.add("Y")
                        }
                        inflatedView.findViewById<MaterialCardView>(R.id.cv_option_2).setOnClickListener {
                            it.isSelected = !it.isSelected
                            if(it.isSelected == true) viewModel.userInitialInfoList.add("N")
                        }
                    }
                }
            }
            3 -> {
                // 다중 선택지 - view
                viewStub.layoutResource = R.layout.layout_intial_setting_multiple_button
                val inflatedView = viewStub.inflate()
                inflatedView.findViewById<TextView>(R.id.tv_multiple_option_1).text = "경영/관리직"
                inflatedView.findViewById<TextView>(R.id.tv_multiple_option_2).text = "기능/숙련공"
                inflatedView.findViewById<TextView>(R.id.tv_multiple_option_3).text = "기술직"
                inflatedView.findViewById<TextView>(R.id.tv_multiple_option_4).text = "사무직"
                inflatedView.findViewById<TextView>(R.id.tv_multiple_option_5).text = "일반 작업직"
                inflatedView.findViewById<TextView>(R.id.tv_multiple_option_6).text = "자영업"
                inflatedView.findViewById<TextView>(R.id.tv_multiple_option_7).text = "전문직"
                inflatedView.findViewById<TextView>(R.id.tv_multiple_option_8).text = "판매/\n서비스직"
                inflatedView.findViewById<TextView>(R.id.tv_multiple_option_9).text = "자유직"
                inflatedView.findViewById<TextView>(R.id.tv_multiple_option_10).text = "전업주부"
                inflatedView.findViewById<TextView>(R.id.tv_multiple_option_11).text = "대학(원)생"
                inflatedView.findViewById<TextView>(R.id.tv_multiple_option_12).text = "재수/\n입시/\n유학준비"
                inflatedView.findViewById<TextView>(R.id.tv_multiple_option_13).text = "무직"
                inflatedView.findViewById<TextView>(R.id.tv_multiple_option_14).text = "기타"
                inflatedView.findViewById<MaterialCardView>(R.id.cv_multiple_option_15).visibility = View.INVISIBLE

                // 다중 선택지 - event
                inflatedView.apply {
                    findViewById<MaterialCardView>(R.id.cv_multiple_option_1).setOnClickListener {
                        it.isSelected = !it.isSelected
                        if(it.isSelected == true) viewModel.userInitialInfoList.add("경영/관리직")
                    }
                    findViewById<MaterialCardView>(R.id.cv_multiple_option_2).setOnClickListener {
                        it.isSelected = !it.isSelected
                        if(it.isSelected == true) viewModel.userInitialInfoList.add("기능/숙련공")
                    }
                    findViewById<MaterialCardView>(R.id.cv_multiple_option_3).setOnClickListener {
                        it.isSelected = !it.isSelected
                        if(it.isSelected == true) viewModel.userInitialInfoList.add("기술직")
                    }
                    findViewById<MaterialCardView>(R.id.cv_multiple_option_4).setOnClickListener {
                        it.isSelected = !it.isSelected
                        if(it.isSelected == true) viewModel.userInitialInfoList.add("사무직")
                    }
                    findViewById<MaterialCardView>(R.id.cv_multiple_option_5).setOnClickListener {
                        it.isSelected = !it.isSelected
                        if(it.isSelected == true) viewModel.userInitialInfoList.add("일반 작업직")
                    }
                    findViewById<MaterialCardView>(R.id.cv_multiple_option_6).setOnClickListener {
                        it.isSelected = !it.isSelected
                        if(it.isSelected == true) viewModel.userInitialInfoList.add("자영업")
                    }
                    findViewById<MaterialCardView>(R.id.cv_multiple_option_7).setOnClickListener {
                        it.isSelected = !it.isSelected
                        if(it.isSelected == true) viewModel.userInitialInfoList.add("전문직")
                    }
                    findViewById<MaterialCardView>(R.id.cv_multiple_option_8).setOnClickListener {
                        it.isSelected = !it.isSelected
                        if(it.isSelected == true) viewModel.userInitialInfoList.add("판매/서비스직")
                    }
                    findViewById<MaterialCardView>(R.id.cv_multiple_option_9).setOnClickListener {
                        it.isSelected = !it.isSelected
                        if(it.isSelected == true) viewModel.userInitialInfoList.add("자유직")
                    }
                    findViewById<MaterialCardView>(R.id.cv_multiple_option_10).setOnClickListener {
                        it.isSelected = !it.isSelected
                        if(it.isSelected == true) viewModel.userInitialInfoList.add("전업주부")
                    }
                    findViewById<MaterialCardView>(R.id.cv_multiple_option_11).setOnClickListener {
                        it.isSelected = !it.isSelected
                        if(it.isSelected == true) viewModel.userInitialInfoList.add("대학(원)생")
                    }
                    findViewById<MaterialCardView>(R.id.cv_multiple_option_12).setOnClickListener {
                        it.isSelected = !it.isSelected
                        if(it.isSelected == true) viewModel.userInitialInfoList.add("재수/입시/유학 준비")
                    }
                    findViewById<MaterialCardView>(R.id.cv_multiple_option_13).setOnClickListener {
                        it.isSelected = !it.isSelected
                        if(it.isSelected == true) viewModel.userInitialInfoList.add("무직")
                    }
                    findViewById<MaterialCardView>(R.id.cv_multiple_option_14).setOnClickListener {
                        it.isSelected = !it.isSelected
                        if(it.isSelected == true) viewModel.userInitialInfoList.add("기타")
                    }
                    findViewById<MaterialCardView>(R.id.cv_multiple_option_15).setOnClickListener {
                        it.isSelected = !it.isSelected
                    }
                }
            }
            2, 6, 7 -> {
                // 슬라이더
                viewStub.layoutResource = R.layout.layout_initial_setting_slider
                val inflatedView = viewStub.inflate()
                when(progress) {
                    2 -> {
                        // 나이
                        val sliderAge = inflatedView.findViewById<Slider>(R.id.slider)
                        sliderAge.valueFrom = 1.0f
                        sliderAge.valueTo = 100.0f
                        sliderAge.value = 1.0f
                        sliderAge.setLabelFormatter { value ->
                            "${value.toInt()}살"
                        }
                        sliderAge.addOnSliderTouchListener(object: Slider.OnSliderTouchListener {
                            override fun onStartTrackingTouch(slider: Slider) {

                            }

                            override fun onStopTrackingTouch(slider: Slider) {
                                Toast.makeText(requireContext(), "value:${slider.value.toInt()}살", Toast.LENGTH_SHORT).show()
                                viewModel.userInitialInfoList.add("${slider.value.toInt()}")
                            }

                        })
                        inflatedView.findViewById<TextView>(R.id.tv_value_start).text = "1살"
                        inflatedView.findViewById<TextView>(R.id.tv_value_end).text = "100살"
                    }
                    6 -> {
                        // 가족 인원수
                        val sliderFamilyCount = view.findViewById<Slider>(R.id.slider)
                        sliderFamilyCount.valueFrom = 3.0f
                        sliderFamilyCount.valueTo = 10.0f
                        sliderFamilyCount.value = 3.0f
                        sliderFamilyCount.setLabelFormatter { value ->
                            "${value.toInt()}명"
                        }
                        sliderFamilyCount.addOnSliderTouchListener(object: Slider.OnSliderTouchListener {
                            override fun onStartTrackingTouch(slider: Slider) {

                            }

                            override fun onStopTrackingTouch(slider: Slider) {
                                Toast.makeText(requireContext(), "value:${slider.value.toInt()}명", Toast.LENGTH_SHORT).show()
                                viewModel.userInitialInfoList.add("${slider.value.toInt()}")
                            }

                        })
                        inflatedView.findViewById<TextView>(R.id.tv_value_start).text = "3명"
                        inflatedView.findViewById<TextView>(R.id.tv_value_end).text = "10명"
                    }
                    7 -> {
                        // 1인당 평균 월수익
                        val sliderIncome = view.findViewById<Slider>(R.id.slider)
                        sliderIncome.valueFrom = 0.0f
                        sliderIncome.valueTo = 1000.0f
                        sliderIncome.value = 0.0f
                        sliderIncome.setLabelFormatter { value ->
                            if(value.toInt()==1000) "${value.toInt()}만원~"
                            else "${value.toInt()}만원"
                        }
                        sliderIncome.addOnSliderTouchListener(object: Slider.OnSliderTouchListener {
                            override fun onStartTrackingTouch(slider: Slider) {

                            }

                            override fun onStopTrackingTouch(slider: Slider) {
                                Toast.makeText(requireContext(), "value:${slider.value.toInt()}만원", Toast.LENGTH_SHORT).show()
                                viewModel.userInitialInfoList.add("${slider.value.toInt()}")
                            }

                        })
                        inflatedView.findViewById<TextView>(R.id.tv_value_start).text = "0만원"
                        inflatedView.findViewById<TextView>(R.id.tv_value_end).text = "1,000만원~"
                    }
                }
            }
        }

        view.findViewById<AppCompatButton>(R.id.btn_next_page).setOnClickListener {
            callback?.onNextPage()
        }

    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }
}