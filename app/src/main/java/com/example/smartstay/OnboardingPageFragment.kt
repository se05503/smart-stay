package com.example.smartstay

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.smartstay.databinding.FragmentOnboardingPageBinding
import com.example.smartstay.model.QuestionItem
import com.google.android.material.card.MaterialCardView
import com.google.android.material.slider.Slider

class OnboardingPageFragment : Fragment(R.layout.fragment_onboarding_page) {

    private lateinit var binding: FragmentOnboardingPageBinding
    private val viewModel: InitialSettingViewModel by activityViewModels()
    private var callback: OnboardingPageCallback? = null
    private var progress: Int = 0

    companion object {
        fun newInstance(item: QuestionItem): OnboardingPageFragment {
            val fragment = OnboardingPageFragment()
            val arguments = Bundle().apply {
                putString(KEY_QUESTION_TITLE, item.questionTitle)
                putInt(KEY_QUESTION_PROGRESS, item.progress)
            }
            fragment.arguments = arguments
            return fragment
        }
        const val KEY_QUESTION_TITLE = "question_title"
        const val KEY_QUESTION_PROGRESS = "question_progress"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = parentFragment as OnboardingPageCallback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOnboardingPageBinding.bind(view)
        val viewStub = binding.viewStub // refactoring
        initViews()
        when(progress) {
            1,4,5,7 -> {
                // 이중 선택지
                viewStub.layoutResource = R.layout.layout_initial_setting_dual_button
                val inflatedView = viewStub.inflate()
                val tvOption1 = inflatedView.findViewById<TextView>(R.id.tv_dual_option_1)
                val tvOption2 = inflatedView.findViewById<TextView>(R.id.tv_dual_option_2)
                val cvOption1 = inflatedView.findViewById<MaterialCardView>(R.id.cv_option_1)
                val cvOption2 = inflatedView.findViewById<MaterialCardView>(R.id.cv_option_2)

                when(progress) {
                    1 -> {

                        tvOption1.text = getString(R.string.option_female)
                        tvOption2.text = getString(R.string.option_male)

                        cvOption1.setOnClickListener {
                            if(!cvOption1.isSelected) {
                                cvOption1.isSelected = true
                                cvOption2.isSelected = false
                                viewModel.userInitialInfoMap["gender"] = Constants.GENDER_FEMALE
                            }
                        }

                        cvOption2.setOnClickListener {
                            if(!cvOption2.isSelected) {
                                cvOption2.isSelected = true
                                cvOption1.isSelected = false
                                viewModel.userInitialInfoMap["gender"] = Constants.GENDER_MALE
                            }
                        }
                    }
                    4 -> {
                        tvOption1.text = getString(R.string.option_married)
                        tvOption2.text = getString(R.string.option_single)
                        cvOption1.setOnClickListener {
                            if(!cvOption1.isSelected) {
                                cvOption1.isSelected = true
                                cvOption2.isSelected = false
                                viewModel.userInitialInfoMap["marriage_status"] = Constants.MARRIAGE_MARRIED
                            }
                        }
                        cvOption2.setOnClickListener {
                            if(!cvOption2.isSelected) {
                                cvOption2.isSelected = true
                                cvOption1.isSelected = false
                                viewModel.userInitialInfoMap["marriage_status"] = Constants.MARRIAGE_SINGLE
                            }
                        }
                    }
                    5 -> {
                        tvOption1.text = getString(R.string.option_present)
                        tvOption2.text = getString(R.string.option_absent)
                        cvOption1.setOnClickListener {
                            if(!cvOption1.isSelected) {
                                cvOption1.isSelected = true
                                cvOption2.isSelected = false
                                viewModel.userInitialInfoMap["children_status"] = Constants.HAS_CHILDREN
                            }
                        }
                        cvOption2.setOnClickListener {
                            if(!cvOption2.isSelected) {
                                cvOption2.isSelected = true
                                cvOption1.isSelected = false
                                viewModel.userInitialInfoMap["children_status"] = Constants.NO_CHILDREN
                            }
                        }
                    }
                    7 -> {
                        tvOption1.text = getString(R.string.option_present)
                        tvOption2.text = getString(R.string.option_absent)
                        cvOption1.setOnClickListener {
                            if(!cvOption1.isSelected) {
                                cvOption1.isSelected = true
                                cvOption2.isSelected = false
                                viewModel.userInitialInfoMap["companion_status"] = Constants.HAS_COMPANION
                            }
                        }
                        cvOption2.setOnClickListener {
                            if(!cvOption2.isSelected) {
                                cvOption2.isSelected = true
                                cvOption1.isSelected = false
                                viewModel.userInitialInfoMap["companion_status"] = Constants.NO_COMPANION
                            }
                        }
                    }
                }
            }
            3,8 -> {
                // 다중 선택지 - view
                viewStub.layoutResource = R.layout.layout_initial_setting_multiple_button
                val inflatedView = viewStub.inflate()
                val textIds = listOf(
                    R.id.tv_multiple_option_1,
                    R.id.tv_multiple_option_2,
                    R.id.tv_multiple_option_3,
                    R.id.tv_multiple_option_4,
                    R.id.tv_multiple_option_5,
                    R.id.tv_multiple_option_6,
                    R.id.tv_multiple_option_7,
                    R.id.tv_multiple_option_8,
                    R.id.tv_multiple_option_9,
                    R.id.tv_multiple_option_10,
                    R.id.tv_multiple_option_11,
                    R.id.tv_multiple_option_12,
                    R.id.tv_multiple_option_13,
                    R.id.tv_multiple_option_14
                )
                val cardIds = listOf(
                    R.id.cv_multiple_option_1,
                    R.id.cv_multiple_option_2,
                    R.id.cv_multiple_option_3,
                    R.id.cv_multiple_option_4,
                    R.id.cv_multiple_option_5,
                    R.id.cv_multiple_option_6,
                    R.id.cv_multiple_option_7,
                    R.id.cv_multiple_option_8,
                    R.id.cv_multiple_option_9,
                    R.id.cv_multiple_option_10,
                    R.id.cv_multiple_option_11,
                    R.id.cv_multiple_option_12,
                    R.id.cv_multiple_option_13,
                    R.id.cv_multiple_option_14,
                )

                when(progress) {
                    3 -> {
                        val jobNames = listOf(
                            R.string.job_manager,
                            R.string.job_skilled_worker,
                            R.string.job_technician,
                            R.string.job_office_worker,
                            R.string.job_general_worker,
                            R.string.job_self_employed,
                            R.string.job_professional,
                            R.string.job_sales_service,
                            R.string.job_freelancer,
                            R.string.job_housewife,
                            R.string.job_student,
                            R.string.job_exam_prep,
                            R.string.job_unemployed,
                            R.string.job_etc
                        )
                        var selectedCard: MaterialCardView? = null

                        textIds.zip(jobNames).forEach { (textId, jobName) ->
                            inflatedView.findViewById<TextView>(textId).text = getString(jobName)
                        }
                        inflatedView.findViewById<MaterialCardView>(R.id.cv_multiple_option_15).visibility = View.INVISIBLE // chain 때문에 놔둠 (refactoring)

                        // 다중 선택지 - event
                        inflatedView.apply {
                            cardIds.zip(jobNames).forEach { (cardId, jobName) ->
                                val card = findViewById<MaterialCardView>(cardId)
                                card.setOnClickListener {
                                    if(!card.isSelected) {
                                        selectedCard?.isSelected = false // 기존 카드 선택 해제
                                        card.isSelected = true
                                        selectedCard = card
                                        viewModel.userInitialInfoMap["job"] = getString(jobName)
                                    }
                                }
                            }
                        }
                    }
                    8 -> {
                        val companionNames = listOf(
                            R.string.companion_spouse,
                            R.string.companion_parents,
                            R.string.companion_children,
                            R.string.companion_siblings,
                            R.string.companion_friend,
                            R.string.companion_coworker,
                            R.string.companion_lover,
                            R.string.companion_pet,
                            R.string.companion_alone,
                            R.string.companion_other
                        )
                        textIds.zip(companionNames).forEach { (textId, companionName) ->
                            inflatedView.findViewById<TextView>(textId).text = getString(companionName)
                        }
                        // 11 ~ 15 invisible 처리
                        inflatedView.findViewById<MaterialCardView>(R.id.cv_multiple_option_11).visibility = View.INVISIBLE
                        inflatedView.findViewById<MaterialCardView>(R.id.cv_multiple_option_12).visibility = View.INVISIBLE
                        inflatedView.findViewById<MaterialCardView>(R.id.cv_multiple_option_13).visibility = View.INVISIBLE
                        inflatedView.findViewById<MaterialCardView>(R.id.cv_multiple_option_14).visibility = View.INVISIBLE
                        inflatedView.findViewById<MaterialCardView>(R.id.cv_multiple_option_15).visibility = View.INVISIBLE

                        var companionList = mutableListOf<String>()
                        cardIds.zip(companionNames).forEach { (cardId, companionName) ->
                            val card = inflatedView.findViewById<MaterialCardView>(cardId)
                            card.setOnClickListener {
                                if(!card.isSelected) {
                                    card.isSelected = true
                                    companionList.add(getString(companionName))
                                    viewModel.userInitialInfoMap["companion"] = companionList
                                } else {
                                    card.isSelected = false
                                    companionList.remove(getString(companionName))
                                    viewModel.userInitialInfoMap["companion"] = companionList
                                }
                            }
                        }
                    }
                }

            }
            2,6,9 -> {
                // 슬라이더
                viewStub.layoutResource = R.layout.layout_initial_setting_slider
                val inflatedView = viewStub.inflate()
                val slider = inflatedView.findViewById<Slider>(R.id.slider)
                val startValue = inflatedView.findViewById<TextView>(R.id.tv_value_start)
                val endValue = inflatedView.findViewById<TextView>(R.id.tv_value_end)
                when(progress) {
                    2 -> {
                        // 나이
                        slider.apply {
                            valueFrom = 1.0f
                            valueTo = 100.0f
                            value = 1.0f
                            setLabelFormatter { value -> "${value.toInt()}살" }
                            addOnSliderTouchListener(object: Slider.OnSliderTouchListener {
                                override fun onStartTrackingTouch(slider: Slider) = Unit
                                override fun onStopTrackingTouch(slider: Slider) {
                                    viewModel.userInitialInfoMap["age"] = slider.value.toInt()
                                }
                            })
                        }
                        startValue.text = "1살"
                        endValue.text = "100살"
                    }
                    6 -> {
                        // 가족 인원수
                        slider.apply {
                            valueFrom = 3.0f
                            valueTo = 10.0f
                            value = 3.0f
                            setLabelFormatter { value ->
                                "${value.toInt()}명"
                            }
                            addOnSliderTouchListener(object: Slider.OnSliderTouchListener {
                                override fun onStartTrackingTouch(slider: Slider) = Unit
                                override fun onStopTrackingTouch(slider: Slider) {
                                    viewModel.userInitialInfoMap["family_count"] = slider.value.toInt()
                                }

                            })
                        }
                        startValue.text = "3명"
                        endValue.text = "10명"
                    }
                    9 -> {
                        // 1인당 평균 월수익
                        slider.apply {
                            valueFrom = 0.0f
                            valueTo = 1000.0f
                            value = 0.0f
                            setLabelFormatter { value ->
                                if(value.toInt()==1000) "${value.toInt()}만원~"
                                else "${value.toInt()}만원"
                            }
                            addOnSliderTouchListener(object: Slider.OnSliderTouchListener {
                                override fun onStartTrackingTouch(slider: Slider) = Unit
                                override fun onStopTrackingTouch(slider: Slider) {
                                    viewModel.userInitialInfoMap["income_per_person"] = slider.value
                                }
                            })
                        }
                        startValue.text = "0만원"
                        endValue.text = "1,000만원~"
                    }
                }
            }
        }

        initListeners()

    }

    private fun initListeners() = with(binding) {
        btnNextPage.setOnClickListener {
            callback?.onNextPage()
        }
    }

    private fun initViews() = with(binding) {
        tvQuestion.text = arguments?.getString(KEY_QUESTION_TITLE)
        progress = arguments?.getInt(KEY_QUESTION_PROGRESS) ?: -1
        progressBar.setProgress(progress, true) // refactoring
    }

    override fun onDetach() {
        super.onDetach()
        callback = null
    }

}