package com.example.smartstay

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.smartstay.databinding.FragmentInitialSettingEndBinding

class InitialSettingEndFragment: Fragment(R.layout.fragment_initial_setting_end) {

    private lateinit var binding: FragmentInitialSettingEndBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentInitialSettingEndBinding.bind(view)
        binding.btnInitialSettingEnd.setOnClickListener {
            startActivity(Intent(requireContext(), ChatActivity::class.java))
        }
    }

}