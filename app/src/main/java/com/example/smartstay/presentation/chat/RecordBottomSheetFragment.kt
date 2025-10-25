package com.example.smartstay.presentation.chat

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.smartstay.R
import com.example.smartstay.databinding.BottomSheetRecordBinding

class RecordBottomSheetFragment: BottomSheetDialogFragment(R.layout.bottom_sheet_record) {

    private lateinit var binding: BottomSheetRecordBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = BottomSheetRecordBinding.bind(view)
        initListeners()
    }

    private fun initListeners() = with(binding) {
        tvBackToChat.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        const val TAG = "RECORD_BOTTOM_SHEET_DIALOG"
    }
}