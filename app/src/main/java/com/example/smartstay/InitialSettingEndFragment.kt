package com.example.smartstay

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.smartstay.databinding.FragmentInitialSettingEndBinding
import androidx.fragment.app.activityViewModels

class InitialSettingEndFragment: Fragment(R.layout.fragment_initial_setting_end) {

    private lateinit var binding: FragmentInitialSettingEndBinding
    private val viewModel by activityViewModels<InitialSettingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentInitialSettingEndBinding.bind(view)
        Log.d("ttest",""+viewModel.userInitialInfoList)
        var userInfo = viewModel.userInitialInfoList.joinToString(", ")
        Log.d("ttest",""+userInfo)
        binding.tvTestResult.text = userInfo
        binding.btnInitialSettingEnd.setOnClickListener {
            startActivity(Intent(requireContext(), ChatActivity::class.java))
        }
    }

}